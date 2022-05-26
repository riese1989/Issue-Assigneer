package ru.pestov.alexey.plugins.spring.service;

import ru.pestov.alexey.plugins.spring.model.Delivery;

import javax.inject.Named;

@Named
public class DeliveryService {
    public boolean compare(Delivery delivery1, Delivery delivery2)  {
        if (delivery1 == null && delivery2 != null) {
            return false;
        }
        if (delivery2 == null && delivery1 != null) {
            return false;
        }
        if (delivery1 == null && delivery2 == null) {
            return true;
        }
        if (delivery1.getUser().getName().equals(delivery2.getUser().getName()))    {
            return true;
        }
        return false;
    }
}
