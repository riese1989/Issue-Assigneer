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

import javax.inject.Inject;
import java.util.Arrays;

@Slf4j
@Data
public class IssueAssigneerWebworkAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(IssueAssigneerWebworkAction.class);
    private String system;
    private String typeChange;
    private String step1;
    private String step2;
    private String step3;
    private final PluginSettings pluginSettings;
    private final String PLUGIN_STORAGE_KEY = "ru.pestov.alexey.plugins.spring.issue-assigneer";

    @Inject
    public IssueAssigneerWebworkAction(@ComponentImport PluginSettingsFactory pluginSettingsFactory) {
            this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
            this.step1 = pluginSettings.get(PLUGIN_STORAGE_KEY + "step1") == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + "step1").toString();
            this.step2 = pluginSettings.get(PLUGIN_STORAGE_KEY + "step2") == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + "step2").toString();
            this.step3 = pluginSettings.get(PLUGIN_STORAGE_KEY + "step3") == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + "step3").toString();
            this.typeChange = pluginSettings.get(PLUGIN_STORAGE_KEY + "typechange") == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + "typechange").toString();
            this.system = pluginSettings.get(PLUGIN_STORAGE_KEY + "system") == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + "system").toString();
    }

    @Override
    public String doExecute() throws Exception {

        return SUCCESS; //returns SUCCESS
    }
    public String doSave() {
        System.out.println("****************************");
        System.out.println("System " + this.system);
        System.out.println("type change " + this.typeChange);
        System.out.println("step1 " + this.step1);
        System.out.println("step2 " + this.step2);
        System.out.println("step3 " + this.step3);
        System.out.println("****************************");
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step1", this.step1);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step2", this.step2);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step3", this.step3);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "typechange", this.typeChange);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "system", this.system);
        return SUCCESS;
    }
}
