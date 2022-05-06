package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Named;
import java.util.Arrays;
import java.util.Date;

@Named
public class LogModelManager extends ModelManager{
    public LogModelManager(ActiveObjects ao) {
        super(ao);
    }

    public Log create (Date date, SystemAssignees systemAssignee, TypeChangeAssignee typeChangeAssignee,
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
                log.setDate(date);
                log.setSystemActive(systemAssignee.getSystem().getActive());
                log.save();
                return log;
            }
        });
    }

    public Log create (Date date, User user, System system, TypeChangeAssignee typeChangeAssignee,
                       User currentUser)   {
        return ao.executeInTransaction(new TransactionCallback<Log>() {
            @Override
            public Log doInTransaction() {
                Log log = ao.create(Log.class);
                log.setSystem(system);
                log.setAssignee(user);
                log.setTypeChangeAssignee(typeChangeAssignee);
                log.setUser(currentUser);
                log.setDate(date);
                log.save();
                return log;
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<Log>() {
            @Override
            public Log doInTransaction() {
                Log[] logs =ao.find(Log.class);
                ao.delete(logs);
                return null;
            }
        });
    }

    public Log[] getDate(Integer idSystem, Integer idTypeChange)   {
        return ao.executeInTransaction(new TransactionCallback<Log[]>() {
            @Override
            public Log[] doInTransaction() {
                Log[] logs =  ao.find(Log.class, Query.select("DATE").distinct().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ?", idSystem, idTypeChange));
                return Arrays.stream(logs).distinct().toArray(Log[]::new);
            }
        });
    }

    public Log[] getLogsByDate(Integer idSystem, Integer idTypeChange, Date date)   {
        return ao.executeInTransaction(new TransactionCallback<Log[]>() {
            @Override
            public Log[] doInTransaction() {
                return ao.find(Log.class, Query.select().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ? AND DATE = ?", idSystem, idTypeChange, date));
            }
        });
    }

}
