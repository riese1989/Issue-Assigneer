package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.TypeChangeAssignee;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;

import javax.inject.Named;
import java.lang.reflect.Array;

@Named
public class TCMManager extends ModelManager {
    public TCMManager(ActiveObjects ao) {
        super(ao);
    }


    public TypeChangeDB createTypeChange(String name) {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeDB>() {
            @Override
            public TypeChangeDB doInTransaction() {
                TypeChangeDB typeChangeDB = ao.create(TypeChangeDB.class);
                typeChangeDB.setName(name);
                typeChangeDB.save();
                return typeChangeDB;
            }
        });
    }

    public TypeChangeDB getTypeChangeByName(String name) {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeDB>() {
            @Override
            public TypeChangeDB doInTransaction() {
                try {
                    return ao.find(TypeChangeDB.class, Query.select().where("NAME = ?", name))[0];
                }
                catch (ArrayIndexOutOfBoundsException ex)   {
                    return null;
                }
            }
        });
    }

    public TypeChangeDB[] getAllTypeChanges() {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeDB[]>() {
            @Override
            public TypeChangeDB[] doInTransaction() {
                return ao.find(TypeChangeDB.class);
            }
        });
    }

    public TypeChangeDB getTypeChangeById(Integer idTypeChange) {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeDB>() {
            @Override
            public TypeChangeDB doInTransaction() {
                return ao.get(TypeChangeDB.class, idTypeChange);
            }
        });
    }

    public void deleteAll() {
        ao.executeInTransaction(new TransactionCallback<TypeChangeDB>() {
            @Override
            public TypeChangeDB doInTransaction() {
                TypeChangeDB[] typeChangeDBS =ao.find(TypeChangeDB.class);
                ao.delete(typeChangeDBS);
                return null;
            }
        });
    }
}
