package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.exception.PermissionException;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.dbmanager.UserModelManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Data
@Named
@ExportAsService
public class HService {

    private final JSONService jsonService;
    private final JSONObject jsonObject;
    private final UserModelManager userModelManagerModel;
    private static Integer count = 0;

    @Inject
    public HService(final JSONService jsonService, final UserModelManager userModelManagerModel) {
        this.jsonService = jsonService;
        this.jsonObject = jsonService.getJsonObject();
        this.userModelManagerModel = userModelManagerModel;
    }

    public Integer createUsers()   {
        Iterator<String> systems = jsonObject.keySet().iterator();
        while(systems.hasNext()) {
            String system = systems.next();
            JSONObject jsonObject1 = (JSONObject) jsonObject.get(system);
            List<String> typeChanges = new ArrayList<>(Arrays.asList("Bugfix", "Фоновое задание", "Предсогласованное изменение", "Активация настроек", "Дефект", "Внерелиз", "Изменение настроек", "Массовая выгрузка", "Спринт", "Релиз"));
            List<String> stages = new ArrayList<>(Arrays.asList("stage22", "stage3", "stage23", "stage21", "authorize", "stage1"));
            for (String typeChange : typeChanges)   {
                JSONObject change = (JSONObject) jsonObject1.get(typeChange);
                for (String stage : stages) {
                    try {
                        JSONArray jsonArray = (JSONArray) change.get(stage);
                        for (int i = 0; i < jsonArray.size(); i++)  {
                            createUsers((String) jsonArray.get(i));
                            count++;
                        }
                    }
                    catch (Exception ex)    {
                        continue;
                    }
                }

            }
        }
        return count;
    }


    private void createUsers(String assignee) {
        UserManager userManager = ComponentAccessor.getUserManager();
        ApplicationUser user = userManager.getUserByName(assignee);
        if (user == null) {
            String name = assignee;
            String password = "123";
            String emailAddress = assignee + "@x5.ru";
            String displayName = assignee;
            boolean sendNotification = false;
            ApplicationUser logUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            com.atlassian.jira.bc.user.UserService userServiceAtl = ComponentAccessor.getComponent(com.atlassian.jira.bc.user.UserService.class);
            try {
                com.atlassian.jira.bc.user.UserService.CreateUserValidationResult validationResult = userServiceAtl.validateCreateUser(com.atlassian.jira.bc.user.UserService.CreateUserRequest.withUserDetails(logUser, name, password, emailAddress, displayName).sendNotification(sendNotification));
                assert validationResult.isValid() : validationResult.getErrorCollection();
                userServiceAtl.createUser(validationResult);
                count++;
                System.out.println("Create " + assignee);
            } catch (PermissionException | CreateException e) {
                System.out.println("adsgddFFFGFFGGG");
                e.printStackTrace();
            }
        }
        //todo подумать как в бд закидывать пользаков
    }

    }

