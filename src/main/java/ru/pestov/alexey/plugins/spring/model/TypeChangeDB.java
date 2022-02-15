package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Table;

@Table("TypeChanges")
public interface TypeChangeDB extends Entity {
    String getName();
    void setName(String name);

    @OneToMany
    Log[] getLogs();
}
