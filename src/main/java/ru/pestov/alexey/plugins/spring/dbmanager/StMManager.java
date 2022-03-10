package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import net.java.ao.Query;
import ru.pestov.alexey.plugins.spring.model.Stage;

import javax.inject.Named;

@Named
public class StMManager extends ModelManager {
    public StMManager(ActiveObjects ao) {
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

    public Stage getStageByName(String nameStage)   {
        return ao.executeInTransaction(new TransactionCallback<Stage>() {
            @Override
            public Stage doInTransaction() {
                return ao.find(Stage.class, Query.select().where("NAME = ?", nameStage))[0];
            }
        });
    }
}