package ru.pestov.alexey.plugins.spring.model;


import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("Deliveries")
public interface Delivery extends Entity {

    System getSystem();
    void setSystem(System system);

    User getUser();
    void setUser(User user);
}
