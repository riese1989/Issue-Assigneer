package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.Log;
import ru.pestov.alexey.plugins.spring.model.LogDelivery;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;
import java.util.Date;

@Named
public class LogDeliveryManager extends ModelManager {

    public LogDeliveryManager(ActiveObjects ao) {
        super(ao);
    }

    public LogDelivery create(Date date, System system, User oldDelivery, User newDelivery, User currentUser)   {
        return ao.executeInTransaction(new TransactionCallback<LogDelivery>() {
            @Override
            public LogDelivery doInTransaction() {
                LogDelivery logDelivery = ao.create(LogDelivery.class);
                logDelivery.setDate(date);
                logDelivery.setSystem(system);
                logDelivery.setOldDelivery(oldDelivery);
                logDelivery.setNewDelivery(newDelivery);
                logDelivery.setUser(currentUser);
                logDelivery.save();
                return logDelivery;
            }
        });
    }

    public LogDelivery[] getLogs(Integer idSystem)   {
        return ao.executeInTransaction(new TransactionCallback<LogDelivery[]>() {
            @Override
            public LogDelivery[] doInTransaction() {
                return ao.find(LogDelivery.class, Query.select().where("SYSTEM_ID = ?", idSystem));
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<LogDelivery>() {
            @Override
            public LogDelivery doInTransaction() {
                LogDelivery[] logs =ao.find(LogDelivery.class);
                ao.delete(logs);
                return null;
            }
        });
    }
}
