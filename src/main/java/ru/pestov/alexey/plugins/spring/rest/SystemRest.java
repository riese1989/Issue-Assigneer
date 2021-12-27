package ru.pestov.alexey.plugins.spring.rest;

import org.json.simple.JSONObject;
import ru.pestov.alexey.plugins.spring.entity.Param;
import ru.pestov.alexey.plugins.spring.service.JSONService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Path("/post")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response post(Param param)  {
        //todo не работает
        return Response.ok(param.getStep1()).build();
    }

    @POST
    @Path("/post2")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_JSON})
    public Response post2(@FormParam("step1") String step1)  {
        //todo не работает
        return Response.ok(step1).build();
    }
}
