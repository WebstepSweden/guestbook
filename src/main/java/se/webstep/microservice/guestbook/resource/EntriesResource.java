package se.webstep.microservice.guestbook.resource;

import com.google.common.collect.ImmutableMap;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.jersey.params.IntParam;
import se.webstep.microservice.guestbook.MicroServicesApplication;
import se.webstep.microservice.guestbook.core.Entry;
import se.webstep.microservice.guestbook.jdbi.EntryDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Api("Resource is handle Entries")
@Path("/guestbook/{guestbookId}/entries")
@Produces(MediaType.APPLICATION_JSON)
public class EntriesResource {

    private final MicroServicesApplication service;

    public EntriesResource(MicroServicesApplication service) {
        this.service = service;
    }

    @ApiOperation("Fetching all entries for given guestbook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Entry created")
    })
    @GET
    public Response getEntries(@ApiParam(value = "ID of guestbook", required = true)
                               @PathParam("guestbookId") IntParam guestbookId) {
        return Response.ok(ImmutableMap.of(
                "entries", service.getJdbi().onDemand(EntryDao.class).list(guestbookId.get())))
                .build();
    }

    @ApiOperation("Fetching all readable entries for given guestbook")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Entry created")
    })
    @GET
    @Path("/readable")
    public Response getReadable(@ApiParam(value = "ID of guestbook", required = true)
                                @PathParam("guestbookId") IntParam guestbookId) {
        return Response.ok(ImmutableMap.of(
                "entries", service.getJdbi().onDemand(EntryDao.class).list(guestbookId.get())
                        .stream()
                        .filter(s -> s.status == Entry.Status.READABLE)
                        .collect(Collectors.toList()))).build();
    }

    @ApiOperation("Fetching all unreadable entries for given guestbook")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Entry created")
    })
    @GET
    @Path("/un_readable")
    public Response getUnReadable(@ApiParam(value = "ID of guestbook", required = true)
                                  @PathParam("guestbookId") IntParam guestbookId) {
        return Response.ok(ImmutableMap.of(
                "entries", service.getJdbi().onDemand(EntryDao.class).list(guestbookId.get())
                        .stream()
                        .filter(s -> s.status == Entry.Status.UN_READABLE)
                        .collect(Collectors.toList()))).build();
    }
}
