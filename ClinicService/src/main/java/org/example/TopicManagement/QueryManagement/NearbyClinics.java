package org.example.TopicManagement.QueryManagement;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

import org.bson.Document;
import org.example.DatabaseManagement.DatabaseManager;
import org.example.DatabaseManagement.PayloadParser;
import org.example.DatabaseManagement.Schemas.EmploymentSchema;
import org.example.DatabaseManagement.Schemas.NearbyQuerySchema;
import org.example.Utils.Entry;
import org.example.Utils.Utils;

import com.mongodb.client.FindIterable;

public class NearbyClinics implements Query {
    private static Integer n;
    // private static PriorityQueue<Double, Document> pq; // Max heap priority que with key-value pairs contained in customized class 'Entry'
    private static PriorityQueue<Entry> pq;

    public NearbyClinics(String topic, String payload) {
        executeRequestedOperation(topic, payload);
    }

    @Override
    public void queryDatabase(String payload) {
        n = Integer.parseInt(PayloadParser.getAttributeFromPayload(payload, "nearby_clinics_number", new NearbyQuerySchema()).toString());
        System.out.println("Length of priority que: " + n.toString());

        Object user_position = PayloadParser.getAttributeFromPayload(payload, "user_position", new NearbyQuerySchema());
        String[] userCoordinatesString = user_position.toString().split(",");
        
        double[] userCoordinates = Utils.convertStringToDoubleArray(userCoordinatesString);
        System.out.println("User position: " + Arrays.toString(userCoordinates));

        // Formula Accuracy Test:
        double[] clinicTempPosition = new double[2];
        clinicTempPosition[0] = 57.78392080;
        clinicTempPosition[1] = 12.09125720;

        double distanceInKm = Utils.haversineFormula(userCoordinates, clinicTempPosition);
        System.out.println("Distance in km: " + distanceInKm);

        // Haversine Formula on priority queue test:
        Document myDocTest = new Document().append("attr1", 10);
        pq = new PriorityQueue<Entry>(Collections.reverseOrder());
        pq.add(new Entry(distanceInKm, myDocTest));

        System.out.println("PQ0: " + pq);
        System.out.println("PQ1: " + pq.toString());
        System.out.println("PQ2: " + Arrays.toString(pq.toArray()));


        // Linear search through every DB-Instance reading 'location' values
        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();
        while (it.hasNext()) {
            Document currentClinic = it.next();
            System.out.println(currentClinic.get("position"));
            System.out.println(currentClinic.toJson());
        }

        // TODO:
        // 1) Decide on priority queue datatype format (Integer, <String, Document>, ...)
        // 2) Perform a mathematical formula on each location-value to convert them into 'distance_value'
        // 3) Add each distance-value in priorityque 'pq' that has a constrained length of N
        // 4) Connect with other components in System
        // 5) Practice for tomorrow's presentation
    }

    /*
    public void addPQElement(double element) {
        pq.add(element);

        if (pq.size() > n) { // Delete element with maximum distance
            pq.poll(); // TODO: Research poll()
        }
    }
    */

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "";

        if (topic.contains("map")) {
            queryDatabase(payload);
        }
    }
}
