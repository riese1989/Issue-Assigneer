package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import ru.pestov.alexey.plugins.spring.model.Stage;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;
import ru.pestov.alexey.plugins.spring.model.User;

import javax.inject.Named;

@Named
public class StageModelManager extends ModelManager {
    public StageModelManager(ActiveObjects ao) {
        super(ao);
    }
    public Stage createStage(String name)   {
        return ao.executeInTransaction(new TransactionCallback<Stage>() {
            @Override
            public Stage doInTransaction() {
                Stage stage = ao.create(Stage.class);
                stage.setName(name);
                stage.save();
                return stage;
            }
        });
    }
    public Stage[] getAllStages()   {
        return ao.executeInTransaction(new TransactionCallback<Stage[]>() {
            @Override
            public Stage[] doInTransaction() {
                return ao.find(Stage.class);
            }
        });
    }
}
