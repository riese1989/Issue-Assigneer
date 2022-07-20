package ru.pestov.alexey.plugins.spring.rest;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.enums.Role;
import ru.pestov.alexey.plugins.spring.jira.webwork.IssueAssigneerWebworkAction;
import ru.pestov.alexey.plugins.spring.model.Stage;
import ru.pestov.alexey.plugins.spring.model.SystemAssignees;
import ru.pestov.alexey.plugins.spring.service.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Named
@Path("/systems")
public class SystemRest {

    private final JSONService jsonService;
    private final TypeChangeService typeChangeService;
    private final SystemService systemService;
    private final IssueAssigneerWebworkAction issueAssigneerWebworkAction;
    private final HService hService;
    private final PluginSettingsFactory pluginSettingsFactory;
    private final PermissionService permissionService;
    private final DBService dbService;
    private final ParamService paramService;
    private final FileService fileService;


    @Inject
    public SystemRest(@ComponentImport PluginSettingsFactory pluginSettingsFactory,
                      final JSONService jsonService,
                      final TypeChangeService typeChangeService,
                      final SystemService systemService,
                      final HService hService,
                      final PermissionService permissionService,
                      final ParamService paramService,
                      IssueAssigneerWebworkAction issueAssigneerWebworkAction,
                      DBService dbService, FileService fileService) {
        this.jsonService = jsonService;
        this.issueAssigneerWebworkAction = issueAssigneerWebworkAction;
        this.typeChangeService = typeChangeService;
        this.systemService = systemService;
        this.hService = hService;
        this.permissionService = permissionService;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.dbService = dbService;
        this.paramService = paramService;
        this.fileService = fileService;
    }

    @GET
    @Path("/get")
    public Response get() {
        JSONObject jsonObject = jsonService.getJsonObject();
        return Response.ok(jsonObject.toString()).build();
    }

    @GET
    @Path("/cr")
    public Response createUsers() {
        String result = hService.createUsers();
        return Response.ok(result).build();
    }

    @GET
    @Path("/check")
    public Response checkFiles() {
        String result = hService.checkFiles();
        return Response.ok(result).build();
    }

    @GET
    @Path("/recover")
    public Response recoverDB() {
        dbService.recoverDB();
        return Response.ok().build();
    }

    @GET
    @Path("/getlistsystems")
    public Response getListSystems(@QueryParam("valuefilter") String valueFilter) {
        return Response.ok(dbService.getHashMapSystems(valueFilter).toString()).build();
    }

    @GET
    @Path("/getlisttypechanges")
    public Response getListSystems() {
        return Response.ok(dbService.getHashMapTypeChanges().toString()).build();
    }

    //done
    @GET
    @Path("/getassignees")
    public Response getSystem(@Context HttpServletRequest httpServletRequest,
                              @QueryParam("namesystem") Integer idSystem,
                              @QueryParam("typechange") Integer idTypeChange,
                              @QueryParam("stage") String nameStage) {
        return Response.ok(systemService.getAssigneesStageSystem(idSystem, idTypeChange, nameStage)).build();
    }

    @GET
    @Path("/getIa")
    public Response getInactiveAssignees(@Context HttpServletRequest httpServletRequest,
                              @QueryParam("namesystem") Integer idSystem,
                              @QueryParam("typechange") Integer idTypeChange,
                              @QueryParam("stage") String nameStage) {
        String inactiveSystemAssigneesIds = systemService.getAssigneesStageSystem(idSystem, idTypeChange, nameStage);
        return Response.ok(dbService.getInactiveUsersById(inactiveSystemAssigneesIds)).build();
    }

    @GET
    @Path(("/synch"))
    public Response runSynch()  {
        //dbService.synchronize();
        return Response.ok().build();
    }

    // done
    @GET
    @Path("/isactive")
    public Response isActive(@Context HttpServletRequest httpServletRequest,
                             @QueryParam("namesystem") Integer idSystem) {
        return Response.ok(systemService.isSystemActive(idSystem).toString()).build();
    }

    @GET
    @Path("/isuseradmin")
    public Response isUserAdmin(@Context HttpServletRequest httpServletRequest) {
        return Response.ok(permissionService.isCurrentUserAdminJira().toString()).build();
    }

    //done
    @POST
    @Path("/post")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public void post(@FormParam("systemCab") Integer idSystem,
                     @FormParam("typechange") Integer idTypeChange,
                     @FormParam("stage1") List<String> stage1,
                     @FormParam("stage21") List<String> stage21,
                     @FormParam("stage22") List<String> stage22,
                     @FormParam("stage23") List<String> stage23,
                     @FormParam("stage3") List<String> stage3,
                     @FormParam("authorize") List<String> authorize,
                     @FormParam("active") Boolean active,
                     @FormParam("delivery") String delivery) throws Exception {
        Param param = new Param(idSystem, idTypeChange,
                stage1, stage21, stage22, stage23, stage3, authorize, delivery, active);
        param = paramService.convert(param);
        dbService.updateDB(param);
        jsonService.updateJsonObject(param);
        issueAssigneerWebworkAction.setParams(param);
        new IssueAssigneerWebworkAction(pluginSettingsFactory, jsonService, systemService, typeChangeService).doExecute();
    }

    @POST
    @Path("/post2")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response post2(@FormParam("stage1") String stage1) {
        return Response.ok(stage1).build();
    }

    @GET
    @Path("/getusers")
    public Response getActiveUsers() {
        String activeUsers = dbService.getUsers();
        //List<String> activeUsersWithId = dbService.addToActiveUsersId(activeUsers);
        return Response.ok(activeUsers).build();
    }

    @GET
    @Path("/issuperuser")
    public Response isSuperuser() {
        return Response.ok(permissionService.isCurrentUserSuperUser().toString()).build();
    }

    @GET
    @Path("/delivery")
    public Response getDelivery(@QueryParam("idsystem") Integer idSystem) {
        return Response.ok(dbService.getDelivery(idSystem)).build();
    }

    @GET
    @Path("/isdelivery")
    public Response checkUserDelivery(@QueryParam("idsystem") Integer idSystem) {
        return Response.ok(permissionService.isCurrentUserDelivery(idSystem).toString()).build();
    }

    @GET
    @Path("/isenable")
    public Response checkEnable(@QueryParam("idsystem") Integer idSystem,
                                @QueryParam("idtypechange") Integer idTypeChange)   {
        return Response.ok(permissionService.isEnable(idSystem, idTypeChange).toString()).build();
    }

    @GET
    @Path("/getlogs")
    public Response getLogs(@QueryParam("idsystem") Integer idSystem,
                            @QueryParam("idtypechange") Integer idTypeChange) {
        return Response.ok(dbService.getLogs(idSystem, idTypeChange)).build();
    }

    @GET
    @Path("/stage/title")
    public Response getTitle(@QueryParam("namestage") String nameStage) {
        return Response.ok(dbService.getTitleStage(nameStage)).build();
    }

    @GET
    @Path("/stage/label")
    public Response getLabel(@QueryParam("namestage") String nameStage) {
        return Response.ok(dbService.getLabelStage(nameStage)).build();
    }

    @GET
    @Path("/getuser")
    public Response getUserById (@QueryParam("id") Integer idUser)   {
        return Response.ok(dbService.getUserById(idUser)).build();
    }

    @GET
    @Path("/export")
    public Response exportCSV() {
        List<SystemAssignees> systemAssigneesList = dbService.getListSystemsUserDelivery();
        List<Stage> stages = dbService.getAllStages();
        return Response.ok(fileService.createFile(systemAssigneesList, stages)).build();
    }

    @GET
    @Path("/countmysystems")
    public Response countMySystems()    {
        return Response.ok(dbService.getCountSystemDelivery().toString()).build();
    }

    @GET
    @Path("/checklastlog")
    public Response checkLastLog(@QueryParam("idsystem") Integer idSystem,
                                 @QueryParam("idtypechange") Integer idTypeChange)  {
        return Response.ok(dbService.checkLastLog(idSystem, idTypeChange).toString()).build();
    }
    @GET
    @Path("/getlastlogs")
    public Response getLastLog(@QueryParam("idsystem") Integer idSystem,
                                 @QueryParam("idtypechange") Integer idTypeChange)  {
        return Response.ok(dbService.getLastLogs(idSystem, idTypeChange).toString()).build();
    }

    @GET
    @Path("/isenablebulk")
    public Response checkEnableBulk()   {
        return Response.ok(permissionService.isEnableBulk().toString()).build();
    }

    @GET
    @Path("/getmysystems")
    public Response getMySystems()  {
        Boolean isSuperUser = permissionService.isCurrentUserSuperUser();
        Boolean isDelivery = permissionService.isCurrentUserDelivery();
        return Response.ok(dbService.getSystemsOfUser(isSuperUser, isDelivery).toString()).build();
    }



}
