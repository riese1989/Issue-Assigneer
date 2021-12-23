package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.entity.Stage;
import ru.pestov.alexey.plugins.spring.entity.SystemCAB;
import ru.pestov.alexey.plugins.spring.entity.TypeChange;

@Named
public class SystemService {

  private List<SystemCAB> systems = new ArrayList<>();
  private JSONService jsonService;

  @Inject
  public SystemService(JSONService jsonService) {
    this.jsonService = jsonService;
  }

  public List<SystemCAB> getSystems() {
    JSONObject jsonObject = jsonService.getJsonObject();
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
    return systems;
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

  public SystemCAB getSystem(String name) {
    for (SystemCAB system : systems) {
      if (system.getName().equals(name)) {
        return system;
      }
    }
    return null;
  }

  public void updateSystem (HttpServletRequest request) {
    String nameSystem = request.getParameter("system");
    String nameTypeChange = request.getParameter("typechange");
    String step1 = request.getParameter("step1");
    String step2 = request.getParameter("step2");
    String step3 = request.getParameter("step3");
    SystemCAB systemCAB = getSystem(nameSystem);
    List<TypeChange> typeChanges = systemCAB.getTypeChanges();
    for (TypeChange typeChange : typeChanges) {
      if (typeChange.getName().equals(nameTypeChange))  {
        //todo дописать обновление стадий
        List<Stage> stages = typeChange.getStages();
        for (Stage stage : stages)  {
          if (stage.getName().equals("stage1")) {
            stage.setAssignees(step1);
            continue;
          }
          if (stage.getName().equals("stage21")) {
            stage.setAssignees(step2);
            continue;
          }
          if (stage.getName().equals("stage3")) {
            stage.setAssignees(step3);
            continue;
          }
          else  {
            stage.setAssignees("");
          }
        }
      }
    }
  }

  private JSONObject convertSystemsToJson()
}
