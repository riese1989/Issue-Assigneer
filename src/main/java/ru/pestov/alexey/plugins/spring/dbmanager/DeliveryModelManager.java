package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.Delivery;
import ru.pestov.alexey.plugins.spring.model.SuperUser;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;

@Named
public class DeliveryModelManager extends ModelManager{
    public DeliveryModelManager(ActiveObjects ao) {
        super(ao);
    }
    public Delivery getSuperUserBySystem(System system)  {
        return ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                return ao.find(Delivery.class, Query.select().where("system_id = ?", system.getID()))[0];
            }
        });
    }
    public Delivery createDelivery(System system, User user)    {
        return ao.executeInTransaction(new TransactionCallback<Delivery>() {
            @Override
            public Delivery doInTransaction() {
                Delivery delivery = ao.create(Delivery.class);
                delivery.setSystem(system);
                delivery.setDelivery(user);
                delivery.save();
                return delivery;
            }
        });
    }
}
