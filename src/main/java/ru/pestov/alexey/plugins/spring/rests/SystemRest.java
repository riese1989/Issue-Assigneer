package ru.pestov.alexey.plugins.spring.rests;

import com.atlassian.jira.issue.fields.rest.json.beans.WorklogJsonBean;
import com.google.common.collect.Lists;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import ru.pestov.alexey.plugins.spring.entity.SystemCAB;
import ru.pestov.alexey.plugins.spring.service.JSONService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import ru.pestov.alexey.plugins.spring.service.SystemService;

@Path("/systems")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class SystemRest {

  private SystemService systemService;

  @Inject
  public SystemRest (SystemService systemService) {
    this.systemService = systemService;
  }

  @GET
  @Path("/get/allsystems")
  public Response get() {
    GenericEntity<List<SystemCAB>> entity =
        new GenericEntity<List<SystemCAB>>(Lists.newArrayList(systemService.getSystems())) {};
    return Response.ok(entity).build();
  }

  @GET
  @Path("/get")
  public Response getSystem(@Context HttpServletRequest request)  {
    String nameSystem = request.getParameter("system");
    return Response.ok(systemService.getSystem(nameSystem)).build();
  }

  @POST
  @Path("/update")
  public Response updateSystem(@Context HttpServletRequest request)   {
    systemService.updateSystem(request);
    return Response.ok().build();
  }
}

