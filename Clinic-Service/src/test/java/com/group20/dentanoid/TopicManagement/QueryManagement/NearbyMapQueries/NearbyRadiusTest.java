package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import org.junit.jupiter.api.Test;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.TopicManagement.MapManagement.Nearby.NearbyClinics;
import com.group20.dentanoid.TopicManagement.MapManagement.Nearby.NearbyRadius;
import com.group20.dentanoid.Utils.Utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;

class NearbyRadiusTest {
    /*
      Ensures that the functionality for reading the gloobal coordinates
      defined in the JSON payload is behaving as expected.
     */
    @Test
    void readReferenceCoordinatesUnitTest() {
        String topic = "grp20/req/map/query/nearby/radius/get";
        String[] referenceCoordinatesExpected = new String[] { "50.17", "12.1" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{ // NOTE: Refactor further - Input NearbyRadiusSchema in parameters
            put("radius", "20");
            put("reference_position", joinedCoordinates);
            put("requestID", "requestId");
        }});

        DatabaseManager.initializeDatabaseConnection();
        NearbyClinics nearbyRadius = new NearbyRadius(topic, payload);
        nearbyRadius.queryDatabase();

        assertEquals(Arrays.toString(referenceCoordinatesExpected), Arrays.toString(nearbyRadius.referenceCoordinates));
    }

    /*
        Sets the radius to the circumference of the Earth and compares
        the retrieved number of clinics to the existing quantity in the DB
        as well as the defined upper limit in NearbyRadius.java
     */
    @Test
    void priorityQueMaxHeapRadiusUnitTest() {
        
        // Define data variables
        String topic = "grp20/req/map/query/nearby/radius/get";
        String[] referenceCoordinatesExpected = new String[] { "19.12321", "89.129947" };
        String joinedCoordinates = String.join(",", referenceCoordinatesExpected);

        DatabaseManager.initializeDatabaseConnection();

        long quantityLimit = NearbyRadius.getQueryLimit();
        long expectedNumberOfClinicsReturned = Math.min(quantityLimit, DatabaseManager.clinicsCollection.countDocuments());

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("radius", Double.toString(Utils.earthCircumference));
            put("reference_position", joinedCoordinates);
            put("requestID", "requestId");
        }});

        NearbyClinics nearbyRadius = new NearbyRadius(topic, payload);
        nearbyRadius.queryDatabase();

        assertEquals(expectedNumberOfClinicsReturned, nearbyRadius.pq.size());
    }
}