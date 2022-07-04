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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.pestov.alexey.plugins.spring.configuration.Property;
import ru.pestov.alexey.plugins.spring.dbmanager.UMManager;
import ru.pestov.alexey.plugins.spring.enums.Mode;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Data
@Named
@ExportAsService
public class HService {

    private final JSONService jsonService;
    private JSONObject jsonObject;
    private final UMManager userModelManagerModel;
    private final Property property;
    private static Integer count = 0;

    @Inject
    public HService(final JSONService jsonService, final UMManager userModelManagerModel, final Property property) {
        this.jsonService = jsonService;
        this.userModelManagerModel = userModelManagerModel;
        this.property = property;
        jsonObject = jsonService.createJSONObject(Mode.FILE);
    }

    public String checkFiles()  {
        String result = "";
        File delivery = new File(property.getProperty("file.delivery.path"));
        if(delivery == null)    {
            result += "Delivery null " + property.getProperty("file.delivery.path");
        }
        else    {
            result += "Delivery true " + property.getProperty("file.delivery.path");
        }

        File cab = new File(property.getProperty("file.cab.path"));
        if(delivery == null)    {
            result += "Cab null " + property.getProperty("file.cab.path");
        }
        else    {
            result += "Cab true " + property.getProperty("file.cab.path");
        }
        return result;
    }

    public String createUsers() {
        String result = "";
        String forWrite = "";
        try {
            Iterator<String> systems = jsonObject.keySet().iterator();
            while (systems.hasNext()) {
                String system = systems.next();
                JSONObject jsonObject1 = (JSONObject) jsonObject.get(system);
                List<String> typeChanges = new ArrayList<>(Arrays.asList("Bugfix", "Фоновое задание", "Предсогласованное изменение", "Активация настроек", "Дефект", "Внерелиз", "Изменение настроек", "Массовая выгрузка", "Спринт", "Релиз"));
                List<String> stages = new ArrayList<>(Arrays.asList("stage22", "stage3", "stage23", "stage21", "authorize", "stage1"));
                for (String typeChange : typeChanges) {
                    JSONObject change = (JSONObject) jsonObject1.get(typeChange);
                    for (String stage : stages) {
                        try {
                            JSONArray jsonArray = (JSONArray) change.get(stage);
                            for (int i = 0; i < jsonArray.size(); i++) {
                                result += (String) jsonArray.get(i);
                                createUsers(((String) jsonArray.get(i)).split("=")[0]);
                                forWrite += "\"" + jsonArray.get(i) + "\",";
                                count++;
                            }
                        } catch (Exception ex) {
                            continue;
                        }
                    }

                }
            }
        }
        catch (Exception ex)   {
            result += "ERROR";
            ex.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/alexey.pestov/Desktop/Issue-Assigneer/src/main/resources/supportFiles/users.txt"));
            writer.write(forWrite);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
                e.printStackTrace();
            }
        }
    }

    }

