package ru.pestov.alexey.plugins.spring.rests;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.entity.FormParam;
import ru.pestov.alexey.plugins.spring.service.JSONService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.ParameterizedType;

@Named
@Path("/systems")
public class SystemRest {

    private final JSONService jsonService;

    @Inject
    public SystemRest(final JSONService jsonService) {
        this.jsonService = jsonService;
    }

    @GET
    @Path("/get")
    public Response get()
    {
        JSONObject jsonObject = jsonService.getJsonObject();
        return Response.ok(jsonObject.toString()).build();
    }

    @GET
    @Path("/getlistsystems")
    public Response getListSystems()    {
        return Response.ok(jsonService.getSystems().toString()).build();
    }

    @GET
    @Path("/getsystem")
    public Response getSystem(@Context HttpServletRequest httpServletRequest, @QueryParam("namesystem") String nameSystem) {
        return Response.ok(jsonService.getSystem(nameSystem).toString()).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/post")
    public Response post(FormParam formParam)  {
        //todo не работает
        return Response.ok(formParam.getTypechange()).build();
    }
}
