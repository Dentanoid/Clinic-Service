package org.example.TopicManagement.QueryManagement;

import java.util.Arrays;
import java.util.PriorityQueue;

import org.bson.Document;
import org.example.DatabaseManagement.PayloadParser;
import org.example.DatabaseManagement.Schemas.EmploymentSchema;
import org.example.DatabaseManagement.Schemas.NearbyQuerySchema;

public class NearbyClinics implements Query {

    private PriorityQueue<Integer> pq; // Contains distances to currentPos

    public NearbyClinics(String topic, String payload) {
        executeRequestedOperation(topic, payload);
    }

    @Override
    public void queryDatabase(String payload) {
        Object n = PayloadParser.getAttributeFromPayload(payload, "nearby_clinics_number", new NearbyQuerySchema());
        System.out.println("Length of priority que: " + n.toString());

        Object user_position = PayloadParser.getAttributeFromPayload(payload, "user_position", new NearbyQuerySchema());
        String[] userCoordinates = user_position.toString().split(",");
        System.out.println("User position: " + Arrays.toString(userCoordinates));

        // pq = new Query with length N

        // TODO:
        // Linear search through every DB-Instance reading 'location' values
        // Perform a mathematical formula on each location-value to convert them into 'distance_value'
        // Add each distance-value in priorityque 'pq' that has a constrained length of N
    }

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "";

        if (topic.contains("map")) {
            queryDatabase(payload);
        }
    }
}
