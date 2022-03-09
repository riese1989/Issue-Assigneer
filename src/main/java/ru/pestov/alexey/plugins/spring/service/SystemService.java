package ru.pestov.alexey.plugins.spring.service;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.dbmanager.SAManager;
import ru.pestov.alexey.plugins.spring.dbmanager.SMManager;
import ru.pestov.alexey.plugins.spring.dbmanager.TCMManager;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Getter
@Named
public class SystemService {
    private final JSONService jsonService;
    private final SMManager smManager;
    private final SAManager saManager;
    private JSONObject jsonObject;
    private final TCMManager tcmManager;

    @Inject
    public SystemService(JSONService jsonService, SMManager smManager, SAManager saManager, TCMManager tcmManager) {
        this.jsonService = jsonService;
        jsonObject = jsonService.getJsonObject();
        this.smManager = smManager;
        this.saManager = saManager;
        this.tcmManager = tcmManager;

    }

    public HashMap<String, Boolean> getMapSystemActive() {
        HashMap<String, Boolean> mapSystemActive = new HashMap<>();
        Iterator<String> keys = jsonObject.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject systemJSON = (JSONObject) jsonObject.get(key);
            mapSystemActive.put(key, (Boolean) systemJSON.get("system_active"));
        }
        return mapSystemActive;
    }

    public String getSystems() {
        String result = "";
        String regex = ",,,,,";
        System[] systems = smManager.getAllSystems();
        List<String> nameSystems = new ArrayList<>();
        Arrays.asList(systems).stream().map(System::getName).forEach(nameSystems::add);
        Collections.sort(nameSystems);
        for (int i = 0; i < nameSystems.size(); i++) {
            result += nameSystems.get(i);
            if (i != nameSystems.size() - 1) {
                result += regex;
            }
        }
//        List<String> systems = new ArrayList<>();
//        Iterator<String> keys = jsonObject.keySet().iterator();
//        while(keys.hasNext()) {
//            String key = keys.next();
//            systems.add(key);
//        }
//        Collections.sort(systems);
//        for (int i = 0; i < systems.size(); i++)    {
//            result += systems.get(i);
//            if (i != systems.size() - 1)    {
//                result += regex;
//            }
//        }
        return result;
    }

    public String getNameSystemById(String idString) {
        List<String> systems = Arrays.asList(getSystems().split(",,,,,"));
        Integer id = Integer.parseInt(idString);
        return systems.get(id - 1);
    }

    public Boolean isSystemActive(String idString) {
        String nameSystem = getNameSystemById(idString);
        JSONObject systemJson = getSystem(nameSystem);
        return (Boolean) systemJson.get("system_active");
    }

    public JSONObject getSystem(String nameSystem) {
        return (JSONObject) jsonObject.get(nameSystem);
    }

    //todo
    public String getAssigneesStageSystem(Integer idSystem, Integer idTypeChange, Integer idStage) {
//        JSONObject jsonSystem = getSystem(idSystem);
//        JSONObject typeChangeSystem = (JSONObject) jsonSystem.get(idTypeChange);
//        return getAssignees(idStage, typeChangeSystem);
        SystemAssignees[] systemAssignees = saManager.getAssignees(idSystem, idTypeChange, idStage);
        String result = "";
        for (int i = 0; i < systemAssignees.length; i++)    {
            result += systemAssignees[i].getUser().getName() + "@x5.ru";
            if (i != systemAssignees.length - 1) {
                result += ", ";
            }
        }
        return result;
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
