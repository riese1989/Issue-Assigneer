package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserManager {
    private final ActiveObjects ao;

    @Inject
    public UserManager(@ComponentImport ActiveObjects ao)   {
        this.ao = ao;
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

}
