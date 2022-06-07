package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.Delivery;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;

@Named
public class DMManager extends ModelManager{
    public DMManager(ActiveObjects ao) {
        super(ao);
    }
    public Delivery getDelivery(System system)  {
        return ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                Delivery[] deliveries = ao.find(Delivery.class, Query.select().where("SYSTEM_ID = ?", system.getID()));
                if(deliveries.length != 0)  {
                    return deliveries[0];
                }
                return null;
            }
        });
    }
    public Delivery createDelivery(System system, User user)    {
        return ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                Delivery delivery = ao.create(Delivery.class);
                delivery.setSystem(system);
                delivery.setUser(user);
                delivery.save();
                return delivery;
            }
        });
    }

    public Delivery delete(Delivery delivery)   {
        return ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                ao.delete(delivery);
                return delivery;
            }
        });
    }

    public Delivery[] getSystemsByDelivery(User user)   {
        return ao.executeInTransaction(new TransactionCallback<Delivery[]>() {
            @Override
            public Delivery[] doInTransaction() {
                return ao.find(Delivery.class, Query.select().where("USER_ID = ?", user.getID()));
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                Delivery[] deliveries =ao.find(Delivery.class);
                ao.delete(deliveries);
                return null;
            }
        });
    }

    public Delivery updateDelivery (Delivery delivery, User newDeliveryUser) {
        return ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                delivery.setUser(newDeliveryUser);
                delivery.save();
                return delivery;
            }
        });
    }
}
