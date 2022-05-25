package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Named;

@Named
public class SAManager extends ModelManager {
    public SAManager(ActiveObjects ao) {
        super(ao);
    }

    public SystemAssignees createSystemAssignee(System system, TypeChangeDB typeChangeDB, Stage stage, User user) {
        return ao.executeInTransaction(new TransactionCallback<SystemAssignees>() {
            @Override
            public SystemAssignees doInTransaction() {
                SystemAssignees sa = ao.create(SystemAssignees.class);
                sa.setSystem(system);
                sa.setTypeChange(typeChangeDB);
                sa.setStage(stage);
                sa.setUser(user);
                sa.save();
                return sa;
            }
        });
    }

    public SystemAssignees[] getAssigneesSystem(Integer idSystem) {
        return ao.find(SystemAssignees.class, Query.select().where("SYSTEM_ID = ?", idSystem));
    }

    public SystemAssignees[] getAssignees(Integer idSystem, Integer idTypeChange, Integer idStage) {
        return ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                return ao.find(SystemAssignees.class, Query.select().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ? AND STAGE_ID = ?", idSystem, idTypeChange, idStage));
            }
        });
    }

    public SystemAssignees[] getAssignees(Integer idSystem, Integer idTypeChange) {
        return ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                return ao.find(SystemAssignees.class, Query.select().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ?", idSystem, idTypeChange));
            }
        });
    }

    public void deleteObjects(Integer idSystem, Integer idTypeChange) {
        ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                SystemAssignees[] systems = ao.find(SystemAssignees.class, Query.select().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ?", idSystem, idTypeChange));
                ao.delete(systems);
                return systems;
            }
        });
    }

    public void deleteObjects(SystemAssignees[] systems) {
        ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                ao.delete(systems);
                return systems;
            }
        });
    }

    public SystemAssignees[] getSystemAssigneesByUser(User user, Stage stage)    {
        return ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                return ao.find(SystemAssignees.class, Query.select().where("USER_ID = ? AND STAGE_ID <> ?", user.getID(), stage.getID()));
            }
        });
    }

    public SystemAssignees[] getSystemAssigneesByAuthorize(User user, Stage stage)    {
        return ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                return ao.find(SystemAssignees.class, Query.select().where("USER_ID = ? AND STAGE_ID = ?", user.getID(), stage.getID()));
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<SystemAssignees>() {
            @Override
            public SystemAssignees doInTransaction() {
                SystemAssignees[] systemAssignees =ao.find(SystemAssignees.class);
                ao.delete(systemAssignees);
                return null;
            }
        });
    }
}
