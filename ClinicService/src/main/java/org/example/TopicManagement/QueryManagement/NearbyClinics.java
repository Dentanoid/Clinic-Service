package org.example.TopicManagement.QueryManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.print.Doc;

import org.bson.Document;
import org.example.MqttMain;
import org.example.DatabaseManagement.DatabaseManager;
import org.example.DatabaseManagement.PayloadParser;
import org.example.DatabaseManagement.Schemas.Clinic.EmploymentSchema;
import org.example.DatabaseManagement.Schemas.Query.NearbyFixedQuerySchema;
import org.example.DatabaseManagement.Schemas.Query.NearbyRadiusQuerySchema;
import org.example.Utils.Entry;
import org.example.Utils.Utils;

import com.mongodb.client.FindIterable;

public class NearbyClinics implements Query {
    private static Integer n;
    private static PriorityQueue<Entry> pq; // Max heap priority que with key-value pairs contained in customized class 'Entry'
    private static double[] userCoordinates;
    private static Integer maximumClinicsInQuery = 20;
    private static Double radius = -1.0;
    private static Integer numberOfFoundClinics;

    public NearbyClinics(String topic, String payload) {
        numberOfFoundClinics = 0;
        executeRequestedOperation(topic, payload);
    }

    @Override
    public void queryDatabase(String payload, String mode) {
        readPayloadAttributes(payload, mode);
        iterateThroughClinics(userCoordinates, mode);
    }

    // Linear search through every DB-Instance reading 'location' values and comparing them to the user's global coordinates
    private void iterateThroughClinics(double[] userCoordinates, String mode) {
        pq = new PriorityQueue<Entry>(Collections.reverseOrder());

        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();

        while (it.hasNext()) {
            Document currentClinic = it.next();
            double[] currentClinicCoordinates = Utils.convertStringToDoubleArray(currentClinic.get("position").toString().split(","));

            double distanceInKm = Utils.haversineFormula(userCoordinates, currentClinicCoordinates);
            addPQElement(new Entry(distanceInKm, currentClinic), mode);
        }
    }

    private void addPQElement(Entry element, String mode) {

        if (mode.equals("radius")) {
            System.out.println("compare " + element.getKey() + " AND " + radius);
            if (element.getKey() <= radius) { // Display clinic if it is within the requested radius
                pq.add(element);
                numberOfFoundClinics++;
            }
        }
        else {
            pq.add(element);
        }

        if (pq.size() > n) { // Delete element with maximum distance to conform to given quantity-constraint of clinics to return
            pq.poll();
        }
    }

    private void readPayloadAttributes(String payload, String mode) {
        getNumberOfClinicsToQuery(payload, mode);
        getUserPosition(payload, mode);

        if (mode.equals("radius")) {
            getRadius(payload);
        }

        System.out.println("A");
    }

    private void getNumberOfClinicsToQuery(String payload, String mode) {
       int requestedPayloadNumber;

       if (mode.equals("fixed")) {
        requestedPayloadNumber = Integer.parseInt(PayloadParser.getAttributeFromPayload(payload, "nearby_clinics_number", new NearbyFixedQuerySchema()).toString());
       }
       else {
        requestedPayloadNumber = maximumClinicsInQuery;
       }
       
       int numberOfExistingClinics = (int)DatabaseManager.clinicsCollection.countDocuments();
       n = Math.min(requestedPayloadNumber, numberOfExistingClinics);
    }

    private void getUserPosition(String payload, String mode) {
        // Fixed
        if (mode.equals("fixed")) {
            Object user_position = PayloadParser.getAttributeFromPayload(payload, "user_position", new NearbyFixedQuerySchema());
            System.out.println(user_position);
            userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
        }
        // Radius
        else {
            Object user_position = PayloadParser.getAttributeFromPayload(payload, "reference_position", new NearbyRadiusQuerySchema());
            System.out.println(user_position);
            userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
        }
    }

    private void getRadius(String payload) {
        radius = Double.parseDouble(PayloadParser.getAttributeFromPayload(payload, "radius", new NearbyRadiusQuerySchema()).toString());
    }

    private Document[] retrieveClosestClinics(String mode) {
        Integer arrSize = mode.equals("fixed") ? n : numberOfFoundClinics;

        Document[] closestClinics = new Document[arrSize];
        Iterator<Entry> iterator = pq.iterator();

        Integer i = 0;
        while (iterator.hasNext()) {
            closestClinics[arrSize - i - 1] = pq.poll().getValue();
            i++;
        }

        return closestClinics;
    }

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "pub/query/map/nearby";
        String mode = "fixed";

        // TODO: Make public static attribute of topic, split it and compare it in O(1) rather than O(n) checks with '.contains()'
        if (topic.contains("fixed")) { // VERSION 1: Fixed number
            queryDatabase(payload, "fixed");
        }
        else if (topic.contains("radius")) { // User sends a radius value instead of a fixed number
            queryDatabase(payload, "radius");
            mode = "radius";
        }

        MqttMain.subscriptionManagers.get(topic).publishMessage(publishTopic, Arrays.toString(retrieveClosestClinics(mode)));
    }
}
