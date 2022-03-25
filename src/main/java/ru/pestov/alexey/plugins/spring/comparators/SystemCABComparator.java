package ru.pestov.alexey.plugins.spring.comparators;

import ru.pestov.alexey.plugins.spring.entity.SystemCAB;

import java.util.Comparator;

public class SystemCABComparator implements Comparator<SystemCAB> {

    @Override
    public int compare(SystemCAB o1, SystemCAB o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
