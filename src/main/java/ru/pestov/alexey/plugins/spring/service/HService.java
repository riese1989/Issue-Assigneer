package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.exception.PermissionException;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
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
    private static Integer count = 0;

    @Inject
    public HService(final JSONService jsonService) {
        this.jsonService = jsonService;
        this.jsonObject = jsonService.getJsonObject();
    }

    public Integer createUsers()   {
        Iterator<String> systems = jsonObject.keySet().iterator();
//todo добавить создание
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
        System.out.println(1);
        if (userManager.getUserByName(assignee) == null) {
            String name = assignee;
            String password = "123";
            String emailAddress = assignee + "@x5.ru";
            String displayName = assignee;
            boolean sendNotification = false;

//            HttpClient httpclient = HttpClients.createDefault();
//            HttpPost httppost = new HttpPost("http://localhost:2990/jira/rest/api/2/user");
//
//// Request parameters and other properties.
//            List<NameValuePair> params = new ArrayList<NameValuePair>(4);
//            params.add(new BasicNameValuePair("name", name));
//            params.add(new BasicNameValuePair("password", password));
//            params.add(new BasicNameValuePair("emailAddress", emailAddress));
//            params.add(new BasicNameValuePair("displayName", displayName));
//            try {
//                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//
////Execute and get the response.
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity entity = response.getEntity();
//
//            if (entity != null) {
//                try (InputStream instream = entity.getContent()) {
//                    System.out.println("Create " + assignee);
//                    count++;
//                }
//            }
//            } catch (IOException e) {
//                e.printStackTrace();
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
    }

    }

