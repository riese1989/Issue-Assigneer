package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;
import java.util.Arrays;

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
                    return ao.find(User.class, Query.select().where("NAME = ?", name))[0];
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

    public User getUserById(Integer id) {
        return ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                if (id != 0) {
                    return ao.find(User.class, Query.select().where("ID = ?", id))[0];
                }
                return null;
            }
        });
    }
    public User updateStatusUser(User user) {
        return ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                boolean currentStatus = user.getActive();
                user.setActive(!currentStatus);
                user.save();
                return user;
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<User>() {
            @Override
            public User doInTransaction() {
                User[] users =ao.find(User.class);
                ao.delete(users);
                return null;
            }
        });
    }

    public User[] getActiveUsers()  {
        return ao.executeInTransaction(new TransactionCallback<User[]>() {
            @Override
            public User[] doInTransaction() {
                return ao.find(User.class, Query.select().where("ACTIVE = true"));
            }
        });
    }

}
