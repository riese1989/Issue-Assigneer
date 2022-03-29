package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;

import javax.inject.Named;

@Named
public class SMManager extends ModelManager{
    public SMManager(ActiveObjects ao) {
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
    public System getSystemById(Integer id)  {
        return ao.executeInTransaction(new TransactionCallback<System>() {
            @Override
            public System doInTransaction() {
                return ao.get(System.class, id);
            }
        });
    }

    public System getSystemByName(String name)  {
        return ao.executeInTransaction(new TransactionCallback<System>() {
            @Override
            public System doInTransaction() {
                return ao.find(System.class, Query.select().where("name = ?", name))[0];
            }
        });
    }

    public System[] getAllSystems() {
        return ao.executeInTransaction(new TransactionCallback<System[]>() {
            @Override
            public System[] doInTransaction() {
                return ao.find(System.class);
            }
        });
    }

    public System setActive(Integer idSystem, Boolean isActive) {
        return ao.executeInTransaction(new TransactionCallback<System>() {
            @Override
            public System doInTransaction() {
                System system = ao.get(System.class, idSystem);
                system.setActive(isActive);
                system.save();
                return system;
            }
        });
    }

    public Boolean isSystemActive(Integer idSystem) {
        return ao.executeInTransaction(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction() {
                System system = ao.get(System.class, idSystem);
                return system.getActive();
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<System>() {
            @Override
            public System doInTransaction() {
                System[] systems =ao.find(System.class);
                ao.delete(systems);
                return null;
            }
        });
    }
}
