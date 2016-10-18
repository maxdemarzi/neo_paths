package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/search")
public class Search {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("/local")
    public Response localSearch(String body, @Context GraphDatabaseService db) throws IOException {

        HashMap input = Validators.getValidCityAndAttributesInput(body);
        ArrayList results = new ArrayList();
        try (Transaction tx = db.beginTx()) {

        }
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    @POST
    @Path("/global")
    public Response globalSearch(String body, @Context GraphDatabaseService db) throws IOException {

        HashMap input = Validators.getValidAttributesInput(body);
        ArrayList results = new ArrayList();
        try (Transaction tx = db.beginTx()) {

        }
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

}