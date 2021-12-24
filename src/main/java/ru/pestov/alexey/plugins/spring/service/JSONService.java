package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.inject.Named;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@Data
@Named
@ExportAsService
public class JSONService {
    private JSONObject jsonObject = getJSONObjectFromFile();

    private String getPathJSON() {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("/Users/usser/issue-assigneer/src/main/resources/issue-assigneer.properties");
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
            Object obj = parser.parse(new InputStreamReader(new FileInputStream(getPathJSON())));
            return (JSONObject) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }

    public List<String> getSystems()    {
        List<String> systems = new ArrayList<>();
        Iterator<String> keys = jsonObject.keySet().iterator();

        while(keys.hasNext()) {
            String key = keys.next();
            systems.add(key);
        }
        return systems;
    }

    public JSONObject getSystem(String nameSystem)   {
        return  (JSONObject) jsonObject.get(nameSystem.replaceAll("_", " "));
    }
}
