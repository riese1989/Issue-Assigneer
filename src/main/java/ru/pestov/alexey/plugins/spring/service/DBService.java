package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.dbmanager.*;
import ru.pestov.alexey.plugins.spring.enums.Mode;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
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
    private final DMManager DMManager;

    @Inject
    public DBService(UMManager UMManager, UserService userService, StMManager stageModelManager,
                     SystemService systemService, SMManager SMManager, TypeChangeService typeChangeService,
                     TCMManager typeChangeModelManager, JSONService jsonService, SAManager SAManager,
                     DMManager DMManager) {
        this.userModelManager = UMManager;
        this.userService = userService;
        this.stageModelManager = stageModelManager;
        this.systemService = systemService;
        this.systemModelManager = SMManager;
        this.typeChangeService = typeChangeService;
        this.typeChangeModelManager = typeChangeModelManager;
        this.jsonService = jsonService;
        this.SAManager = SAManager;
        this.DMManager = DMManager;
    }

    public Integer recoverDB() {
        jsonService.createJSONObject(Mode.FILE);
        recoverUser();
        recoverStage();
        recoverSystems();
        recoverTypeChanges();
        recoverSystemAssignees();
        recoverDelivery();
        return 1;
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
                TypeChangeDB[] typeChangesDB = typeChangeModelManager.getAllTypeChanges();
                for (TypeChangeDB typeChangeDB : typeChangesDB) {
                    Stage[] stages = stageModelManager.getAllStages();
                    for (Stage stage : stages) {
                        JSONObject stagesJSON = (JSONObject) systemJSON.get(typeChangeDB.getName());
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
    }

    private List<User> getAssignees(String key, JSONObject jsonObject) {
        try {
            List<User> users = new ArrayList<>();
            JSONArray assigneesJSON = (JSONArray) jsonObject.get(key);
            if (assigneesJSON == null) {
                return null;
            }
            for (int i = 0; i < assigneesJSON.size(); i++) {
                User user = userModelManager.getUserByName((String) assigneesJSON.get(i));
                users.add(user);
            }
            return users;
        }
        catch (Exception ex)    {
            ex.printStackTrace();
            return null;
        }

    }

    private void recoverDelivery()  {
        String[] nameSystems = systemService.getSystems().split(",,,,,");
        int countUsers = userModelManager.getAllUsers().length;
        List<String> deliveries = new ArrayList<>();
        for (int i = 0; i < nameSystems.length; i++)    {
            Random random = new Random();
            int idDelivery = random.ints(1, countUsers).findFirst().getAsInt();
            User delivery = userModelManager.getUserById(idDelivery);
            deliveries.add(delivery.getName());
            System system = systemModelManager.getSystemByName(nameSystems[i]);
            DMManager.createDelivery(system, delivery);

        }
        jsonService.createJsonDelivery(deliveries);

        //создаём лист delivery
        //отправляем это дело в json
        //заполлняем бд
    }

}
