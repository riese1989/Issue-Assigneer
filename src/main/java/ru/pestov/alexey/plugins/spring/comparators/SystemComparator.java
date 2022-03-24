package ru.pestov.alexey.plugins.spring.comparators;

import ru.pestov.alexey.plugins.spring.model.System;

import java.util.Comparator;

public class SystemComparator implements Comparator<System> {
    @Override
    public int compare(System o1, System o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
