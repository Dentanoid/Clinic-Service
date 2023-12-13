package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import org.junit.jupiter.api.Test;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.Utils.Utils;

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
    void priorityQueMaxHeapRadiusUnitTest() {
        
        // Define data variables
        String topic = "grp20/req/map/query/nearby/radius/get";
        String[] referenceCoordinatesExpected = new String[] { "19.12321", "89.129947" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        DatabaseManager.initializeDatabaseConnection();

        long quantityLimit = NearbyRadius.getQueryLimit();
        long expectedNumberOfClinicsReturned = Math.min(quantityLimit, DatabaseManager.clinicsCollection.countDocuments());

        // Create payload
        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("radius", Double.toString(Utils.earthCircumference)); // Set the radius to the size at which every clinic in the database is returned
            put("reference_position", joinedCoordinates);
            put("requestID", "requestId");
        }});

        // Test code
        NearbyClinics nearbyRadius = new NearbyRadius(topic, payload);
        nearbyRadius.queryDatabase();

        assertEquals(expectedNumberOfClinicsReturned, nearbyRadius.pq.size());
    }
}