package ru.pestov.alexey.plugins.spring.jira.webwork;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.service.JSONService;
import ru.pestov.alexey.plugins.spring.service.SystemService;
import ru.pestov.alexey.plugins.spring.service.TypeChangeService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Slf4j
@Data
@Named
public class IssueAssigneerWebworkAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(IssueAssigneerWebworkAction.class);
    private String systemId;
    private String typeChangeId;
    private List<String> stage1;
    private List<String> stage21;
    private List<String> stage22;
    private List<String> stage23;
    private List<String> stage3;
    private List<String> authorize;
    private String active;
    private final PluginSettings pluginSettings;
    private final String PLUGIN_STORAGE_KEY = "ru.pestov.alexey.plugins.spring.issue-assigneer";
    private final JSONService jsonService;
    private final SystemService systemService;
    private final TypeChangeService typeChangeService;

    @Inject
    public IssueAssigneerWebworkAction(@ComponentImport PluginSettingsFactory pluginSettingsFactory,
                                       final JSONService jsonService,
                                       final SystemService systemService,
                                       final TypeChangeService typeChangeService) {
            this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
            this.active = getSettings("active");
            this.typeChangeId = getSettings("typechange");
            this.systemId = getSettings("system");
            this.jsonService = jsonService;
            this.systemService = systemService;
            this.typeChangeService = typeChangeService;
    }

    public void setParams(Param param)  {
        this.stage1 = param.getStage1();
        this.stage21 = param.getStage21();
        this.stage22 = param.getStage22();
        this.stage23 = param.getStage23();
        this.stage3 = param.getStage3();
        this.authorize = param.getAuthorize();
        this.active = String.valueOf(param.getActive());
        this.typeChangeId = String.valueOf(param.getTypeChangeId());
        this.systemId = String.valueOf(param.getSystemId());
    }

    @Override
    public String doExecute() throws Exception {
        return SUCCESS; //returns SUCCESS
    }
    public String doSave() {
//        Param param = new Param(system, typeChange,stage1, stage21, stage22, stage23, stage3, authorize, active);
//        jsonService.updateJsonObject(param);
        return SUCCESS;
    }

    private String getSettings(String key)  {
        return pluginSettings.get(PLUGIN_STORAGE_KEY + key) == null ? "" : pluginSettings.get(PLUGIN_STORAGE_KEY + key).toString();
    }
}
