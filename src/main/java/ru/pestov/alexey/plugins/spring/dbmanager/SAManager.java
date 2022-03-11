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
        return ao.find(SystemAssignees.class, Query.select().where("system_id = ?", idSystem));
    }

    public SystemAssignees[] getAssignees(Integer idSystem, Integer idTypeChange, Integer idStage) {
        return ao.executeInTransaction(new TransactionCallback<SystemAssignees[]>() {
            @Override
            public SystemAssignees[] doInTransaction() {
                return ao.find(SystemAssignees.class, Query.select().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ? AND STAGE_ID = ?", idSystem, idTypeChange, idStage));
            }
        });
    }

    public void deleteObjects(Integer idSystem, Integer idTypeChange) {
        ao.executeInTransaction(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction() {
                SystemAssignees[] systems = ao.find(SystemAssignees.class, Query.select().where("SYSTEM_ID = ? AND TYPE_CHANGE_ID = ?", idSystem, idTypeChange));
                ao.delete(systems);
                return null;
            }
        });
    }


}
