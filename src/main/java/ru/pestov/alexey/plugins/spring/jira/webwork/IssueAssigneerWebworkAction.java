package ru.pestov.alexey.plugins.spring.jira.webwork;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
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
            this.step1 = pluginSettings.get(PLUGIN_STORAGE_KEY + "step1") == null ? "no value" : pluginSettings.get(PLUGIN_STORAGE_KEY + "step1").toString();
            this.step2 = pluginSettings.get(PLUGIN_STORAGE_KEY + "step2") == null ? "no value" : pluginSettings.get(PLUGIN_STORAGE_KEY + "step2").toString();
            this.step3 = pluginSettings.get(PLUGIN_STORAGE_KEY + "step3") == null ? "no value" : pluginSettings.get(PLUGIN_STORAGE_KEY + "step3").toString();
    }

    @Override
    public String doExecute() throws Exception {

        return SUCCESS; //returns SUCCESS
    }
    public String doSave() {
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step1", this.step1);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step2", this.step2);
        this.pluginSettings.put(PLUGIN_STORAGE_KEY + "step3", this.step3);
        return SUCCESS;
    }
}
