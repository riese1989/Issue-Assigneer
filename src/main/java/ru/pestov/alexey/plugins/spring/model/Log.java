package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.ManyToMany;
import net.java.ao.schema.Table;
import org.joda.time.DateTime;

@Table("Logs")
public interface Log extends Entity {

    System getSystem();
    void setSystem(System system);

    TypeChangeDB getTypeChange();
    void setTypeChange(TypeChangeDB typeChange);

    @ManyToMany(LogToUser.class)
    User[] getOldValueUsers();

    @ManyToMany(LogToUser.class)
    User[] getNewValueUsers();
}
