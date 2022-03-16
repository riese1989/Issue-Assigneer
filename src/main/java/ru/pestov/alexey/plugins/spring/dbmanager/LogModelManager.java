package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Named;
import java.util.Date;

@Named
public class LogModelManager extends ModelManager{
    public LogModelManager(ActiveObjects ao) {
        super(ao);
    }

    public Log create (SystemAssignees systemAssignee, TypeChangeAssignee typeChangeAssignee,
                       User currentUser)   {
        return ao.executeInTransaction(new TransactionCallback<Log>() {
            @Override
            public Log doInTransaction() {
                Log log = ao.create(Log.class);
                log.setSystem(systemAssignee.getSystem());
                log.setTypeChange(systemAssignee.getTypeChange());
                log.setStage(systemAssignee.getStage());
                log.setAssignee(systemAssignee.getUser());
                log.setTypeChangeAssignee(typeChangeAssignee);
                log.setUser(currentUser);
                log.setDate(new Date());
                log.setSystemActive(systemAssignee.getSystem().getActive());
                log.save();
                return log;
            }
        });
    }

    public Log create (Delivery delivery, TypeChangeAssignee typeChangeAssignee,
                       User currentUser)   {
        return ao.executeInTransaction(new TransactionCallback<Log>() {
            @Override
            public Log doInTransaction() {
                Log log = ao.create(Log.class);
                log.setSystem(delivery.getSystem());
                log.setAssignee(delivery.getDelivery());
                log.setTypeChangeAssignee(typeChangeAssignee);
                log.setUser(currentUser);
                log.setDate(new Date());
                log.save();
                return log;
            }
        });
    }
}
