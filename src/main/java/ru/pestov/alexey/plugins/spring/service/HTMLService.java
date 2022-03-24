package ru.pestov.alexey.plugins.spring.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.pestov.alexey.plugins.spring.comparators.SystemCABComparator;
import ru.pestov.alexey.plugins.spring.entity.Stage;
import ru.pestov.alexey.plugins.spring.entity.SystemCAB;
import ru.pestov.alexey.plugins.spring.entity.TypeChange;

import java.io.*;
import java.util.*;


public class HTMLService {
    private static String pathJSON = setPathJSON();
    private static List<SystemCAB> systems = new ArrayList<>();

    public static String getHTMLCode() {
        String code = "<table border=\"1\">" +
                "<caption>Назначенцы</caption>" +
                "<tr>" +
                "<th>Система</th>" +
                "<th>Тип изменения</th>" +
                "<th>1-ый этап</th>" +
                "<th>2-ой этап</th>" +
                "<th>3-ий этап</th>" +
                "<th>Авторизующий</th>" +
                "</tr>" +
                convertJSONtoHTML(getJSONObject());
        code += "</table>";
        return code;
    }

    private static String setPathJSON() {
        return new Properties().getProperty("file.cab.path");
    }

    private static JSONObject getJSONObject() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new InputStreamReader(new FileInputStream(pathJSON)));
            return (JSONObject) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }

    private static String convertJSONtoHTML(JSONObject jsonObject) {
        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String systemName = iterator.next();
            SystemCAB systemCAB = new SystemCAB(systemName.replaceAll("& ", "").replaceAll("&", ""));
            JSONObject systemJSON = (JSONObject) jsonObject.get(systemName);
            Set<String> typeChanges = systemJSON.keySet();
            Iterator<String> iteratorTypeChanges = typeChanges.iterator();
            while (iteratorTypeChanges.hasNext()) {
                String typeChangeName = iteratorTypeChanges.next();
                if (typeChangeName.equals("system_active")) {
                    continue;
                }
                systemName = systemName.replaceAll("& ", "").replaceAll("&", "");
                JSONObject stages = (JSONObject) systemJSON.get(typeChangeName);
                String assigneesStage1 = getAssignees("stage1", stages);
                String assigneesStage21 = getAssignees("stage21", stages);
                String assigneesStage22 = getAssignees("stage22", stages);
                String assigneesStage23 = getAssignees("stage23", stages);
                String assigneesStage3 = getAssignees("stage3", stages);
                String authorize = getAssignees("authorize", stages);
                TypeChange typeChange = new TypeChange(typeChangeName);
                typeChange.addStage(new Stage("stage1", assigneesStage1));
                typeChange.addStage(new Stage("stage21", assigneesStage21));
                typeChange.addStage(new Stage("stage22", assigneesStage22));
                typeChange.addStage(new Stage("stage23", assigneesStage23));
                typeChange.addStage(new Stage("stage3", assigneesStage3));
                typeChange.addStage(new Stage("authorize", authorize));
                systemCAB.addType(typeChange);
                StageService.sort(typeChange);
            }
            TypeChangeService.sort(systemCAB);
            systems.add(systemCAB);
        }
        Collections.sort(systems, new SystemCABComparator());
        return getStringForWrite();
    }

    private static String getStringForWrite() {
        String result = "";
        for (SystemCAB systemCAB : systems) {
            List<TypeChange> typeChanges = systemCAB.getTypeChanges();
            for (TypeChange typeChange : typeChanges) {
                result += "<tr><td>" + systemCAB.getName() + "</td>";
                result += "<td>" + typeChange.getName() + "</td>";
                List<Stage> stages = typeChange.getStages();
                for (Stage stage : stages) {
                    if (!stage.getName().equals("stage22") && !stage.getName().equals("stage23"))  {
                        result += "<td>";
                    }
                    String assignees = stage.getAssignees();
                    result += assignees + ", ";
                    if (!stage.getName().equals("stage21") && !stage.getName().equals("stage22"))  {
                        result += "</td>";
                    }
                }
                result += "</tr>";
            }
        }
        return result;
}

    private static String getAssignees(String key, JSONObject jsonObject) {
        String result = "";
        try {
            JSONArray assigneesJSON = (JSONArray) jsonObject.get(key);

            for (int i = 0; i < assigneesJSON.size(); i++) {
                result += assigneesJSON.get(i);
                if (i != assigneesJSON.size() - 1) {
                    result += ",";
                }
            }
        } catch (ClassCastException ex) {
            result += jsonObject.get(key).toString();
        }
        return result;
    }

}