package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.SuperUser;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.User;

public class SuperUserModelManager extends ModelManager {
    public SuperUserModelManager(ActiveObjects ao) {
        super(ao);
    }

    public SuperUser getSuperUserBySystem(System system)  {
        return ao.executeInTransaction(new TransactionCallback<SuperUser>() {
            @Override
            public SuperUser doInTransaction() {
                return ao.find(SuperUser.class, Query.select().where("system_id = ?", system.getID()))[0];
            }
        });
    }
}
