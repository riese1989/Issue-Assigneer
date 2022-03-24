package ru.pestov.alexey.plugins.spring.service;

import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.dbmanager.SAManager;
import ru.pestov.alexey.plugins.spring.dbmanager.SMManager;
import ru.pestov.alexey.plugins.spring.dbmanager.StMManager;
import ru.pestov.alexey.plugins.spring.dbmanager.TCMManager;
import ru.pestov.alexey.plugins.spring.model.Stage;
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
    private final StMManager stMManager;
    private List<String> nameSystems = new ArrayList<>();

    @Inject
    public SystemService(JSONService jsonService, SMManager smManager, SAManager saManager, TCMManager tcmManager,
                         StMManager stMManager) {
        this.jsonService = jsonService;
        jsonObject = jsonService.getJsonObject();
        this.smManager = smManager;
        this.saManager = saManager;
        this.tcmManager = tcmManager;
        this.stMManager = stMManager;

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
        if (nameSystems.size() == 0) {
            for (System system : systems) {
                nameSystems.add(system.getName());
            }
            Collections.sort(nameSystems);
        }
        for (int i = 0; i < nameSystems.size(); i++) {
            result += nameSystems.get(i);
            if (i != nameSystems.size() - 1) {
                result += regex;
            }
        }
        return result;
    }

    public String getNameSystemById(String idString) {
        List<String> systems = Arrays.asList(getSystems().split(",,,,,"));
        Integer id = Integer.parseInt(idString);
        return systems.get(id - 1);
    }

    public Boolean isSystemActive(Integer idSystem) {
        return smManager.isSystemActive(idSystem);
    }

    public JSONObject getSystem(String nameSystem) {
        return (JSONObject) jsonObject.get(nameSystem);
    }

    public String getAssigneesStageSystem(Integer idSystem, Integer idTypeChange, String nameStage) {
        Stage stage = stMManager.getStageByName(nameStage);
        System systemDB = smManager.getSystemById(idSystem);
        Integer idSystemDB = systemDB.getID();
        SystemAssignees[] systemAssignees = saManager.getAssignees(idSystemDB, idTypeChange, stage.getID());
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
