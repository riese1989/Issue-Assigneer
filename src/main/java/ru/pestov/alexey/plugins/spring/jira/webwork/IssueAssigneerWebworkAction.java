package ru.pestov.alexey.plugins.spring.jira.webwork;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.service.HTTPService;
import ru.pestov.alexey.plugins.spring.service.JSONService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;

@Slf4j
@Data
@Named
public class IssueAssigneerWebworkAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(IssueAssigneerWebworkAction.class);
    private String system;
    private String typeChange;
    private String step1;
    private String step21;
    private String step22;
    private String step23;
    private String step3;
    private String autorize;
    private String active;
    private final PluginSettings pluginSettings;
    private final String PLUGIN_STORAGE_KEY = "ru.pestov.alexey.plugins.spring.issue-assigneer";
    private final JSONService jsonService;

    @Inject
    public IssueAssigneerWebworkAction(@ComponentImport PluginSettingsFactory pluginSettingsFactory, final JSONService jsonService) {
            this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
            this.step1 = getSettings("step1");
            this.step21 = getSettings("step21");
            this.step22 = getSettings("step22");
            this.step23 = getSettings("step23");
            this.step3 = getSettings("step3");
            this.autorize = getSettings("autorize");
            this.active = getSettings("active");
            this.typeChange = getSettings("typechange");
            this.system = getSettings("system");
            this.jsonService = jsonService;
    }

    public void setParams(Param param)  {
        this.step1 = param.getStep1();
        this.step21 = param.getStep21();
        this.step22 = param.getStep22();
        this.step23 = param.getStep23();
        this.step3 = param.getStep3();
        this.autorize = param.getAutorize();
        this.active = param.getActive();
        this.typeChange = param.getTypeChange();
        this.system = param.getSystem();
    }

    @Override
    public String doExecute() throws Exception {
        return SUCCESS; //returns SUCCESS
    }
    public String doSave() {
        Param param = new Param(system, typeChange,step1, step21, step22, step23, step3, autorize, active);
        jsonService.updateJsonObject(param);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step1", this.step1);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step21", this.step21);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step22", this.step22);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step23", this.step23);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step3", this.step3);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "autorize", this.autorize);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "active", this.active);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "typechange", this.typeChange);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "system", this.system);
        return SUCCESS;
    }

    private String getSettings(String key)  {
        return pluginSettings.get(PLUGIN_STORAGE_KEY + key) == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + key).toString();
    }
}
