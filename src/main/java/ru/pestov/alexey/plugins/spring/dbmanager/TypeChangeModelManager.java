package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;

import javax.inject.Named;

@Named
public class TypeChangeModelManager extends ModelManager{
    public TypeChangeModelManager(ActiveObjects ao) {
        super(ao);
    }
    public TypeChangeDB createTypeChange(String name)   {
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
    public TypeChangeDB getTypeChangeByName(String name)  {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeDB>() {
            @Override
            public TypeChangeDB doInTransaction() {
                return ao.find(TypeChangeDB.class, Query.select().where("name = ?", name))[0];
            }
        });
    }
    public TypeChangeDB[] getAllTypeChanges()   {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeDB[]>() {
            @Override
            public TypeChangeDB[] doInTransaction() {
                return ao.find(TypeChangeDB.class);
            }
        });
    }
}
