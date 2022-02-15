package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.ManyToMany;
import net.java.ao.schema.Table;

@Table("Users")
public interface User extends Entity {
    String getName();
    void setName(String name);

    boolean getActive();
    void setActive(boolean active);

    @ManyToMany(LogToUser.class)
    Log[] getLogs();

}
