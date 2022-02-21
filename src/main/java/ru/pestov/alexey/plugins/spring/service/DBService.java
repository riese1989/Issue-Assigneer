package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.user.ApplicationUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.dbmanager.*;
import ru.pestov.alexey.plugins.spring.model.Stage;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class DBService {
    private final UserModelManager userModelManager;
    private final UserService userService;
    private final StageModelManager stageModelManager;
    private final SystemService systemService;
    private final SystemModelManager systemModelManager;
    private final TypeChangeService typeChangeService;
    private final TypeChangeModelManager typeChangeModelManager;
    private final JSONService jsonService;
    private final SystemAssigneesManager systemAssigneesManager;

    @Inject
    public DBService(UserModelManager userModelManager, UserService userService, StageModelManager stageModelManager,
                     SystemService systemService, SystemModelManager systemModelManager, TypeChangeService typeChangeService,
                     TypeChangeModelManager typeChangeModelManager, JSONService jsonService, SystemAssigneesManager systemAssigneesManager) {
        this.userModelManager = userModelManager;
        this.userService = userService;
        this.stageModelManager = stageModelManager;
        this.systemService = systemService;
        this.systemModelManager = systemModelManager;
        this.typeChangeService = typeChangeService;
        this.typeChangeModelManager = typeChangeModelManager;
        this.jsonService = jsonService;
        this.systemAssigneesManager = systemAssigneesManager;
    }

    public Integer prepareDB() {
        prepareUser();
        prepareStage();
        prepareSystems();
        prepareTypeChanges();
        prepareSystemAssignees();
        return 1;
    }

    private void prepareUser() {
        List<ApplicationUser> users = userService.getUsersJira();
        for (ApplicationUser user : users) {
            userModelManager.createUser(user);
        }
    }

    private void prepareStage() {
        List<String> stages = Arrays.asList("stage1", "stage21", "stage22", "stage23", "stage3");
        for (String stage : stages) {
            stageModelManager.createStage(stage);
        }
    }

    private void prepareSystems() {
        HashMap<String, Boolean> mapSystems = systemService.getMapSystemActive();
        for (Map.Entry<String, Boolean> entry : mapSystems.entrySet()) {
            String name = entry.getKey();
            Boolean active = entry.getValue();
            systemModelManager.createSystem(name, active);
        }
    }

    private void prepareTypeChanges() {
        HashMap<String, String> typeChanges = typeChangeService.getTypeChanges();
        for (String name : typeChanges.values()) {
            typeChangeModelManager.createTypeChange(name);
        }
    }

    private void prepareSystemAssignees() {
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
                                systemAssigneesManager.createSystemAssignee(system, typeChangeDB, stage, assignee);
                                java.lang.System.out.println(1);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<User> getAssignees(String key, JSONObject jsonObject) {
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

}
