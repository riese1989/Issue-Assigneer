package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Named;

@Named
public class SpecialUsersSystemsManager extends ModelManager{
    public SpecialUsersSystemsManager(ActiveObjects ao) {
        super(ao);
    }

    public SpecialUsersSystems createSpecialUserSystems(System system, SuperUser user, Delivery delivery)    {
        return ao.executeInTransaction(new TransactionCallback<SpecialUsersSystems>() {
            @Override
            public SpecialUsersSystems doInTransaction() {
                SpecialUsersSystems sus = ao.create(SpecialUsersSystems.class);
                sus.setSystem(system);
                sus.setSuperuser(user);
                sus.setDelivery(delivery);
                sus.save();
                return sus;
            }
        });
    }
}
