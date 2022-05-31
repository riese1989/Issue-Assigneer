package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Table;

@Table("Stages")
public interface Stage extends Entity {
    String getName();
    void setName(String name);

    String getLabel();
    void setLabel(String label);

    String getTitle();
    void setTitle(String title);

    @OneToMany
    SystemAssignees[] getSystemAssignees();
}
