package ru.pestov.alexey.plugins.spring.model;


import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("Superusers")
public interface SuperUser extends Entity {

    System getSystem();
    void setSystem(System system);

    SuperUser getSuperUser();
    void setSuperUser(SuperUser superUser);
}