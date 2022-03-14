package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.TypeChangeAssignee;

import javax.inject.Named;

@Named
public class TCAManager extends ModelManager {
    public TCAManager(ActiveObjects ao) {
        super(ao);
    }

    public TypeChangeAssignee create(String name)   {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeAssignee>() {
            @Override
            public TypeChangeAssignee doInTransaction() {
                TypeChangeAssignee typeChangeAssignee = ao.create(TypeChangeAssignee.class);
                typeChangeAssignee.save();
                return typeChangeAssignee;
            }
        });
    }

    public TypeChangeAssignee get(Integer id)   {
        return ao.executeInTransaction(new TransactionCallback<TypeChangeAssignee>() {
            @Override
            public TypeChangeAssignee doInTransaction() {
                return ao.get(TypeChangeAssignee.class, id);
            }
        });
    }
}
