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
                    return Arrays.stream(ao.find(User.class)).filter(user -> user.getName().equals(name)).findFirst().get();
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
                return ao.find(User.class, Query.select().where("ID = ?", id))[0];
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

}
