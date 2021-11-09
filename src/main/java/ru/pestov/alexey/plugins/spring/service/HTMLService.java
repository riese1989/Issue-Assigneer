package ru.pestov.alexey.plugins.spring.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.pestov.alexey.plugins.spring.comparators.SystemComparator;
import ru.pestov.alexey.plugins.spring.entity.Stage;
import ru.pestov.alexey.plugins.spring.entity.SystemCAB;
import ru.pestov.alexey.plugins.spring.entity.TypeChange;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class HTMLService {
    private static String pathJSON = setPathJSON();
    private static List<SystemCAB> systems = new ArrayList<>();

    public static String getHTMLCode()  {
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

    private static String setPathJSON()   {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("C:\\Users\\alexey.pestov\\Desktop\\Issue-Assigneer\\src\\main\\resources\\issue-assigneer.properties");
            property.load(fis);
            System.out.println(property.getProperty("file.cab.path"));
           // return property.getProperty("file.cab.path");
            return "C:\\Users\\alexey.pestov\\Desktop\\Issue-Assigneer\\src\\main\\resources\\supportFiles\\CAB_approval.json";
        }
        catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
            return new String();
        }
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

    private static String convertJSONtoHTML(JSONObject jsonObject)  {
        String result = "";
        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String systemName = iterator.next();
            SystemCAB systemCAB = new SystemCAB(systemName.replaceAll("& ","").replaceAll("&",""));
            JSONObject systemJSON = (JSONObject) jsonObject.get(systemName);
            Set<String> typeChanges = systemJSON.keySet();
            Iterator<String> iteratorTypeChanges = typeChanges.iterator();
            while (iteratorTypeChanges.hasNext()) {
                String typeChangeName = iteratorTypeChanges.next();
                if (typeChangeName.equals("system_active")) {
                    continue;
                }
                systemName = systemName.replaceAll("& ","").replaceAll("&","");
                result += "<tr>\n<td>" + systemName + "</td>\n";
                result += "<td>" + typeChangeName + "</td>\n";
                JSONObject stages = (JSONObject) systemJSON.get(typeChangeName);
                String assigneesStage1 = getAssignees("stage1", stages);
                String assigneesStage21 = getAssignees("stage21", stages);
                String assigneesStage22 = getAssignees("stage22", stages);
                String assigneesStage23 = getAssignees("stage23", stages);
                String assigneesStage3 = getAssignees("stage3", stages);
                String authorize = getAssignees("authorize", stages);
                result += assigneesStage1;
                result += assigneesStage21;
                result += assigneesStage22;
                result += assigneesStage23;
                result += assigneesStage3;
                result += authorize;
                result += "</tr>\n";
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
        Collections.sort(systems,new SystemComparator());
        return getStringForWrite();
    }

    private static String getStringForWrite()   {
        String result = "";
        for (SystemCAB systemCAB : systems) {
            List<TypeChange> typeChanges = systemCAB.getTypeChanges();
            for (TypeChange typeChange : typeChanges) {
                result += "<tr><td>" + systemCAB.getName() + "</td>";
                result += "<td>" + typeChange.getName() + "</td>";
                List<Stage> stages = typeChange.getStages();
                for (Stage stage : stages)  {
                    String assignees = stage.getAssignees();
                    if (assignees.equals(""))   {
                        result += "<td></td>";
                    }
                    else {
                        result += "<td>" + stage.getAssignees() + "</td>";
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
