package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

import java.util.Date;

@Table("LogDelivery")
public interface LogDelivery extends Entity {

    Date getDate();
    void setDate(Date date);

    System getSystem();
    void setSystem(System system);

    User getOldDelivery();
    void setOldDelivery(User user);

    User getNewDelivery();
    void setNewDelivery(User user);

    User getUser();
    void setUser(User user);

}
