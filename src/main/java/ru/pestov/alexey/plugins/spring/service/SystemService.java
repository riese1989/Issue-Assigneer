package ru.pestov.alexey.plugins.spring.service;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Getter
@Named
public class SystemService {
    private final JSONService jsonService;
    private JSONObject jsonObject;

    @Inject
    public SystemService(JSONService jsonService) {
        this.jsonService = jsonService;
        jsonObject = jsonService.getJsonObject();
    }

    public HashMap<String, Boolean> getMapSystemActive()    {
        HashMap<String, Boolean> mapSystemActive = new HashMap<>();
        Iterator<String> keys = jsonObject.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            JSONObject systemJSON = (JSONObject) jsonObject.get(key);
            mapSystemActive.put(key, (Boolean) systemJSON.get("system_active"));
        }
        return mapSystemActive;
    }

    public String getSystems()    {
        String result = "";
        String regex = ",,,,,";
        List<String> systems = new ArrayList<>();
        Iterator<String> keys = jsonObject.keySet().iterator();
        while(keys.hasNext()) {
            String key = keys.next();
            systems.add(key);
        }
        Collections.sort(systems);
        for (int i = 0; i < systems.size(); i++)    {
            result += systems.get(i);
            if (i != systems.size() - 1)    {
                result += regex;
            }
        }
        return result;
    }

    public String getNameSystemById(String idString)   {
        List<String> systems = Arrays.asList(getSystems().split(",,,,,"));
        Integer id = Integer.parseInt(idString);
        return systems.get(id-1);
    }

    public Boolean isSystemActive(String idString)  {
        String nameSystem = getNameSystemById(idString);
        JSONObject systemJson = getSystem(nameSystem);
        return (Boolean) systemJson.get("system_active");
    }

    public JSONObject getSystem(String nameSystem)   {
        return  (JSONObject) jsonObject.get(nameSystem);
    }

    public String getStageSystem(String nameSystem, String typeChange, String stage)    {
        JSONObject jsonSystem = getSystem(nameSystem);
        JSONObject typeChangeSystem = (JSONObject) jsonSystem.get(typeChange);
        return getAssignees(stage,typeChangeSystem);
    }

    private static String getAssignees(String key, JSONObject jsonObject) {
        String result = "";
        try {
            JSONArray assigneesJSON = (JSONArray) jsonObject.get(key);

            for (int i = 0; i < assigneesJSON.size(); i++) {
                result += assigneesJSON.get(i) + "@x5.ru";
                if (i != assigneesJSON.size() - 1) {
                    result += ", ";
                }
            }
        } catch (ClassCastException ex) {
            result += jsonObject.get(key).toString();
        }
        return result;
    }
}
