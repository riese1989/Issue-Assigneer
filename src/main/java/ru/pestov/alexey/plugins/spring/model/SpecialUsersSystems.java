package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

public interface SpecialUsersSystems extends Entity {
    System getSystem();
    void setSystem(System system);

    User getSuperuser();
    void setSuperuser(User superuser);

    User getDelivery();
    void setDelivery(User delivery);
}
