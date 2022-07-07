package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.model.Stage;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
public class FileService {
    public String createFile(List<SystemAssignees> systemAssigneesList, List<Stage> stages) {
        return createDataLines(systemAssigneesList, stages);
    }

    private String createDataLines(List<SystemAssignees> systemAssigneesList, List<Stage> stages) {
        StringBuilder result = new StringBuilder();
        Map<System, List<SystemAssignees>> mapSystemAssigneesGroupSystems = systemAssigneesList.stream()
                .collect(Collectors.groupingBy(SystemAssignees::getSystem)); //группировка по системам
        String[] header = {"Система", "Тип изменения", stages.get(0).getLabel(), stages.get(1).getLabel(),
                stages.get(2).getLabel(), stages.get(3).getLabel(), stages.get(4).getLabel(),
                stages.get(5).getLabel()};
        result.append(getRecord(Arrays.asList(header)));
        for (Map.Entry<System, List<SystemAssignees>> map : mapSystemAssigneesGroupSystems.entrySet())  {
            System system = map.getKey();
            List<SystemAssignees> systemAssigneesListGroupSystems = map.getValue();
            Map<TypeChangeDB, List<SystemAssignees>> mapGroupTypeChange = systemAssigneesListGroupSystems.stream()
                    .collect(Collectors.groupingBy(SystemAssignees::getTypeChange)); //внутри системы группируем по типам изменений
            for (Map.Entry<TypeChangeDB, List<SystemAssignees>> entry : mapGroupTypeChange.entrySet())  {
                List<SystemAssignees> listGroupTypeChange = entry.getValue();
                TypeChangeDB typeChangeDB = entry.getKey();
                List<String> records = new ArrayList<>();
                records.add(system.getName());
                records.add(typeChangeDB.getName());
                for (Stage stage : stages)  {
                    String nameStage = stage.getName();
                    if(nameStage.equals("delivery") || nameStage.equals("active"))  {
                        continue;
                    }
                    StringBuffer recordAssignee = new StringBuffer();
                    List<SystemAssignees> assigneesStage = listGroupTypeChange.stream().filter(s -> s.getStage().getName().equals(stage.getName())).collect(Collectors.toList());
                    assigneesStage.forEach(a -> recordAssignee.append(a.getUser().getName()).append(" "));
                    records.add(recordAssignee.toString());
                }
                result.append(getRecord(records));
            }

        }
        return result.toString();
    }

    private StringBuilder getRecord(List<String> labels) {
        StringBuilder record = new StringBuilder();
        labels.forEach(l -> record.append(l).append(","));
        record.append("\n");
        return record;
    }
}
