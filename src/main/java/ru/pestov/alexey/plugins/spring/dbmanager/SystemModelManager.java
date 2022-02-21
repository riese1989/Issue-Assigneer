package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Named;

@Named
public class SystemModelManager extends ModelManager{
    public SystemModelManager(ActiveObjects ao) {
        super(ao);
    }
    public System createSystem(String name, Boolean isActive)   {
        return ao.executeInTransaction(new TransactionCallback<System>() {
            @Override
            public System doInTransaction() {
                System system = ao.create(System.class);
                system.setName(name);
                system.setActive(isActive);
                system.save();
                return system;
            }
        });
    }
}
