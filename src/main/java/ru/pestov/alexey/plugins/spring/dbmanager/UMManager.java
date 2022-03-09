package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;

@Named
public class UMManager extends ModelManager{

    public UMManager(ActiveObjects ao) {
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
                try {
                    return ao.find(User.class, Query.select().where("name = ?", name))[0];
                }
                catch (Exception ex)    {
                    ex.printStackTrace();
                    return null;
                }
            }
        });
    }

    public User[] getAllUsers() {
        return ao.executeInTransaction(new TransactionCallback<User[]>() {
            @Override
            public User[] doInTransaction() {
                return ao.find(User.class);
            }
        });
    }

    public User getUserById(int id) {
        return ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                return ao.find(User.class, Query.select().where("id = ?", id))[0];
            }
        });
    }

}
