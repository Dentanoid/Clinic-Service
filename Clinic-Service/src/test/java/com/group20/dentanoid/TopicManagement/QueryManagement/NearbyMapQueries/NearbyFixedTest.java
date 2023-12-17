package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import org.junit.jupiter.api.Test;

import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class NearbyFixedTest {
    @Test
    void priorityQueMaxHeapFixedUnitTest() {
        
        // Define data variables
        String topic = "grp20/req/map/query/nearby/fixed/get";
        String[] referenceCoordinatesExpected = new String[] { "10.17", "19.1" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        DatabaseManager.initializeDatabaseConnection();

        long requestNr = 34; // The quantity of requested clinics the user wants to query on the map
        long numberOfRegisteredClinics = DatabaseManager.clinicsCollection.countDocuments();
        long expectedQuantityOfClinics = requestNr > numberOfRegisteredClinics ? numberOfRegisteredClinics : requestNr;

        // Create payload
        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("nearby_clinics_number", Long.toString(expectedQuantityOfClinics));
            put("reference_position", joinedCoordinates);
            put("requestID", "requestID46");
        }});

        // Test code
        NearbyClinics nearbyFixedNumber = new NearbyFixed(topic, payload);
        nearbyFixedNumber.queryDatabase();
        assertEquals(expectedQuantityOfClinics, nearbyFixedNumber.pq.size());
    }
}