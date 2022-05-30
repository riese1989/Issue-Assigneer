package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.LogActiveSystem;
import ru.pestov.alexey.plugins.spring.model.LogDelivery;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;
import java.util.Date;

@Named
public class LogActiveSystemManager extends ModelManager {
    public LogActiveSystemManager(ActiveObjects ao) {
        super(ao);
    }

    public LogActiveSystem create(Date date, System system, Boolean newValue, User currentUser)    {
        return ao.executeInTransaction(new TransactionCallback<LogActiveSystem>() {
            @Override
            public LogActiveSystem doInTransaction() {
                LogActiveSystem logActiveSystem = ao.create(LogActiveSystem.class);
                logActiveSystem.setDate(date);
                logActiveSystem.setSystem(system);
                logActiveSystem.setNewValue(newValue);
                logActiveSystem.setUser(currentUser);
                logActiveSystem.save();
                return logActiveSystem;
            }
        });
    }

    public LogActiveSystem[] getLogs(Integer idSystem)   {
        return ao.executeInTransaction(new TransactionCallback<LogActiveSystem[]>() {
            @Override
            public LogActiveSystem[] doInTransaction() {
                return ao.find(LogActiveSystem.class, Query.select().where("SYSTEM_ID = ?", idSystem));
            }
        });
    }
}
