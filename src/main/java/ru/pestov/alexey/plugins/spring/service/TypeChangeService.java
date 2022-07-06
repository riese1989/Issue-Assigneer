package ru.pestov.alexey.plugins.spring.service;

import lombok.Getter;
import ru.pestov.alexey.plugins.spring.comparators.TypeChangesComparator;
import ru.pestov.alexey.plugins.spring.entity.SystemCAB;

import javax.inject.Named;
import java.util.Collections;
import java.util.HashMap;

@Getter
@Named
public class TypeChangeService  {
    HashMap <String, String> typeChanges = new HashMap<>();

    public TypeChangeService() {
        typeChanges.put("Bugfix", "Bugfix");
        typeChanges.put("Активация настроек", "Activatcia nastroek");
        typeChanges.put("Внерелиз", "Vnereliz");
        typeChanges.put("Дефект", "Defect");
        typeChanges.put("Изменение настроек", "Izmenenie nastroek");
        typeChanges.put("Массовая выгрузка", "Massovaya vigruzka");
        typeChanges.put("Предсогласованное изменение", "Predsoglasovannoe izmenenie");
        typeChanges.put("Релиз", "Reliz");
        typeChanges.put("Спринт", "Sprint");
        typeChanges.put("Фоновое задание", "Fonovoe zadanie");
    }

    public String getTypeChangeById(String stringId)    {
        Integer id = Integer.parseInt(stringId) - 1;
        return typeChanges.get(stringId);
    }

    public static void sort (SystemCAB systemCAB)   {
        Collections.sort(systemCAB.getTypeChanges(),new TypeChangesComparator());
    }
}
