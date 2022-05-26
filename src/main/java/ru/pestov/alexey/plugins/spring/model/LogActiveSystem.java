package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;

import java.util.Date;

public interface LogActiveSystem extends Entity {

    Date getDate();
    void setDate(Date date);

    System getSystem();
    void setSystem(System system);

    Boolean getNewValue();
    void setNewValue(Boolean newValue);

    User getUser();
    void setUser(User user);
}
