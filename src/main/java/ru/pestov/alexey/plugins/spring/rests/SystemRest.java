package ru.pestov.alexey.plugins.spring.rests;

import com.atlassian.jira.issue.fields.rest.json.beans.WorklogJsonBean;
import ru.pestov.alexey.plugins.spring.service.JSONService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/systems")
public class SystemRest {

    public SystemRest() {

    }

    @GET
    @Path("/get1")
    public Response get1()
    {
        return Response.ok().build();
    }

    @POST
    @Path("/post")
    public Response post()  {
        return Response.ok().build();
    }

    @POST
    @Path("/uploaddata")
    public Response postUploadData()    {
        return Response.ok().build();
    }

    @GET
    @Path("/uploaddata")
    public Response getUploadData()    {
        return Response.ok().build();
    }
}
