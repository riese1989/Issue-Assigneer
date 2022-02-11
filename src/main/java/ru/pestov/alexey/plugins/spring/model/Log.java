package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

import java.util.Date;
import java.util.List;

@Table("Logs")
public interface Log extends Entity {
    User getUser();
    void setUser(User user);

    System getSystem();
    void setSystem(System system);

    TypeChangeDB getTypeChange();
    void setTypeChange(TypeChangeDB typeChange);

    Date getDate();
    void setDate(Date date);

    List<User> getOldValue();
    void setOldValue(List<User> users);

    List<User> getNewValue();
    void setNewValue(List<User> users);
}
