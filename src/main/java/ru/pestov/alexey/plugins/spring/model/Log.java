package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.ManyToMany;
import net.java.ao.schema.Table;

import java.util.Date;

@Table("Logs")
public interface Log extends Entity {

    System getSystem();
    void setSystem(System system);

    TypeChangeDB getTypeChange();
    void setTypeChange(TypeChangeDB typeChange);

    TypeChangeAssignee getTypeChangeAssignee();
    void setTypeChangeAssignee(TypeChangeAssignee typeChangeAssignee);

    User getAssignee();
    void setAssignee(User user);

    Date getDate();
    void setDate(Date date);

    User getUser();
    void setUser(User user);

    Stage getStage();
    void setStage(Stage stage);

    Boolean getSystemActive();
    void setSystemActive(Boolean flag);
}
