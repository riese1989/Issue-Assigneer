package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.Delivery;
import ru.pestov.alexey.plugins.spring.model.SuperUser;
import ru.pestov.alexey.plugins.spring.model.System;

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
}
