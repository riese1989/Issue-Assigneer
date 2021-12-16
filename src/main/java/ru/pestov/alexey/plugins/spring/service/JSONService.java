package ru.pestov.alexey.plugins.spring.service;

import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Data
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

    public String countSystems(){
        JSONObject jsonObject = getJSONObjectFromFile();
        return jsonObject.toString();
    }
}
