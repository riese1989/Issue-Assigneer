package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.model.Stage;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;

import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
public class FileService {
    public String createFile(List<SystemAssignees> systemAssigneesList, List<Stage> stages) {
        String path = "export.csv";
        List<String[]> dataLines = createDataLines(systemAssigneesList, stages);
        write(dataLines, path);
        return path;
    }

    private List<String[]> createDataLines(List<SystemAssignees> systemAssigneesList, List<Stage> stages) {
        List<String[]> result = new ArrayList<>();
        Map<System, List<SystemAssignees>> mapSystemAssigneesGroupSystems = systemAssigneesList.stream()
                .collect(Collectors.groupingBy(SystemAssignees::getSystem));
        String[] header = {"Система", "Тип изменения", stages.get(0).getName(), stages.get(1).getName(),
                stages.get(2).getName(), stages.get(3).getName(), stages.get(4).getName(), stages.get(5).getName()};
        result.add(header);
        for (Map.Entry<System, List<SystemAssignees>> map : mapSystemAssigneesGroupSystems.entrySet())  {
            System system = map.getKey();
            List<SystemAssignees> systemAssigneesListGroupSystems = map.getValue();
            Map<TypeChangeDB, List<SystemAssignees>> mapGroupTypeChange = systemAssigneesListGroupSystems.stream()
                    .collect(Collectors.groupingBy(SystemAssignees::getTypeChange));
            for (Map.Entry<TypeChangeDB, List<SystemAssignees>> entry : mapGroupTypeChange.entrySet())  {
                List<SystemAssignees> listGroupTypeChange = entry.getValue();
                TypeChangeDB typeChangeDB = entry.getKey();
                List<String> record = new ArrayList<>();
                record.add(system.getName());
                record.add(typeChangeDB.getName());
                for (Stage stage : stages)  {
                    StringBuffer recordAssignee = new StringBuffer();
                    listGroupTypeChange.stream().filter(s -> s.getStage() == stage).forEach(l -> {
                       recordAssignee.append(l.getUser()).append(" ");
                    });
                    record.add(recordAssignee.toString());
                }
                result.add(record.toArray(new String[0]));
            }

        }
        return result;
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private void write(List<String[]> dataLines, String path) {
        File csvOutputFile = new File(path);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(csvOutputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataLines.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        csvOutputFile.exists();
    }


}
