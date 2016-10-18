package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

class Validators {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static HashMap getValidAttributesInput(String body) throws IOException {
        HashMap input;

        if ( body == null) {
            throw Exceptions.invalidInput;
        }

        try {
            input = objectMapper.readValue(body, HashMap.class);
        } catch (Exceptions e) {
            throw Exceptions.invalidInput;
        }

        if (!input.containsKey("attributes")) {
            throw Exceptions.missingAttributesParameter;
        }  else {
            Object attributes = input.get("attributes");
            if (attributes instanceof List<?>) {
                if (((List) attributes).isEmpty()) {
                    throw Exceptions.emptyAttributesParameter;
                }
            } else {
                throw Exceptions.invalidAttributesParameter;
            }
        }
        return input;
    }

    static HashMap getValidCityAndAttributesInput(String body) throws IOException {
        HashMap input = getValidAttributesInput(body);
        if (!input.containsKey("geoname_id")) {
            throw Exceptions.missingGeoNameIdParameter;
        }
        return input;
    }
    static HashMap getValidListingInput(String body) throws IOException {
        HashMap input = getValidCityAndAttributesInput(body);
        if (!input.containsKey("id")) {
            throw Exceptions.missingIdParameter;
        }
        return input;
    }
}
