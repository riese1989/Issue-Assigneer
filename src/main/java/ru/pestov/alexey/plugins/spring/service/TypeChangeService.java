package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.comparators.TypeChangesComparator;
import ru.pestov.alexey.plugins.spring.entity.SystemCAB;

import javax.inject.Named;
import java.util.Collections;
import java.util.HashMap;

@Named
public class TypeChangeService  {
    HashMap <String, String> typeChanges = new HashMap<>();

    public TypeChangeService() {
        typeChanges.put("1", "Bugfix");
        typeChanges.put("2", "Активация настроек");
        typeChanges.put("3", "Внерелиз");
        typeChanges.put("4", "Дефект");
        typeChanges.put("5", "Изменение настроек");
        typeChanges.put("6", "Массовая выгрузка");
        typeChanges.put("7", "Предсогласованное изменение");
        typeChanges.put("8", "Релиз");
        typeChanges.put("9", "Спринт");
        typeChanges.put("10", "Фоновое задание");
    }

    public String getTypeChangeById(String stringId)    {
        Integer id = Integer.parseInt(stringId) - 1;
        return typeChanges.get(stringId);
    }

    public static void sort (SystemCAB systemCAB)   {
        Collections.sort(systemCAB.getTypeChanges(),new TypeChangesComparator());
    }
}
