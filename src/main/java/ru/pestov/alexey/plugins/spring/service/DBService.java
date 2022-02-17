package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.dbmanager.UserModelManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class DBService {
    private final UserModelManager userModelManager;
    private final UserService userService;

    @Inject
    public DBService(UserModelManager userModelManager, UserService userService) {
        this.userModelManager = userModelManager;
        this.userService = userService;
    }

    public Integer prepareDB() {
        prepareUser();
        return 1;
    }

    private void prepareUser()  {
        List<String> users = userService.getActiveUsers();
        for (String user : users)   {
            userModelManager.createUser(user.split("=")[0]);
        }
    }
}
