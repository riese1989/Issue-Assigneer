package ru.pestov.alexey.plugins.spring.rest;

import com.atlassian.jira.util.json.JSONException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    //create users from json-file (for launch on localhost)
    @GET
    @Path("/cr")
    public Response createUsers() {
        String result = hService.createUsers();
        return Response.ok(result).build();
    }

    //check availability of files CAB_delivery and CAB_approval
    @GET
    @Path("/check")
    public Response checkFiles() {
        String result = hService.checkFiles();
        return Response.ok(result).build();
    }

    //filling database on first launch
    @GET
    @Path("/recover")
    public Response recoverDB() {
        dbService.recoverDB();
        return Response.ok().build();
    }

    //get list systems for filling element select on front
    //valuefilter - it's parameter from front with view "1,2,3,4"
    //1 - return all systems
    //2 - get systems, where current user is assignee
    //3 - get systems, where current user is authorize
    //4 - get systems, where current user is delivery
    @GET
    @Path("/getlistsystems")
    public Response getListSystems(@QueryParam("valuefilter") String valueFilter) {
        return Response.ok(dbService.getHashMapSystems(valueFilter).toString()).build();
    }

    //get list possible type changes
    @GET
    @Path("/getlisttypechanges")
    public Response getListSystems() {
        return Response.ok(dbService.getHashMapTypeChanges().toString()).build();
    }

    //get assignees of current system on current stage and type change
    @GET
    @Path("/getassignees")
    public Response getSystem(@Context HttpServletRequest httpServletRequest,
                              @QueryParam("namesystem") Integer idSystem,
                              @QueryParam("typechange") Integer idTypeChange,
                              @QueryParam("stage") String nameStage) {
        return Response.ok(systemService.getAssigneesStageSystem(idSystem, idTypeChange, nameStage)).build();
    }
    //get list inactive assignees (like as /getassignees)
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
    public Response runSynch() {
        //dbService.synchronize();
        return Response.ok().build();
    }

    // get status system
    @GET
    @Path("/isactive")
    public Response isActive(@Context HttpServletRequest httpServletRequest,
                             @QueryParam("namesystem") Integer idSystem) {
        return Response.ok(systemService.isSystemActive(idSystem).toString()).build();
    }

    //check administrator permission of user
    @GET
    @Path("/isuseradmin")
    public Response isUserAdmin(@Context HttpServletRequest httpServletRequest) {
        return Response.ok(permissionService.isCurrentUserAdminJira().toString()).build();
    }

    //get data from form and put in database
    @POST
    @Path("/post")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public void post(@FormParam("systemCab") List<Integer> idSystems,
                     @FormParam("typechange") List<Integer> idTypeChanges,
                     @FormParam("stage1") List<Integer> stage1,
                     @FormParam("stage21") List<Integer> stage21,
                     @FormParam("stage22") List<Integer> stage22,
                     @FormParam("stage23") List<Integer> stage23,
                     @FormParam("stage3") List<Integer> stage3,
                     @FormParam("authorize") List<Integer> authorize,
                     @FormParam("active") Boolean active,
                     @FormParam("delivery") Integer delivery) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("systems", idSystems);
        jsonObject.put("typeChanges", idTypeChanges);
        jsonObject.put("stage1_id", stage1);
        jsonObject.put("stage21_id", stage21);
        jsonObject.put("stage22_id", stage22);
        jsonObject.put("stage23_id", stage23);
        jsonObject.put("stage3_id", stage3);
        jsonObject.put("authorize_id", authorize);
        jsonObject.put("delivery_id", delivery);
        jsonObject.put("active_id", active);
        jsonObject.put("stage1_enable", true);
        jsonObject.put("stage21_enable", true);
        jsonObject.put("stage22_enable", true);
        jsonObject.put("stage23_enable", true);
        jsonObject.put("stage3_enable", true);
        jsonObject.put("authorize_enable", true);
        jsonObject.put("delivery_enable", true);
        jsonObject.put("active_enable", true);
        dbService.updateDB(jsonObject);
    }

    @POST
    @Path("/post2")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response post2(@FormParam("stage1") String stage1) {
        return Response.ok(stage1).build();
    }

    //get list active users
    @GET
    @Path("/getusers")
    public Response getActiveUsers() {
        String activeUsers = dbService.getUsers();
        //List<String> activeUsersWithId = dbService.addToActiveUsersId(activeUsers);
        return Response.ok(activeUsers).build();
    }

    //check permission of current user as superuser
    @GET
    @Path("/issuperuser")
    public Response isSuperuser() {
        return Response.ok(permissionService.isCurrentUserSuperUser().toString()).build();
    }

    //get delivery of system
    @GET
    @Path("/delivery")
    public Response getDelivery(@QueryParam("idsystem") Integer idSystem) {
        return Response.ok(dbService.getDelivery(idSystem)).build();
    }

    //check is user delivery of current system
    @GET
    @Path("/isdelivery")
    public Response checkUserDelivery(@QueryParam("idsystem") Integer idSystem) {
        return Response.ok(permissionService.isCurrentUserDelivery(idSystem).toString()).build();
    }

    //check if this system enable for editing of current user
    @GET
    @Path("/isenable")
    public Response checkEnable(@QueryParam("idsystem") Integer idSystem,
                                @QueryParam("idtypechange") Integer idTypeChange) {
        return Response.ok(permissionService.isEnable(idSystem, idTypeChange).toString()).build();
    }

    //get change logs current system
    @GET
    @Path("/getlogs")
    public Response getLogs(@QueryParam("idsystem") Integer idSystem,
                            @QueryParam("idtypechange") Integer idTypeChange) {
        return Response.ok(dbService.getLogs(idSystem, idTypeChange)).build();
    }

    //get title stage for view on front
    @GET
    @Path("/stage/title")
    public Response getTitle(@QueryParam("namestage") String nameStage) {
        return Response.ok(dbService.getTitleStage(nameStage)).build();
    }

    //get label stage for view on front
    @GET
    @Path("/stage/label")
    public Response getLabel(@QueryParam("namestage") String nameStage) {
        return Response.ok(dbService.getLabelStage(nameStage)).build();
    }

    //get user by id
    @GET
    @Path("/getuser")
    public Response getUserById(@QueryParam("id") Integer idUser) {
        return Response.ok(dbService.getUserById(idUser)).build();
    }

    //export data to csv
    @GET
    @Path("/export")
    public Response exportCSV() {
        List<SystemAssignees> systemAssigneesList = dbService.getListSystemsUserDelivery();
        List<Stage> stages = dbService.getAllStages();
        return Response.ok(fileService.createFile(systemAssigneesList, stages)).build();
    }

    //get count systems, where current user is delivery
    @GET
    @Path("/countmysystems")
    public Response countMySystems() {
        return Response.ok(dbService.getCountSystemDelivery().toString()).build();
    }

    //Was current user author of last change?
    @GET
    @Path("/checklastlog")
    public Response checkLastLog(@QueryParam("idsystem") Integer idSystem,
                                 @QueryParam("idtypechange") Integer idTypeChange) {
        return Response.ok(dbService.checkLastLog(idSystem, idTypeChange).toString()).build();
    }

    //get list of last logs
    @GET
    @Path("/getlastlogs")
    public Response getLastLog(@QueryParam("idsystem") Integer idSystem,
                               @QueryParam("idtypechange") Integer idTypeChange) {
        return Response.ok(dbService.getLastLogs(idSystem, idTypeChange).toString()).build();
    }

    //check permission for bulk edit
    @GET
    @Path("/isenablebulk")
    public Response checkEnableBulk() {
        return Response.ok(permissionService.isEnableBulk().toString()).build();
    }

    @GET
    @Path("/getmysystems")
    public Response getMySystems() {
        Boolean isSuperUser = permissionService.isCurrentUserSuperUser();
        Boolean isDelivery = permissionService.isCurrentUserDelivery();
        return Response.ok(dbService.getSystemsOfUser(isSuperUser, isDelivery).toString()).build();
    }

    //get assignees in multi edit
    @GET
    @Path("/getassigneesmulti")
    public Response getAssigneesMulti(@QueryParam("idSystems") String idSystems,
                                      @QueryParam("idTypeChanges") String idTypeChanges) {
        return Response.ok(dbService.getAssigneesMulti(idSystems, idTypeChanges).toString()).build();
    }

    //post changes in multi edit
    @POST
    @Path("/postmulti")
    public void postmulti(@Context HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject(request.getParameterMap());
        dbService.updateDB(jsonObject);
    }

    @GET
    @Path("/getcad")
    public Response getCountActiveDelivery(@QueryParam("idSystems") String idSystemsString)  {
        return Response.ok(dbService.getCountActiveDelivery(idSystemsString).toString()).build();
    }
}
