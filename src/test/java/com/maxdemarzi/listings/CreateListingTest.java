package com.maxdemarzi.listings;

import com.maxdemarzi.Service;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateListingTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withExtension("/v1", Service.class);

    @Test
    @Ignore
    public void shouldCreateListing() {
        HTTP.POST(neo4j.httpURI().resolve("/v1/schema/create").toString());

        ArrayList<String> attributes = new ArrayList<String>() {{
            add("ruby");
            add("java");
            add("neo4j");
        }};

        HashMap input = new HashMap<String,Object>() {{
            put("id","1st");
            put("geoname_id","chi");
            put("attributes",attributes);
        }};

        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/v1/listings/create").toString(), input);

        ArrayList actual = response.content();
        Assert.assertEquals(expected, actual);
    }

    private static final ArrayList expected = new ArrayList<Object>() {{
        add(new HashMap<String,Object>() {{
            put("end_node",2);
            put("start_node",1);
            put("path",4);
            put("rel_type","IN_PATH");
            put("id",0);
        }});
    }};

    public static final String MODEL_STATEMENT =
            "CREATE (c1:City {geoname_id:'chi', name:'Chicago'})" +
            "CREATE (a1:Attribute {name:'ruby'})" +
            "CREATE (a2:Attribute {name:'java'})" +
            "CREATE (a3:Attribute {name:'neo4j'})";
}
