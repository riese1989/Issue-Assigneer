package ru.pestov.alexey.plugins.spring.model;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

public interface SpecialUsersSystems extends Entity {
    System getSystem();
    void setSystem(System system);

    SuperUser getSuperuser();
    void setSuperuser(SuperUser superuser);

    Delivery getDelivery();
    void setDelivery(Delivery delivery);
}
