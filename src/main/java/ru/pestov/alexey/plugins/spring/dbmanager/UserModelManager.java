package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserModelManager extends ModelManager{

    public UserModelManager(ActiveObjects ao) {
        super(ao);
    }

    public User createUser(ApplicationUser applicationUser)    {
        return ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                User user = ao.create(User.class);
                user.setActive(applicationUser.isActive());
                user.setName(applicationUser.getName());
                user.save();
                return user;
            }
        });
    }

    public User createUser(String nameUser)    {
        return ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                User user = ao.create(User.class);
                user.setActive(true);
                user.setName(nameUser);
                user.save();
                return user;
            }
        });
    }

    public User getUserByName(String name)  {
        return ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                return ao.find(User.class, Query.select().where("name = ?", name))[0];
            }
        });
    }

}
