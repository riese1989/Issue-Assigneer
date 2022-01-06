package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import lombok.Data;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.pestov.alexey.plugins.spring.entity.Param;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Data
@Named
@ExportAsService
public class JSONService {
    private final JSONObject jsonObject;
    private final String pathJson;
    private final StringService stringService;

    @Inject
    public JSONService(StringService stringService) {
        pathJson = getPathJSON();
        jsonObject = getJSONObjectFromFile();
        this.stringService = stringService;
    }

//    private JSONObject numeratedSystems()   {
//        Integer id = 1;
//        JSONObject jsonObject = getJSONObjectFromFile();
//        Iterator<String> keys = jsonObject.keySet().iterator();
//        while(keys.hasNext()) {
//            String key = keys.next();
//            JSONObject jsonSystem = (JSONObject) jsonObject.get(key);
//            jsonSystem.put("id", id);
//            jsonObject.put(key, jsonSystem);
//            id++;
//        }
//        return jsonObject;
//    }

    private String getPathJSON() {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("/Users/alexey.pestov/Desktop/Issue-Assigneer/src/main/resources/issue-assigneer.properties");
            property.load(fis);
            System.out.println(property.getProperty("file.cab.path"));
            return property.getProperty("file.cab.path");
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
            return new String();
        }
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
        jsonSystem.put("system_active", Boolean.valueOf(param.getActive()));
        JSONObject jsonTypeChange = (JSONObject) jsonSystem.get(param.getTypeChange());
        jsonTypeChange.put("stage1", createJsonArray(param.getStep1()));
        jsonTypeChange.put("stage21", createJsonArray(param.getStep21()));
        jsonTypeChange.put("stage22", createJsonArray(param.getStep22()));
        jsonTypeChange.put("stage23", createJsonArray(param.getStep23()));
        jsonTypeChange.put("stage3", createJsonArray(param.getStep3()));
        jsonTypeChange.put("authorize", createJsonArray(param.getAutorize()));
        jsonSystem.put(param.getTypeChange(), jsonTypeChange);
        writeToFile();
    }

    private void writeToFile() {
        try {
            FileWriter file = new FileWriter(pathJson);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray createJsonArray(String assigneesString) {
        String regex = ";";
        JSONArray jsonArray = new JSONArray();
        List<String> assignees = Arrays.asList(assigneesString.split(regex));
        jsonArray.addAll(assignees);
        return jsonArray;
    }
}
