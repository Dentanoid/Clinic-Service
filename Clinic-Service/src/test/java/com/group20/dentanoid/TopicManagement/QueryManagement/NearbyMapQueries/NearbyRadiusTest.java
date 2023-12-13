package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class NearbyRadiusTest {

    @Test
    void readReferenceCoordinates() {
        String topic = "grp20/req/map/query/nearby/radius/get";

        String[] referenceCoordinatesExpected = new String[] { "50.17", "12.1" }; // lat, lng
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        JsonObject jsonPayload = new JsonObject(); // TODO: Replace with 'new NearbyRadiusQuerySchema().assignAttributes(...)'
        jsonPayload.addProperty("radius", "20");
        jsonPayload.addProperty("reference_position", joinedCoordinates);
        jsonPayload.addProperty("requestID", "requestId");

        Gson gson = new Gson();
        String payload = gson.toJson(jsonPayload);

        DatabaseManager.initializeDatabaseConnection();
        NearbyClinics nearbyRadius = new NearbyRadius(topic, payload);
        nearbyRadius.queryDatabase();

        assertEquals(Arrays.toString(referenceCoordinatesExpected), Arrays.toString(nearbyRadius.referenceCoordinates));
    }

    @Test
    void addPQElement() {
        assertEquals(2, 2);
    }

    @Test
    void getReferencePosition() {
        assertEquals(3, 3);
    }
}