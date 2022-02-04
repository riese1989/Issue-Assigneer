package ru.pestov.alexey.plugins.spring.rest;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.jira.webwork.IssueAssigneerWebworkAction;
import ru.pestov.alexey.plugins.spring.service.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Named
@Path("/systems")
public class SystemRest {

    private final JSONService jsonService;
    private final StringService stringService;
    private final TypeChangeService typeChangeService;
    private final SystemService systemService;
    private final IssueAssigneerWebworkAction issueAssigneerWebworkAction;
    private final UserService userService;
    private final HService hService;
    private final PluginSettingsFactory pluginSettingsFactory;


    @Inject
    public SystemRest(@ComponentImport PluginSettingsFactory pluginSettingsFactory,
                      final JSONService jsonService,
                      final StringService stringService,
                      final TypeChangeService typeChangeService,
                      final SystemService systemService,
                      final UserService userService,
                      final HService hService,
                      IssueAssigneerWebworkAction issueAssigneerWebworkAction) {
        this.jsonService = jsonService;
        this.issueAssigneerWebworkAction = issueAssigneerWebworkAction;
        this.stringService = stringService;
        this.typeChangeService = typeChangeService;
        this.systemService = systemService;
        this.userService = userService;
        this.hService = hService;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @GET
    @Path("/get")
    public Response get()
    {
        JSONObject jsonObject = jsonService.getJsonObject();
        return Response.ok(jsonObject.toString()).build();
    }

    @GET
    @Path("/cr")
    public Response createUsers()   {
        return Response.ok(hService.createUsers()).build();
    }

    @GET
    @Path("/getlistsystems")
    public Response getListSystems()    {
        return Response.ok(systemService.getSystems()).build();
    }

    @GET
    @Path("/getsystem")
    public Response getSystem(@Context HttpServletRequest httpServletRequest,
                              @QueryParam("namesystem") String nameSystem) {
        return Response.ok(systemService.getSystem(nameSystem).toString()).build();
    }
    @GET
    @Path("/getassignees")
    public Response getSystem(@Context HttpServletRequest httpServletRequest,
                              @QueryParam("namesystem") String idSystem,
                              @QueryParam("typechange") String idChange,
                              @QueryParam("stage") String stage,
                              @QueryParam("_") String a) {
        String nameSystem = systemService.getNameSystemById(idSystem);
        String typeChange = typeChangeService.getTypeChangeById(idChange);
        return Response.ok(systemService.getStageSystem(nameSystem, typeChange, stage)).build();
    }
    @GET
    @Path("/isactive")
    public Response isActive(@Context HttpServletRequest httpServletRequest,
                             @QueryParam("namesystem") String idSystem) {
        return Response.ok(systemService.isSystemActive(idSystem).toString()).build();
    }
    @POST
    @Path("/post")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public void post(@FormParam("systemCab") String system,
                         @FormParam("typechange") String typeChange,
                         @FormParam("stage1") List<String> stage1,
                         @FormParam("stage21") List<String> stage21,
                         @FormParam("stage22") List<String> stage22,
                         @FormParam("stage23") List<String> stage23,
                         @FormParam("stage3") List<String> stage3,
                         @FormParam("authorize") List<String> authorize,
                         @FormParam("active") String active) throws Exception {
        Param param = new Param(systemService.getNameSystemById(system), typeChangeService.getTypeChangeById(typeChange),
                stage1, stage21, stage22, stage23, stage3, authorize, active);
        jsonService.updateJsonObject(param);
        issueAssigneerWebworkAction.setParams(param);
        new IssueAssigneerWebworkAction(pluginSettingsFactory,jsonService, systemService, typeChangeService).doExecute();
    }

    @POST
    @Path("/post2")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response post2(@FormParam("stage1") String stage1)  {
        return Response.ok(stage1).build();
    }

    @GET
    @Path("/getactiveusers")
    public Response getActiveUsers() {
        List<String> activeUsers = this.userService.getActiveUsers();
        return Response.ok(activeUsers.toString()).build();
    }
}
