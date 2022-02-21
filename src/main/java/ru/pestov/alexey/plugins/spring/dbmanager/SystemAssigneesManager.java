package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.*;
import ru.pestov.alexey.plugins.spring.model.System;

import javax.inject.Named;

@Named
public class SystemAssigneesManager extends ModelManager {
    public SystemAssigneesManager(ActiveObjects ao) {
        super(ao);
    }
    public SystemAssignees createSystemAssignee(System system, TypeChangeDB typeChangeDB, Stage stage, User user)  {
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
}
