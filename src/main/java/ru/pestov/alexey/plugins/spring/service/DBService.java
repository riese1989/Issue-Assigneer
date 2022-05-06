package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ru.pestov.alexey.plugins.spring.dbmanager.*;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.enums.Mode;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
@Getter
@ExportAsService
public class DBService {
    private final UMManager userModelManager;
    private final UserService userService;
    private final StMManager stageModelManager;
    private final SystemService systemService;
    private final SMManager systemModelManager;
    private final TypeChangeService typeChangeService;
    private final TCMManager typeChangeModelManager;
    private final JSONService jsonService;
    private final SAManager SAManager;
    private final DMManager dmManager;
    private final TCAManager tcaManager;
    private final LogModelManager logModelManager;

    @Inject
    public DBService(UMManager UMManager, UserService userService, StMManager stageModelManager,
                     SystemService systemService, SMManager SMManager, TypeChangeService typeChangeService,
                     TCMManager typeChangeModelManager, JSONService jsonService, SAManager SAManager,
                     DMManager dmManager, TCAManager tcaManager, LogModelManager logModelManager) {
        this.userModelManager = UMManager;
        this.userService = userService;
        this.stageModelManager = stageModelManager;
        this.systemService = systemService;
        this.systemModelManager = SMManager;
        this.typeChangeService = typeChangeService;
        this.typeChangeModelManager = typeChangeModelManager;
        this.jsonService = jsonService;
        this.SAManager = SAManager;
        this.dmManager = dmManager;
        this.tcaManager = tcaManager;
        this.logModelManager = logModelManager;
    }

    public Integer recoverDB() {
        clearDB();
        jsonService.createJSONObject(Mode.FILE);
        recoverUser();
        recoverStage();
        recoverSystems();
        recoverTypeChanges();
        recoverSystemAssignees();
        recoverDelivery();
        recoverTypeChangeAssignee();
        return 1;
    }

    private void clearDB() {
        logModelManager.deleteAll();
        SAManager.deleteAll();
        dmManager.deleteAll();
        systemModelManager.deleteAll();
        userModelManager.deleteAll();
        typeChangeModelManager.deleteAll();
        stageModelManager.deleteAll();
        tcaManager.deleteAll();
    }

    private void recoverUser() {
        List<ApplicationUser> users = userService.getUsersJira();
        for (ApplicationUser user : users) {
            userModelManager.createUser(user);
        }
    }

    private void recoverStage() {
        List<String> stages = Arrays.asList("stage1", "stage21", "stage22", "stage23", "stage3", "authorize");
        for (String stage : stages) {
            stageModelManager.createStage(stage);
        }
    }

    private void recoverSystems() {
        HashMap<String, Boolean> mapSystems = systemService.getMapSystemActive();
        for (Map.Entry<String, Boolean> entry : mapSystems.entrySet()) {
            String name = entry.getKey();
            Boolean active = entry.getValue();
            systemModelManager.createSystem(name, active);
        }
    }

    private void recoverTypeChanges() {
        HashMap<String, String> typeChanges = typeChangeService.getTypeChanges();
        for (String name : typeChanges.values()) {
            typeChangeModelManager.createTypeChange(name);
        }
    }

    private void recoverSystemAssignees() {
        JSONObject jsonObject = jsonService.getJsonObject();
        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String systemName = iterator.next();
            System system = systemModelManager.getSystemByName(systemName);
            JSONObject systemJSON = (JSONObject) jsonObject.get(systemName);
            Set<String> typeChanges = systemJSON.keySet();
            Iterator<String> iteratorTypeChanges = typeChanges.iterator();
            while (iteratorTypeChanges.hasNext()) {
                String typeChangeName = iteratorTypeChanges.next();
                if (typeChangeName.equals("system_active")) {
                    continue;
                }
                TypeChangeDB typeChangeDB = typeChangeModelManager.getTypeChangeByName(typeChangeName);
                if (typeChangeDB == null) {
                    int i = 0;
                }
                JSONObject stagesJSON = new JSONObject();
                try {
                    stagesJSON = (JSONObject) systemJSON.get(typeChangeDB.getName());
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
                Set<String> stages = stagesJSON.keySet();
                Iterator<String> iteratorStages = stages.iterator();
                while (iteratorStages.hasNext()) {
                    String nameStage = iteratorStages.next();
                    Stage stage = stageModelManager.getStageByName(nameStage);
                    List<User> assignees = getAssignees(stage.getName(), stagesJSON);
                    if (assignees != null) {
                        for (User assignee : assignees) {
                            SAManager.createSystemAssignee(system, typeChangeDB, stage, assignee);
                        }
                    }
                }
            }
        }
    }

    private List<User> getAssignees(String key, JSONObject jsonObject) {
        List<User> users = new ArrayList();
        try {
            JSONArray assigneesJSON = (JSONArray) jsonObject.get(key);
            if (assigneesJSON == null) {
                return null;
            }

            for (int i = 0; i < assigneesJSON.size(); ++i) {
                User user = userModelManager.getUserByName((String) assigneesJSON.get(i));
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (ClassCastException ex) {
            String nameUser = jsonObject.get(key).toString();
            users.add(userModelManager.getUserByName(nameUser));
        }
        return users;
    }

    private void recoverDelivery() {
        JSONObject jsonObject = jsonService.getJsonDelivery();
        Iterator keys = jsonObject.keySet().iterator();
        while (keys.hasNext()) {
            String nameSystem = (String) keys.next();
            String nameDelivery = jsonObject.get(nameSystem).toString().replaceAll("@x5.ru", "");
            User delivery = userModelManager.getUserByName(nameDelivery);
            if (delivery == null) {
                delivery = userModelManager.createUser(nameDelivery);
            }
            System system = systemModelManager.getSystemByName(nameSystem);
            dmManager.createDelivery(system, delivery);
        }
    }

    public List<SystemAssignees> updateDB(Param param) {
        Date date = new Date();
        List<SystemAssignees> result = new ArrayList<>();
        Integer idSystem = param.getSystemId();
        System system = systemModelManager.getSystemById(idSystem);
        param.setSystem(system);
        Integer idTypeChange = param.getTypeChangeId();
        TypeChangeDB typeChangeDB = typeChangeModelManager.getTypeChangeById(idTypeChange);
        param.setTypeChangeDB(typeChangeDB);
        Boolean isActive = Boolean.valueOf(param.getActive());
        List<Stage> stages = Arrays.asList(stageModelManager.getAllStages());
        User currentUser = userModelManager.getUserByName(ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser().getName());
        List<SystemAssignees> oldSystemAssignees = Arrays.asList(SAManager.getAssignees(idSystem, idTypeChange));
        for (SystemAssignees systemAssignee : oldSystemAssignees) {
            logModelManager.create(date, systemAssignee, tcaManager.get(2), currentUser);
        }
        system = systemModelManager.setActive(idSystem, isActive);
        SAManager.deleteObjects(idSystem, idTypeChange);
        for (Stage stage : stages) {
            List<String> nameUsers = param.getRequiredStage(stage.getName());
            for (String nameUser : nameUsers) {
                User user = userModelManager.getUserByName(nameUser);
                if (user != null) {
                    SystemAssignees systemAssigneeNew = SAManager.createSystemAssignee(system, typeChangeDB, stage, user);
                    result.add(systemAssigneeNew);
                    logModelManager.create(date, systemAssigneeNew, tcaManager.get(1), currentUser);
                }
            }
        }
        List<SystemAssignees> newSystemAssignees = Arrays.asList(SAManager.getAssignees(idSystem, idTypeChange));
        if (param.getDelivery() != null) {
            updateDeliveryDB(date, param, currentUser);
        }
        return result;
    }

    private void updateDeliveryDB(Date date, Param param, User currentUser) {
        System system = param.getSystem();
        Delivery delivery = dmManager.getDelivery(system);
        if (delivery != null) {
            User oldUser = delivery.getUser();
            dmManager.delete(system);
            logModelManager.create(date, oldUser, system, tcaManager.get(4), currentUser);
        }
        User user = userModelManager.getUserByName(param.getDelivery());
        if (user != null) {
            Delivery newDelivery = dmManager.createDelivery(system, user);
            logModelManager.create(date, newDelivery.getUser(), system, tcaManager.get(3), currentUser);
        }

    }

    private void recoverTypeChangeAssignee() {
        tcaManager.create("Create");
        tcaManager.create("Delete");
        tcaManager.create("Create delivery");
        tcaManager.create("Delete delivery");
    }

    public String getDelivery(Integer idSystem) {
        System system = systemModelManager.getSystemById(idSystem);
        Delivery delivery = dmManager.getDelivery(system);
        if (delivery != null) {
            User user = delivery.getUser();
            return String.valueOf(user.getID());
        }
        return null;
    }

    public Boolean isCurrentUserDelivery(Integer idSystem, ApplicationUser currentUser) {
        System system = systemModelManager.getSystemById(idSystem);
        Delivery delivery = dmManager.getDelivery(system);
        if (delivery != null) {
            return delivery.getUser().getName().equals(currentUser.getUsername());
        }
        return false;
    }

    public HashMap<Integer, String> getHashMapTypeChanges() {
        HashMap<Integer, String> typeChanges = new HashMap<>();
        List<TypeChangeDB> typeChangeDBS = Arrays.asList(typeChangeModelManager.getAllTypeChanges());
        for (TypeChangeDB typeChangeDB : typeChangeDBS) {
            typeChanges.put(typeChangeDB.getID(), typeChangeDB.getName());
        }
        return typeChanges;
    }

    public HashMap<Integer, String> getHashMapSystems(String valueFilter) {
        String regex = "&&&&&&&&&&&&&&&&&&";
        List<String> nameSystems = new ArrayList<>();
        HashMap<Integer, String> result = new HashMap<>();
        List<System> systems = new ArrayList<>();
        ApplicationUser applicationUser = userService.getCurrentUser();
        User user = userModelManager.getUserByName(applicationUser.getUsername());
        List<String> valuesFilter = Arrays.asList(valueFilter.split(","));
        if (user != null) {
            for (String value : valuesFilter) {
                if (value.equals("1")) {
                    systems = Arrays.asList(systemModelManager.getAllSystems());
                    break;
                }
                switch (value) {
                    case "2": {
                        systems.addAll(getSystemsWhereAssignee(user));
                        break;
                    }
                    case "3": {
                        systems.addAll(getSystemWhereAuthorize(user));
                        break;
                    }
                    case "4": {
                        systems.addAll(getSystemWhereDelivery(user));
                        break;
                    }
                }
            }
            if (nameSystems.size() == 0) {
                for (System system : systems) {
                    nameSystems.add(system.getName());
                }
            }
            for (String nameSystem : nameSystems) {
                System system = systemModelManager.getSystemByName(nameSystem);
                if (result.containsValue(system.getName())) {
                    continue;
                }
                result.put(system.getID(), system.getName().replaceAll(", ", regex));
            }
        }
        return result;
    }

    private List<System> getSystemsWhereAssignee(User user) {
        Stage stageAuthorize = stageModelManager.getStageByName("authorize");
        List<SystemAssignees> systemAssignees = Arrays.asList(SAManager.getSystemAssigneesByUser(user, stageAuthorize));
        return getSystemFromSystemAssignees(systemAssignees);
    }

    private List<System> getSystemWhereAuthorize(User user) {
        Stage stageAuthorize = stageModelManager.getStageByName("authorize");
        List<SystemAssignees> systemAssignees = Arrays.asList(SAManager.getSystemAssigneesByAuthorize(user, stageAuthorize));
        return getSystemFromSystemAssignees(systemAssignees);
    }

    private List<System> getSystemWhereDelivery(User user) {
        List<System> systems = new ArrayList<>();
        List<Delivery> deliverySystems = Arrays.asList(dmManager.getSystemsByDelivery(user));
        deliverySystems.forEach(delivery -> systems.add(delivery.getSystem()));
        return systems;
    }


    private List<System> getSystemFromSystemAssignees(List<SystemAssignees> systemAssignees) {
        List<System> systems = new ArrayList<>();
        for (SystemAssignees systemAssignee : systemAssignees) {
            systems.add(systemAssignee.getSystem());
        }
        return systems;
    }

    public void synchronize() {
        List<ApplicationUser> users = userService.getUsersJira();
        for (ApplicationUser user : users) {
            checkAndChangeStatus(user.getUsername());
        }
    }

    public void checkAndChangeStatus(String nameUser) {
        ApplicationUser user = ComponentAccessor.getUserManager().getUserByName(nameUser);
        User userDB = userModelManager.getUserByName(user.getUsername());
        if (userDB == null) {
            userModelManager.createUser(nameUser);
        } else {
            boolean isActive = user.isActive();
            if (userDB.getActive() != isActive) {
                userModelManager.updateStatusUser(userDB);
            }
        }
    }

    public List<String> addToActiveUsersId(List<String> activeUsers) {
        List<String> result = new ArrayList<>();
        for (String nameUser : activeUsers) {
            //todo 500 ошибка
            Integer idUser = userModelManager.getUserByName(nameUser.replaceAll("=", "")).getID();
            result.add(nameUser + idUser);
        }
        return result;
    }

    public List<String> getNameActiveUsers() {
        List<User> activeUsers = Arrays.asList(userModelManager.getActiveUsers());
        List<String> result = new ArrayList<>();
        activeUsers.forEach(au -> result.add(au.getName() + "="));
        return result;
    }

    public String getUserById(int id) {
        return userModelManager.getUserById(id).getName();
    }

    public String getLogs(Integer idSystem, Integer idTypeChange) {
        String result = "";
        List<Log> logsDate = Arrays.asList(logModelManager.getDate(idSystem, idTypeChange));
        for (Log logDate : logsDate) {
            Date date = logDate.getDate();
            List<Log> logs = Arrays.asList(logModelManager.getLogsByDate(idSystem, idTypeChange, date));
            result += compareStages(logs);
        }
        return result;
    }

    private String compareStages(List<Log> logs) {
        String result = "";
        List<Stage> stages = Arrays.asList(stageModelManager.getAllStages());
        for (Stage stage : stages) {
            java.lang.System.out.println();
            List<Log> logsBefore = logs.stream().filter(l -> l.getTypeChangeAssignee().getName().equals("Delete") && l.getStage().getName().equals(stage.getName())).collect(Collectors.toList());
            List<Log> logsAfter = logs.stream().filter(l -> l.getTypeChangeAssignee().getName().equals("Create") && l.getStage().getName().equals(stage.getName())).collect(Collectors.toList());
            List<User> usersBefore = new ArrayList<>();
            List<User> usersAfter = new ArrayList<>();
            logsBefore.forEach(l -> usersBefore.add(l.getAssignee()));
            logsAfter.forEach(l -> usersAfter.add(l.getAssignee()));
            List<User> usersBeforeBuf = new ArrayList<>(usersBefore);
            List<User> usersAfterBuf = new ArrayList<>(usersAfter);
            usersBeforeBuf.removeAll(usersAfterBuf);
            usersAfterBuf.removeAll(usersBefore);
            if (usersBeforeBuf.size() != 0 || usersAfterBuf.size() != 0) {
                result += "<tr><td headers=\"when\">" + logs.get(0).getDate().toString() + "</td>";
                result += "<td headers =\"who\">" + logs.get(0).getUser().getName() + "</td>";
                result += "<td headers =\"stage\">" + stage.getName() + "</td>";
                result += "<td headers =\"change\">" + "<s>" + getAssigneeLogs(usersBefore) + "</s><br>" + getAssigneeLogs(usersAfter) + "</td>";
                result += "</tr>";
            }
        }
        return result;
    }


    private String getAssigneeLogs(List<User> users) {
        String result = "";
        for (User user : users) {
            result += user.getName() + " ";
        }
        return result;
    }


}
