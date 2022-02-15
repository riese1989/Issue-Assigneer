package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Table;

@Table("Systems")
public interface System extends Entity {
    String getName();
    void setName(String name);

    Boolean getActive();
    void setActive(Boolean active);

    @OneToMany
    Log[] getLogs();
}
