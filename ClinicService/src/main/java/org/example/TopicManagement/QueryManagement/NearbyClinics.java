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
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "user_position", new NearbyQuerySchema());
        
        // TODO: Refactor the two lines below into a general method
        String[] userCoordinatesString = user_position.toString().split(","); 
        double[] userCoordinates = Utils.convertStringToDoubleArray(userCoordinatesString);


        System.out.println("In NearbyClinics.java");
        pq = new PriorityQueue<Entry>(Collections.reverseOrder());

        // Linear search through every DB-Instance reading 'location' values
        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();
        while (it.hasNext()) {
            Document currentClinic = it.next();
            System.out.println(currentClinic.get("position"));

            String[] currentClinicCoordinatesString = currentClinic.get("position").toString().split(",");
            double[] currentClinicCoordinates = Utils.convertStringToDoubleArray(currentClinicCoordinatesString);

            double distanceInKm = Utils.haversineFormula(userCoordinates, currentClinicCoordinates);
            addPQElement(new Entry(distanceInKm, currentClinic));
        }

        System.out.println("PQ0: " + pq);
        // System.out.println("PQ1: " + pq.toString());
        // System.out.println("PQ2: " + Arrays.toString(pq.toArray()));

        /*
        // TEMPORARY CHECK FOR DEVELOPERS:
        Iterator<Entry> iterator = pq.iterator();
        Integer counter = 0;
        while (iterator.hasNext()) { // Expected behaviour: Print in descending order from max to min
            counter++;
            System.out.println("Iteration " + counter + ": " + pq.poll());
        }
        */
    }

    public void addPQElement(Entry element) {
        pq.add(element);

        if (pq.size() > n) { // Delete element with maximum distance
            pq.poll(); // TODO: Research poll()
        }
    }

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "";

        if (topic.contains("map")) {
            queryDatabase(payload);
        }
    }
}
