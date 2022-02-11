package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;

import java.util.List;

public interface SystemAssignees extends Entity {
    System getSystem();
    void setSystem(System system);

    TypeChangeDB getTypeChange();
    void setTypeChange(TypeChangeDB typeChange);

    List<User> getStage1();
    void setStage1(List<User> users);

    List<User> getStage21();
    void setStage21(List<User> users);

    List<User> getStage22();
    void setStage22(List<User> users);

    List<User> getStage23();
    void setStage23(List<User> users);

    List<User> getStage3();
    void setStage3(List<User> users);

    User getAuthorize();
    void setAuthorize(User user);
}
