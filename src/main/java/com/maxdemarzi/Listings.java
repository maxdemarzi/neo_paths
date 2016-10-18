package com.maxdemarzi;


import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("/listings")
public class Listings {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Path("/create")
    public Response create(String body, @Context GraphDatabaseService db) throws IOException {

        HashMap input = Validators.getValidListingInput(body);
        ArrayList<Node> attributes = new ArrayList<>();
        ArrayList<Relationship> relationships = new ArrayList<>();
        ArrayList results = new ArrayList();
        try (Transaction tx = db.beginTx()) {
            // Create Listing
            Node listing  = db.findNode(Labels.Listing, "id", input.get("id"));
            if (listing != null) {
                throw Exceptions.listingAlreadyExists;
            }

            listing = db.createNode(Labels.Listing);
            listing.setProperty("id", input.get("id"));

            // Get City
            Node city = db.findNode(Labels.City, "geoname_id", input.get("geoname_id"));
            if (city == null) {
                throw Exceptions.cityNotFound;
            }

            // Get Attributes
            for(String name : (Collection<String>) input.get("attributes")) {
                Node attribute = db.findNode(Labels.Attribute, "name", name);
                if (attribute == null) {
                    attribute = db.createNode(Labels.Attribute);
                    attribute.setProperty("name", name);
                }
                attributes.add(attribute);
            }

            Collections.sort(attributes, (o1, o2) -> ((Long)o1.getId()).compareTo(o2.getId()));

            for (int i = 0; i < attributes.size() - 1; i ++) {
                Relationship relationship = attributes.get(i).createRelationshipTo(attributes.get(i+1), RelationshipTypes.IN_PATH);
                relationship.setProperty("path", listing.getId());
                relationships.add(relationship);
            }

            // Connect City and Listing via Path
            Relationship city2path = city.createRelationshipTo(attributes.get(0),RelationshipTypes.IN_PATH);
            city2path.setProperty("path", listing.getId());
            relationships.add(city2path);
            Relationship path2listing = attributes.get(attributes.size()-1).createRelationshipTo(listing,RelationshipTypes.IN_LISTING);
            relationships.add(path2listing);

            for ( Relationship relationship : relationships )
            {
                Map<String, Object> relMap = new HashMap<>();
                relMap.put("id", relationship.getId());
                relMap.put("rel_type", relationship.getType().name());
                relMap.put("start_node", relationship.getStartNode().getId());
                relMap.put("end_node", relationship.getEndNode().getId());
                relMap.put("path", relationship.getProperty("path", ""));
                results.add(relMap);
            }

            tx.success();
        }
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }
}
