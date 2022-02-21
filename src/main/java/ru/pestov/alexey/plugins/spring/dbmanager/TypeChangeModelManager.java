package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.Stage;
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
}
