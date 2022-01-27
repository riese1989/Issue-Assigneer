package ru.pestov.alexey.plugins.spring.rest;

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


    @Inject
    public SystemRest(final JSONService jsonService,
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
        return Response.ok(systemService.getSystems().toString()).build();
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
    public Response post(@FormParam("system") String system,
                         @FormParam("typechange") String typeChange,
                         @FormParam("stage1") String stage1,
                         @FormParam("stage21") String stage21,
                         @FormParam("stage22") String stage22,
                         @FormParam("stage23") String stage23,
                         @FormParam("stage3") String stage3,
                         @FormParam("authorize") String authorize,
                         @FormParam("active") String active) throws Exception {
        Param param = new Param(system, typeChange, stage1, stage21, stage22, stage23, stage3, authorize, active);
        jsonService.updateJsonObject(param);
        issueAssigneerWebworkAction.setParams(param);
        return Response.ok(issueAssigneerWebworkAction.doSave()).build();
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
