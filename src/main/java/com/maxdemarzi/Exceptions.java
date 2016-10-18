package com.maxdemarzi;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class Exceptions extends WebApplicationException {
    private Exceptions(int code, String error)  {
        super(new Throwable(error), Response.status(code)
                .entity("{\"error\":\"" + error + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build());

    }

    static Exceptions invalidInput = new Exceptions(400, "Invalid Input");

    static Exceptions missingAttributesParameter = new Exceptions(400, "Missing attributes Parameter.");
    static Exceptions emptyAttributesParameter = new Exceptions(400, "Empty attributes Parameter.");
    static Exceptions invalidAttributesParameter = new Exceptions(400, "Invalid attributes Parameter.");

    static Exceptions missingGeoNameIdParameter = new Exceptions(400, "Missing geoname_id Parameter.");
    static Exceptions missingIdParameter = new Exceptions(400, "Missing id Parameter.");

    static Exceptions listingAlreadyExists = new Exceptions(400, "Listing already exists.");
    static Exceptions cityNotFound = new Exceptions(400, "City nout found.");

}