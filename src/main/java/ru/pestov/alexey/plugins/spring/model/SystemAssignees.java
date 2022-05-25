package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

import java.util.List;

@Table("SystemAssignees")
public interface SystemAssignees extends Entity {
    System getSystem();
    void setSystem(System system);

    TypeChangeDB getTypeChange();
    void setTypeChange(TypeChangeDB typeChange);

    Stage getStage();
    void setStage(Stage stage);

    User getUser();
    void setUser(User user);
}
