package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class NearbyRadiusTest {

    @Test
    void readReferenceCoordinatesUnitTest() {
        // Data variables
        String topic = "grp20/req/map/query/nearby/radius/get";
        String[] referenceCoordinatesExpected = new String[] { "50.17", "12.1" }; // lat, lng
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        // Create JSON payload
        JsonObject jsonPayload = new JsonObject(); // TODO: Replace with 'new NearbyRadiusQuerySchema().assignAttributes(...)'
        jsonPayload.addProperty("radius", "20");
        jsonPayload.addProperty("reference_position", joinedCoordinates);
        jsonPayload.addProperty("requestID", "requestId");

        Gson gson = new Gson();
        String payload = gson.toJson(jsonPayload);

        // Test code
        DatabaseManager.initializeDatabaseConnection();
        NearbyClinics nearbyRadius = new NearbyRadius(topic, payload);
        nearbyRadius.queryDatabase();

        assertEquals(Arrays.toString(referenceCoordinatesExpected), Arrays.toString(nearbyRadius.referenceCoordinates));
    }

    @Test
    void priorityQueMaxHeapUnitTest() {
        
        // Data variables
        String topic = "grp20/req/map/query/nearby/fixed/get";
        String[] referenceCoordinatesExpected = new String[] { "10.17", "19.1" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        DatabaseManager.initializeDatabaseConnection();

        long requestNr = 34; // The quantity of requested clinics the user wants to query on the map
        long numberOfRegisteredClinics = DatabaseManager.clinicsCollection.countDocuments();
        long expectedQuantityOfClinics = requestNr > numberOfRegisteredClinics ? numberOfRegisteredClinics : requestNr;

        // Create JSON payload
        JsonObject jsonPayload = new JsonObject(); // TODO: Replace with 'new NearbyRadiusQuerySchema().assignAttributes(...)' OR createJSONPayload() in PayloadParser.java
        jsonPayload.addProperty("nearby_clinics_number", Long.toString(expectedQuantityOfClinics));
        jsonPayload.addProperty("reference_position", joinedCoordinates);
        jsonPayload.addProperty("requestID", "requestId");

        Gson gson = new Gson();
        String payload = gson.toJson(jsonPayload);

        // Test code
        NearbyClinics nearbyFixedNumber = new NearbyFixed(topic, payload);
        nearbyFixedNumber.queryDatabase();

        System.out.println(nearbyFixedNumber.pq.size());

        assertEquals(expectedQuantityOfClinics, nearbyFixedNumber.pq.size());
    }
}