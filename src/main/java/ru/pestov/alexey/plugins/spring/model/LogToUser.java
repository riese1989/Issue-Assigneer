package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;

public interface LogToUser extends Entity {
    Log getLog();
    void setLog(Log log);

    User getUser();
    void setUser(User user);
}
