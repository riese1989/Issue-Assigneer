package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.user.UserService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.exception.CreateException;
import com.atlassian.jira.exception.PermissionException;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import lombok.Data;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.pestov.alexey.plugins.spring.configuration.Property;
import ru.pestov.alexey.plugins.spring.entity.Param;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.util.*;

@Data
@Named
@ExportAsService
public class JSONService {

    private final JSONObject jsonObject;
    private final LogService logService;
    private final String pathJson;
    private final StringService stringService;
    private final Property property;
    private static int i = 0;

    @Inject
    public JSONService(StringService stringService, LogService logService,
                       Property property) {
        this.property = property;
        this.stringService = stringService;
        this.logService = logService;
        pathJson = getPathJSON();
        jsonObject = getJSONObjectFromFile();
    }

    private String getPathJSON() {
        return property.getProperty("file.cab.path");
    }

    private JSONObject getJSONObjectFromFile() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new InputStreamReader(new FileInputStream(pathJson)));
            return (JSONObject) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }


    public void updateJsonObject(Param param) {
        JSONObject jsonSystem = (JSONObject) jsonObject.get(param.getSystem());
        jsonSystem.put("system_active", param.getActive().equals("true"));
        JSONObject jsonTypeChange = (JSONObject) jsonSystem.get(param.getTypeChange());
        jsonTypeChange.put("stage1", createJsonArray(param.getStage1()));
        jsonTypeChange.put("stage21", createJsonArray(param.getStage21()));
        jsonTypeChange.put("stage22", createJsonArray(param.getStage22()));
        jsonTypeChange.put("stage23", createJsonArray(param.getStage23()));
        jsonTypeChange.put("stage3", createJsonArray(param.getStage3()));
        jsonTypeChange.put("authorize", createJsonArray(param.getAuthorize()));
        jsonSystem.put(param.getTypeChange(), jsonTypeChange);
        writeToFile(jsonObject, pathJson);
        logService.log("dsfafdf");
    }

    private void writeToFile(JSONObject jsonObject, String path) {
        try {
            FileWriter file = new FileWriter(path);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray createJsonArray(List<String> assigneesList) {
        JSONArray jsonArray = new JSONArray();
        for (String assignee : assigneesList) {
            jsonArray.add(assignee.replace("@x5.ru", ""));
        }
        return jsonArray;
    }

    public JSONObject createJsonDelivery(List<String> deliveries)  {
        Set<String> nameSystems = jsonObject.keySet();
        String pathDeliveryFile = property.getProperty("file.delivery.path");
        JSONObject deliveryJSON = new JSONObject();
        int id = 0;
        for (String nameSystem : nameSystems) {
            deliveryJSON.put(nameSystem, deliveries.get(id));
            id++;
        }
        File file = new File(pathDeliveryFile);
        if (!file.exists())   {
            try {
                file.createNewFile();
            }
            catch (IOException ex)  {
                ex.printStackTrace();
            }
        }
        writeToFile(deliveryJSON, pathDeliveryFile);
        return deliveryJSON;
    }

}
