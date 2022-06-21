package ru.pestov.alexey.plugins.spring.service;

import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.pestov.alexey.plugins.spring.configuration.Property;
import ru.pestov.alexey.plugins.spring.dbmanager.SAManager;
import ru.pestov.alexey.plugins.spring.dbmanager.SMManager;
import ru.pestov.alexey.plugins.spring.dbmanager.TCMManager;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.enums.Mode;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.util.*;

@Data
@Named
public class JSONService {

    private JSONObject jsonObject;
    private final String pathJson;
    private final String pathDelivery;
    private final Property property;
    private final SMManager systemModelManager;
    private final SAManager SAManager;
    private final JSONObject jsonDelivery;
    private final TCMManager tcmManager;
    private static int i = 0;

    @Inject
    public JSONService(Property property, SMManager systemModelManager, SAManager SAManager, TCMManager tcmManager) {
        this.property = property;
        this.systemModelManager = systemModelManager;
        this.SAManager = SAManager;
        pathJson = property.getProperty("file.cab.path");
        pathDelivery = property.getProperty("file.delivery.path");
        jsonObject = getJSONObjectFromFile(pathJson);
        jsonDelivery = getJSONObjectFromFile(pathDelivery);
        this.tcmManager = tcmManager;
    }

    public JSONObject createJSONObject(Mode mode) {
        if (mode == Mode.DB) {
            jsonObject = getJSONObjectFromDB();
        } else {
            jsonObject = getJSONObjectFromFile(pathJson);
        }
        return jsonObject;
    }

    private JSONObject getJSONObjectFromFile(String pathJson) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new InputStreamReader(new FileInputStream(pathJson)));
            return (JSONObject) obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }

    private JSONObject getJSONObjectFromDB() {
        JSONObject jsonObject = new JSONObject();
        System[] systems = systemModelManager.getAllSystems();
        for (System system : systems) {
            JSONObject systemJSON = new JSONObject();
            SystemAssignees systemAssignees[] = SAManager.getAssigneesSystem(system.getID());
            for (SystemAssignees systemAssignee : systemAssignees) {
                String nameTypeChange = systemAssignee.getTypeChange().getName();
                JSONObject typeChangeJSON;
                if (systemJSON.containsKey(nameTypeChange)) {
                    typeChangeJSON = (JSONObject) systemJSON.get(nameTypeChange);
                } else {
                    typeChangeJSON = new JSONObject();
                }
                String nameStage = systemAssignee.getStage().getName();
                JSONArray stageJSON;
                if (typeChangeJSON.containsKey(nameStage)) {
                    stageJSON = (JSONArray) typeChangeJSON.get(nameStage);
                } else {
                    stageJSON = new JSONArray();
                }
                stageJSON.add(systemAssignee.getUser().getName());
                typeChangeJSON.put(nameStage, stageJSON);
                systemJSON.put(nameTypeChange, typeChangeJSON);
                jsonObject.put(system.getName(), systemJSON);
            }
        }
        return jsonObject;
    }


    public void updateJsonObject(Param param) {
        String nameSystem = systemModelManager.getSystemById(param.getSystemId()).getName();
        String nameTypeChange = tcmManager.getTypeChangeById(param.getTypeChangeId()).getName();
        JSONObject jsonSystem = (JSONObject) jsonObject.get(nameSystem);
        jsonSystem.put("system_active", param.getActive());
        JSONObject jsonTypeChange = (JSONObject) jsonSystem.get(nameTypeChange);
        jsonTypeChange.put("stage1", createJsonArray(param.getStage1()));
        jsonTypeChange.put("stage21", createJsonArray(param.getStage21()));
        jsonTypeChange.put("stage22", createJsonArray(param.getStage22()));
        jsonTypeChange.put("stage23", createJsonArray(param.getStage23()));
        jsonTypeChange.put("stage3", createJsonArray(param.getStage3()));
        jsonTypeChange.put("authorize", createJsonArray(param.getAuthorize()));
        jsonSystem.put(nameTypeChange, jsonTypeChange);
        jsonObject.put(nameSystem, jsonSystem);
        if (param.getDelivery() != null) {
            jsonDelivery.put(nameSystem, param.getDelivery().replaceAll("@x5.ru", ""));
        } else {
            jsonDelivery.remove(nameSystem);
        }
        writeToFile(jsonObject, pathJson);
        writeToFile(jsonDelivery, pathDelivery);
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

    public JSONObject createJsonDelivery(List<String> deliveries) {
        Set<String> nameSystems = jsonObject.keySet();
        String pathDeliveryFile = property.getProperty("file.delivery.path");
        JSONObject deliveryJSON = new JSONObject();
        int id = 0;
        for (String nameSystem : nameSystems) {
            deliveryJSON.put(nameSystem, deliveries.get(id));
            id++;
        }
        File file = new File(pathDeliveryFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        writeToFile(deliveryJSON, pathDeliveryFile);
        return deliveryJSON;
    }

}
