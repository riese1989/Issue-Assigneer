package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.user.ApplicationUser;
import com.sun.org.apache.xpath.internal.operations.Bool;
import ru.pestov.alexey.plugins.spring.dbmanager.StageModelManager;
import ru.pestov.alexey.plugins.spring.dbmanager.SystemModelManager;
import ru.pestov.alexey.plugins.spring.dbmanager.TypeChangeModelManager;
import ru.pestov.alexey.plugins.spring.dbmanager.UserModelManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class DBService {
    private final UserModelManager userModelManager;
    private final UserService userService;
    private final StageModelManager stageModelManager;
    private final SystemService systemService;
    private final SystemModelManager systemModelManager;
    private final TypeChangeService typeChangeService;
    private final TypeChangeModelManager typeChangeModelManager;

    @Inject
    public DBService(UserModelManager userModelManager, UserService userService, StageModelManager stageModelManager,
                     SystemService systemService, SystemModelManager systemModelManager, TypeChangeService typeChangeService,
                     TypeChangeModelManager typeChangeModelManager) {
        this.userModelManager = userModelManager;
        this.userService = userService;
        this.stageModelManager = stageModelManager;
        this.systemService = systemService;
        this.systemModelManager = systemModelManager;
        this.typeChangeService = typeChangeService;
        this.typeChangeModelManager = typeChangeModelManager;
    }

    public Integer prepareDB() {
        prepareUser();
        prepareStage();
        prepareSystems();
        prepareTypeChanges();
        return 1;
    }

    private void prepareUser()  {
        List<ApplicationUser> users = userService.getUsersJira();
        for (ApplicationUser user : users)   {
            userModelManager.createUser(user);
        }
    }

    private void prepareStage() {
        List<String> stages = Arrays.asList("stage1", "stage21", "stage22", "stage23", "stage3");
        for (String stage : stages) {
            stageModelManager.createStage(stage);
        }
    }

    private void prepareSystems()   {
        HashMap<String, Boolean> mapSystems = systemService.getMapSystemActive();
        for (Map.Entry<String, Boolean> entry : mapSystems.entrySet())  {
            String name = entry.getKey();
            Boolean active = entry.getValue();
            systemModelManager.createSystem(name,active);
        }
    }

    private void prepareTypeChanges()   {
        HashMap <String, String> typeChanges = typeChangeService.getTypeChanges();
        for (String name : typeChanges.values())    {
            typeChangeModelManager.createTypeChange(name);
        }
    }

}
