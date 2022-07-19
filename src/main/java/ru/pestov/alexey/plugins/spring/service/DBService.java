package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.dbmanager.*;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.enums.Mode;
import ru.pestov.alexey.plugins.spring.enums.TypeEmailNotifications;
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
    private final SystemAssigneeService systemAssigneeService;
    private final LogDeliveryManager logDeliveryManager;
    private final DeliveryService deliveryService;
    private final LogActiveSystemManager logActiveSystemManager;
    private final String markInactiveUser = "[X]";
    private final EmailService emailService;
    private Param paramForLastLog = null;
    JSONObject jsonResponseLastLog = null;

    @Inject
    public DBService(UMManager UMManager, UserService userService, StMManager stageModelManager,
                     SystemService systemService, SMManager SMManager, TypeChangeService typeChangeService,
                     TCMManager typeChangeModelManager, JSONService jsonService, SAManager SAManager,
                     DMManager dmManager, TCAManager tcaManager, LogModelManager logModelManager,
                     SystemAssigneeService systemAssigneeService, LogDeliveryManager logDeliveryManager, DeliveryService deliveryService, LogActiveSystemManager logActiveSystemManager, EmailService emailService) {
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
        this.systemAssigneeService = systemAssigneeService;
        this.logDeliveryManager = logDeliveryManager;
        this.deliveryService = deliveryService;
        this.logActiveSystemManager = logActiveSystemManager;
        this.emailService = emailService;
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
        logDeliveryManager.deleteAll();
        logActiveSystemManager.deleteAll();
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

    private void recoverTypeChangeAssignee() {
        tcaManager.create("Create");
        tcaManager.create("Delete");
    }

    private void recoverStage() {
        stageModelManager.createStage("stage1", "1 этап", "1 Этап согласования\n" +
                "функционального лидера или Начальника отдела функциональной области или Техническим владельцем\n" +
                "\n" +
                "Роль определяется исходя из орг.структуры и требований процесса согласований конкретной области");
        stageModelManager.createStage("stage21", "Менеджер изменений", "2 Этап согласования: менеджер изменений");
        stageModelManager.createStage("stage22", "2 этап", "2 Этап согласования: (м.б. определены заранее или назначены при необходимости дополнительного контроля менеджером изменений)");
        stageModelManager.createStage("stage23", "Поддержка", "2 Этап согласования: Поддержка");
        stageModelManager.createStage("stage3", "Утверждающий", "");
        stageModelManager.createStage("authorize", "Авторизующий", "");
        stageModelManager.createStage("delivery", "Деливери", "");
        stageModelManager.createStage("active", "Система активна?", "");
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
        for (Map.Entry<String, String> entry : typeChanges.entrySet()) {
            String name = entry.getKey();
            String translit = entry.getValue();
            typeChangeModelManager.createTypeChange(name, translit);
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

    private User getCurrentUser()   {
        return userModelManager.getUserByName(ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser().getName());
    }

    public List<SystemAssignees> updateDB(Param param) {
        Date date = new Date();
        List<SystemAssignees> result = new ArrayList<>();
        Integer idSystem = param.getSystemId();
        System system = systemModelManager.getSystemById(idSystem);
        Integer idTypeChange = param.getTypeChangeId();
        TypeChangeDB typeChangeDB = typeChangeModelManager.getTypeChangeById(idTypeChange);
        Boolean isActive = param.getActive();
        List<Stage> stages = Arrays.asList(stageModelManager.getAllStages());
        User currentUser = getCurrentUser();
        List<SystemAssignees> oldSystemAssignees = Arrays.asList(SAManager.getAssignees(idSystem, idTypeChange));
        if (isActive != system.getActive()) {
            logActiveSystemManager.create(date, system, isActive, currentUser);
        }
        system = systemModelManager.setActive(idSystem, isActive);
        List<SystemAssignees> newSystemAssignees = new ArrayList<>();
        for (Stage stage : stages) {
            if (stage.getName().equals("delivery") || stage.getName().equals("active")) {
                continue;
            }
            List<String> nameUsers = param.getRequiredStage(stage.getName());
            for (String nameUser : nameUsers) {
                User user = userModelManager.getUserByName(nameUser);
                if (user != null) {
                    SystemAssignees systemAssigneeNew = SAManager.createSystemAssignee(system, typeChangeDB, stage, user);
                    newSystemAssignees.add(systemAssigneeNew);
                    result.add(systemAssigneeNew);
                }
            }
        }
        if (!oldSystemAssignees.equals(newSystemAssignees)) {
            updateLog(oldSystemAssignees, newSystemAssignees, stages, currentUser, system, typeChangeDB, date);
        }
        if (param.getDelivery() != null) {
            updateDeliveryDB(date, param, currentUser);
        }
        SAManager.deleteObjects(oldSystemAssignees.toArray(new SystemAssignees[0]));
        return result;
    }

    private void updateLog(List<SystemAssignees> oldSystemAssignees, List<SystemAssignees> newSystemAssignees,
                           List<Stage> stages, User currentUser,
                           System system, TypeChangeDB typeChangeDB, Date date) {
        for (Stage stage : stages) {
            List<SystemAssignees> oldSystemAssigneesStage = oldSystemAssignees.stream()
                    .filter(osa -> osa.getStage().getName().equals(stage.getName())).collect(Collectors.toList());
            List<SystemAssignees> newSystemAssigneesStage = newSystemAssignees.stream()
                    .filter(nsa -> nsa.getStage().getName().equals(stage.getName())).collect(Collectors.toList());
            List<User> oldUsers = systemAssigneeService.getUsers(oldSystemAssigneesStage);
            List<User> newUsers = systemAssigneeService.getUsers(newSystemAssigneesStage);
            if (!userService.compareLists(oldUsers, newUsers)) {
                if (oldSystemAssigneesStage.size() != 0) {
                    logModelManager.create(date, oldSystemAssigneesStage, tcaManager.getByName("Delete"), currentUser);
                }
                if (newSystemAssigneesStage.size() != 0) {
                    logModelManager.create(date, newSystemAssigneesStage, tcaManager.getByName("Create"), currentUser);
                }
            }
            String nameSystem = system.getName();
            String nameTypeChange = typeChangeDB.getName();
            List<String> nameAddedUsers = userService.removeUsers(newUsers, oldUsers);
            nameAddedUsers.forEach(u -> emailService.sendNotification(u + "@x5.ru", TypeEmailNotifications.ADDED,
                    nameSystem, nameTypeChange, stage.getLabel()));
            List<String> nameDeletedUsers = userService.removeUsers(oldUsers, newUsers);
           nameDeletedUsers.forEach (u -> emailService.sendNotification(u + "@x5.ru", TypeEmailNotifications.DELETE,
                    nameSystem, nameTypeChange, stage.getLabel()));
        }
    }

    private void updateDeliveryDB(Date date, Param param, User currentUser) {
        System system = systemModelManager.getSystemById(param.getSystemId());
        Delivery oldDelivery = dmManager.getDelivery(system);
        User oldDeliveryUser = oldDelivery == null ? null : oldDelivery.getUser();
        User newDeliveryUser = userModelManager.getUserByName(param.getDelivery());
        if (!userService.compare(oldDeliveryUser, newDeliveryUser)) {
            if (oldDeliveryUser == null) {
                dmManager.createDelivery(system, newDeliveryUser);
            } else {
                if (newDeliveryUser == null) {
                    dmManager.delete(oldDelivery);
                } else {
                    dmManager.updateDelivery(oldDelivery, newDeliveryUser);
                }
            }
            logDeliveryManager.create(date, system, oldDeliveryUser, newDeliveryUser, currentUser);
            if (oldDeliveryUser != null)    {
                emailService.sendNotification(oldDeliveryUser.getName() + "@x5.ru", TypeEmailNotifications.DELETE_DELIVERY,
                        system.getName(), null, null);
            }
            if (newDeliveryUser != null)    {
                emailService.sendNotification(newDeliveryUser.getName() + "@x5.ru", TypeEmailNotifications.ADDED_DELIVERY,
                        system.getName(), null, null);
            }
        }
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
        String[] valuesFilter = valueFilter.split(",");
        if (user != null) {
            //todo заменить 1 2 3 4  на что-то осмысленное
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
                        systems.addAll(getNameSystemWhereDelivery(user));
                        break;
                    }
                }
            }
            for (System system : systems) {
                nameSystems.add(system.getName());
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

    private List<System> getNameSystemWhereDelivery(User user) {
        List<System> systems = new ArrayList<>();
        List<Delivery> deliverySystems = Arrays.asList(dmManager.getSystemsByDelivery(user));
        deliverySystems.forEach(delivery -> systems.add(delivery.getSystem()));
        return systems;
    }

    public  List<SystemAssignees> getListSystemsUserDelivery()  {
        List<SystemAssignees> result = new ArrayList<>();
        User currentUser = getCurrentUser();
        List<Delivery> deliveries = Arrays.asList(dmManager.getSystemsByDelivery(currentUser));
        for (Delivery delivery : deliveries)    {
            System system = delivery.getSystem();
            result.addAll(Arrays.asList(system.getSystemAssignees()));
        }
        return result;
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
            Integer idUser = userModelManager.getUserByName(nameUser.replaceAll("=", "")).getID();
            result.add(nameUser + idUser);
        }
        return result;
    }

    public String getUsers() {
        String resultString = "[";
        StringBuffer resultStringBuffer = new StringBuffer(resultString);
        List<User> users = Arrays.asList(userModelManager.getAllUsers());
        users.forEach(u -> {
            resultStringBuffer.append(u.getName());
            if (!u.getActive())    {
                resultStringBuffer.append(markInactiveUser);
            }
            resultStringBuffer.append("=").append(u.getID()).append(", ");
            //resultStringBuffer.append(au.getName()).append("=").append(au.getID()).append(", ");
        });
        resultString = resultStringBuffer.toString();
        return resultString;
    }

    public List<String> getUsers(String pattern) {
        List<User> activeUsers = Arrays.asList(userModelManager.getActiveUsers(pattern));
        List<String> result = new ArrayList<>();
        activeUsers.forEach(au -> result.add(au.getName() + "="));
        return result;
    }

    public String getUserById(int id) {
        User user = userModelManager.getUserById(id);
        return user.getName() + "=" + user.getID() + "=" + user.getActive();
    }

    public String getInactiveUsersById(String ids) {
        String regex = ", ";
        String[] idsList = ids.split(regex);
        StringBuilder result = new StringBuilder();
        if (ids != "") {
            for (String idString : idsList) {
                Integer id = Integer.valueOf(idString);
                User user = userModelManager.getUserById(id);
                if (user.getActive()) {
                    continue;
                }
                result.append(user.getName()).append("[X]").append("=").append(user.getID()).append(regex);
            }
        }
        return result.toString();
    }

    public String getLogs(Integer idSystem, Integer idTypeChange) {
        List<String> stringLogs = new ArrayList<>();
        stringLogs.addAll(getFromAssigneeLogs(idSystem, idTypeChange));
        stringLogs.addAll(getLogsFromDelivery(idSystem));
        stringLogs.addAll(getLogsActiveSystem(idSystem));
        stringLogs.sort(Collections.reverseOrder());
        if (stringLogs.size() == 0) {
            return "";
        }
        return stringLogs.toString().replaceAll("[|]", "");
    }

    private List<String> getFromAssigneeLogs(Integer idSystem, Integer idTypeChange) {
        TypeChangeAssignee createTCA = tcaManager.getByName("Create");
        TypeChangeAssignee deleteTCA = tcaManager.getByName("Delete");
        TypeChangeDB typeChange = typeChangeModelManager.getTypeChangeById(idTypeChange);
        String nameTypeChange = typeChange.getName();
        List<String> result = new ArrayList<>();
        List<Log> logs = Arrays.asList(logModelManager.getLogs(idSystem, idTypeChange));
        List<Date> dates = new ArrayList<>();
        for (Log log : logs) {
            Date dateLog = log.getDate();
            if (!dates.contains(dateLog)) {
                dates.add(dateLog);
            }
        }
        for (Date date : dates) {
            String when = date.toString();
            List<Log> logsByDateDelete = Arrays.asList(logModelManager.getLogData(idSystem, idTypeChange, date, deleteTCA.getID()));
            List<Log> logsByDateCreate = Arrays.asList(logModelManager.getLogData(idSystem, idTypeChange, date, createTCA.getID()));
            List<Log> allLogsByDate = new ArrayList<>();
            allLogsByDate.addAll(logsByDateCreate);
            allLogsByDate.addAll(logsByDateDelete);
            String who = logsByDateDelete.size() == 0 ? logsByDateCreate.get(0).getUser().getName() : logsByDateDelete.get(0).getUser().getName();
            List<String> nameStages = getUniqueStages(allLogsByDate);
            for (String nameStage : nameStages) {
                String stageLabel = stageModelManager.getStageByName(nameStage).getLabel();
                List<Log> logsByDateDeleteStage = logsByDateDelete.stream().filter(l -> l.getStage().getName().equals(nameStage)).collect(Collectors.toList());
                List<Log> logsByDateCreateStage = logsByDateCreate.stream().filter(l -> l.getStage().getName().equals(nameStage)).collect(Collectors.toList());
                String change = "<s>" + convertLogsToString(logsByDateDeleteStage) + "</s> " + convertLogsToString(logsByDateCreateStage);
                result.add(getHTMLFromPattern(when, who, nameTypeChange, stageLabel, change));
            }
        }
        return result;
    }

    private List<String> getUniqueStages(List<Log> logs) {
        List<String> nameStages = new ArrayList<>();
        for (Log log : logs) {
            if (!nameStages.contains(log.getStage().getName())) {
                nameStages.add(log.getStage().getName());
            }
        }
        return nameStages;
    }

    private List<String> getLogsFromDelivery(Integer idSystem) {
        List<LogDelivery> logs = Arrays.asList(logDeliveryManager.getLogs(idSystem));
        List<String> result = new ArrayList<>();
        for (LogDelivery log : logs) {
            String when = log.getDate().toString();
            String who = log.getUser().getName();
            String stage = stageModelManager.getStageByName("delivery").getLabel();
            User oldDelivery = log.getOldDelivery();
            String nameOldDelivery = oldDelivery == null ? "" : oldDelivery.getName();
            User newDelivery = log.getNewDelivery();
            String nameNewDelivery = newDelivery == null ? "" : newDelivery.getName();
            String change = "<s>" + nameOldDelivery + "</s> " + nameNewDelivery;
            result.add(getHTMLFromPattern(when, who, "-", stage, change));
        }
        return result;
    }

    private List<String> getLogsActiveSystem(Integer idSystem) {
        List<LogActiveSystem> logs = Arrays.asList(logActiveSystemManager.getLogs(idSystem));
        List<String> result = new ArrayList<>();
        for (LogActiveSystem log : logs) {
            String when = log.getDate().toString();
            String who = log.getUser().getName();
            String stage = stageModelManager.getStageByName("active").getLabel();
            boolean isActive = log.getNewValue();
            String change = "<s>" + !isActive + "</s> " + isActive;
            result.add(getHTMLFromPattern(when, who, "-", stage, change));
        }
        return result;
    }

    private String convertLogsToString(List<Log> logs) {
        String result = "";
        for (int i = 0; i < logs.size(); i++) {
            result += logs.get(i).getAssignee().getName();
            if (i != logs.size() - 1) {
                result += ", ";
            }
        }
        return result;
    }

    //todo че-то осмысленное arg

    private String getHTMLFromPattern(String when, String who, String typeChangeName, String stageLabel, String change) {
        return "<tr><td>" + when + "</td><td>" + who + "</td><td>" + typeChangeName + "</td><td>" + stageLabel + "</td><td>" + change + "</td></tr>";
    }

    private String compareStages(List<Log> logs) {
        String result = "";
        List<Stage> stages = Arrays.asList(stageModelManager.getAllStages());
        for (Stage stage : stages) {
            if (stage.getName().equals("Delivery") || stage.getName().equals("Active system")) {
                continue;
            }
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

    public String getLabelStage(String name) {
        Stage stage = stageModelManager.getStageByName(name);
        return stage.getLabel();
    }

    public String getTitleStage(String name) {
        Stage stage = stageModelManager.getStageByName(name);
        return stage.getTitle();
    }

    public List<Stage> getAllStages()   {
        return Arrays.asList(stageModelManager.getAllStages());
    }

    public Integer getCountSystemDelivery() {
        return getHashMapSystems("4").size();
    }

    public Boolean checkLastLog(Integer idSystem, Integer idTypeChange)   {
        jsonResponseLastLog = null;
        Boolean result = false;
        List<Date> datesLastLogs = new ArrayList<>();
        List<Log> logs = Arrays.asList(logModelManager.getLogs(idSystem, idTypeChange));
        Comparator<Log> comparatorLog = Comparator.comparing(Log::getDate);
        Date maxDateLogs = logs.size() != 0 ? logs.stream().max(comparatorLog).get().getDate() : null;;
        List<Log> logsMaxDate = logs.stream().filter(l -> l.getDate().equals(maxDateLogs)).collect(Collectors.toList());
        if(logsMaxDate.size() != 0) {
            datesLastLogs.add(maxDateLogs);
        }

        List<LogDelivery> logDeliveries = Arrays.asList(logDeliveryManager.getLogs(idSystem));
        Comparator<LogDelivery> comparatorLogDelivery = Comparator.comparing(LogDelivery::getDate);
        Date maxDateLogsDelivery = logDeliveries.size() != 0 ? logDeliveries.stream().max(comparatorLogDelivery).get().getDate() : null;
        List<LogDelivery> logsDeliveryMaxDate = logDeliveries.stream().filter(l -> l.getDate().equals(maxDateLogsDelivery)).collect(Collectors.toList());
        if(logsDeliveryMaxDate.size() != 0) {
            datesLastLogs.add(maxDateLogsDelivery);
        }

        List<LogActiveSystem> logsActiveSystem = Arrays.asList(logActiveSystemManager.getLogs(idSystem));
        Comparator<LogActiveSystem> comparatorLogActiveSystem = Comparator.comparing(LogActiveSystem::getDate);
        Date maxDateLogsActiveSystem = logsActiveSystem.size() != 0 ? logsActiveSystem.stream().max(comparatorLogActiveSystem).get().getDate(): null;
        List<LogActiveSystem> logsMaxDateActiveSystem = logsActiveSystem.stream().filter(l -> l.getDate().equals(maxDateLogsActiveSystem)).collect(Collectors.toList());
        if(logsMaxDateActiveSystem.size() != 0) {
            datesLastLogs.add(maxDateLogsActiveSystem);
        }
        if (datesLastLogs.size() != 0) {
            Date maxDate = Collections.max(datesLastLogs);

            if (maxDateLogs != null && maxDateLogs.equals(maxDate) && logsMaxDate.get(0).getUser().getID()== getCurrentUser().getID()) {
                List<String> nameStages = Arrays.asList("stage1", "stage21", "stage22", "stage23", "stage3", "authorize");
                for (String nameStage : nameStages) {
                    prepareJSON(nameStage, logsMaxDate);
                }
                result = true;
            }

            if (maxDateLogsDelivery != null && maxDateLogsDelivery.equals(maxDate) && logsDeliveryMaxDate.get(0).getUser().getID()== getCurrentUser().getID()) {
                if (jsonResponseLastLog == null) {
                    jsonResponseLastLog = new JSONObject();
                }
                jsonResponseLastLog.put("delivery", String.valueOf(logsDeliveryMaxDate.get(0).getOldDelivery().getID()));
                result = true;
            }

            if (maxDateLogsActiveSystem != null && maxDateLogsActiveSystem.equals(maxDate) && logsMaxDateActiveSystem.get(0).getUser().getID()== getCurrentUser().getID()) {
                if (jsonResponseLastLog == null) {
                    jsonResponseLastLog = new JSONObject();
                }
                jsonResponseLastLog.put("active", !logsMaxDateActiveSystem.get(0).getNewValue());
                result = true;
            }
        }
        if (!result)    {
            jsonResponseLastLog = null;
        }
        return result;
    }

    public JSONObject getLastLogs(Integer idSystem, Integer idTypeChange)   {
        if (jsonResponseLastLog == null)    {
            checkLastLog(idSystem, idTypeChange);
        }
        return jsonResponseLastLog;
    }

    private List<String> getListAssignee(List<Log> logs)    {
        List<Log> deletedLogs = logs.stream().filter(l -> l.getTypeChangeAssignee().getName().equals("Delete")).collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        for (Log log : deletedLogs)    {
            result.add(String.valueOf(log.getAssignee().getID()));
        }
        return result;
    }

    private void prepareJSON(String nameStage, List<Log> logs) {
        if (jsonResponseLastLog == null) {
            jsonResponseLastLog = new JSONObject();
        }
        List<Log> filterLogs = logs.stream().filter(l -> l.getStage().getName().equals(nameStage)).collect(Collectors.toList());
        if (filterLogs.size() != 0) {
            jsonResponseLastLog.put(nameStage, getListAssignee(filterLogs));
        }
    }

    public JSONArray getSystemsOfUser(Boolean isAdmin, Boolean isDelivery)    {
        JSONArray systemsJSON = new JSONArray();
        if (isAdmin)    {
            List<System> systems = Arrays.asList(systemModelManager.getAllSystems());
            systems.forEach(s -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", s.getID());
                jsonObject.put("name", s.getName());
                jsonObject.put("active", s.getActive());
                systemsJSON.add(jsonObject);
            });

        }
        if (isDelivery && !isAdmin) {
            List<Delivery> systemsDelivery = Arrays.asList(dmManager.getSystemsByDelivery(getCurrentUser()));
            systemsDelivery.forEach(s -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", s.getID());
                jsonObject.put("name", s.getSystem().getName());
                jsonObject.put("active", s.getSystem().getActive());
                systemsJSON.add(jsonObject);
            });
        }
        return  systemsJSON;
    }
}
