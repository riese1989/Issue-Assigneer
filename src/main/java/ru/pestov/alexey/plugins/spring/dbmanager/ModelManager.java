package ru.pestov.alexey.plugins.spring.dbmanager;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;

public class ModelManager {
    protected final ActiveObjects ao;

    @Inject
    public ModelManager(@ComponentImport ActiveObjects ao)   {
        this.ao = ao;
    }
}
