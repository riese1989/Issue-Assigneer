package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.SuperUser;
import ru.pestov.alexey.plugins.spring.model.System;

public class SUMManager extends ModelManager {
    public SUMManager(ActiveObjects ao) {
        super(ao);
    }

    public SuperUser getSuperUserBySystem(System system)  {
        return ao.executeInTransaction(new TransactionCallback<SuperUser>() {
            @Override
            public SuperUser doInTransaction() {
                return ao.find(SuperUser.class, Query.select().where("SYSTEM_ID = ?", system.getID()))[0];
            }
        });
    }
}
