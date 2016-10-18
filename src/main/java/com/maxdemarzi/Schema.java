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

@Path("/schema")
public class Schema {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("/create")
    public Response create(@Context GraphDatabaseService db) throws IOException {
        ArrayList<String> results = new ArrayList<>();

        try (Transaction tx = db.beginTx()) {
            org.neo4j.graphdb.schema.Schema schema = db.schema();
            if (!schema.getConstraints(Labels.City).iterator().hasNext()) {
                schema.constraintFor(Labels.City)
                        .assertPropertyIsUnique("geoname_id")
                        .create();
                schema.indexFor(Labels.City)
                        .on("name")
                        .create();
                tx.success();
                results.add("(:City {geoname_id}) constraint created");
                results.add("(:City {name}) index created");
            }
        }

        results.add("Schema Created");

        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }
}
