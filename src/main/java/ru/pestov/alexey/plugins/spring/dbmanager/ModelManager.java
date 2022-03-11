package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;

public abstract class ModelManager {
    protected final ActiveObjects ao;

    @Inject
    public ModelManager(@ComponentImport ActiveObjects ao)   {
        this.ao = ao;
    }
//
//    public abstract Object getById(Integer id);
//    public abstract Object getByName(String name);
//    public abstract Object getAll();
//    public abstract Object create();
}
