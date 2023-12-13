package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;

class NearbyRadiusTest {
    /*
      Ensures that the functionality for reading the gloobal coordinates
      defined in the JSON payload is behaving as expected.
      (See 'reference_Position' in NearbyRadiusQuerySchema.java)
     */
    @Test
    void readReferenceCoordinatesUnitTest() {

        // Define data variables
        String topic = "grp20/req/map/query/nearby/radius/get";
        String[] referenceCoordinatesExpected = new String[] { "50.17", "12.1" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        // Create payload
        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{ // NOTE: Refactor further - Input NearbyRadiusSchema in parameters
            put("radius", "20");
            put("reference_position", joinedCoordinates);
            put("requestID", "requestId");
        }});

        // Test code
        DatabaseManager.initializeDatabaseConnection();
        NearbyClinics nearbyRadius = new NearbyRadius(topic, payload);
        nearbyRadius.queryDatabase();

        assertEquals(Arrays.toString(referenceCoordinatesExpected), Arrays.toString(nearbyRadius.referenceCoordinates));
    }

    @Test
    void priorityQueMaxHeapUnitTest() {
        
        // Define data variables
        String topic = "grp20/req/map/query/nearby/fixed/get";
        String[] referenceCoordinatesExpected = new String[] { "10.17", "19.1" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        DatabaseManager.initializeDatabaseConnection();

        long requestNr = 34; // The quantity of requested clinics the user wants to query on the map
        long numberOfRegisteredClinics = DatabaseManager.clinicsCollection.countDocuments();
        long expectedQuantityOfClinics = requestNr > numberOfRegisteredClinics ? numberOfRegisteredClinics : requestNr;

        // Create payload
        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{ // NOTE: Refactor further - Input NearbyRadiusSchema in parameters
            put("nearby_clinics_number", Long.toString(expectedQuantityOfClinics));
            put("reference_position", joinedCoordinates);
            put("requestID", "requestId");
        }});

        // Test code
        NearbyClinics nearbyFixedNumber = new NearbyFixed(topic, payload);
        nearbyFixedNumber.queryDatabase();
        assertEquals(expectedQuantityOfClinics, nearbyFixedNumber.pq.size());
    }
}