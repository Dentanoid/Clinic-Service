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
import org.example.DatabaseManagement.Schemas.Query.NearbyQuerySchema;
import org.example.Utils.Entry;
import org.example.Utils.Utils;

import com.mongodb.client.FindIterable;

public class NearbyClinics implements Query {
    private static Integer n;
    private static PriorityQueue<Entry> pq; // Max heap priority que with key-value pairs contained in customized class 'Entry'
    private static double[] userCoordinates;

    public NearbyClinics(String topic, String payload) {
        executeRequestedOperation(topic, payload);
    }

    @Override
    public void queryDatabase(String payload) {
        readPayloadAttributes(payload);

        pq = new PriorityQueue<Entry>(Collections.reverseOrder());
        iterateThroughClinics(userCoordinates);
    }

    // Linear search through every DB-Instance reading 'location' values and comparing them to the user's global coordinates
    private void iterateThroughClinics(double[] userCoordinates) {
        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();
        
        while (it.hasNext()) {
            Document currentClinic = it.next();
            double[] currentClinicCoordinates = Utils.convertStringToDoubleArray(currentClinic.get("position").toString().split(","));

            double distanceInKm = Utils.haversineFormula(userCoordinates, currentClinicCoordinates);
            addPQElement(new Entry(distanceInKm, currentClinic));
        }
    }

    private void addPQElement(Entry element) {
        pq.add(element);

        if (pq.size() > n) { // Delete element with maximum distance to conform to given quantity-constraint of clinics to return
            pq.poll();
        }
    }

    private void readPayloadAttributes(String payload) {
        getNumberOfClinicsToQuery(payload);
        getUserPosition(payload);
    }

    private void getNumberOfClinicsToQuery(String payload) {
       int requestedPayloadNumber = Integer.parseInt(PayloadParser.getAttributeFromPayload(payload, "nearby_clinics_number", new NearbyQuerySchema()).toString());
       int numberOfExistingClinics = (int)DatabaseManager.clinicsCollection.countDocuments();

       n = Math.min(requestedPayloadNumber, numberOfExistingClinics);
    }

    private void getUserPosition(String payload) {
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "user_position", new NearbyQuerySchema());
        userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
    }

    private Document[] retrieveClosestClinics() {
        Document[] closestClinics = new Document[n];
        Iterator<Entry> iterator = pq.iterator();

        Integer i = 0;
        while (iterator.hasNext()) {
            closestClinics[n - i - 1] = pq.poll().getValue();
            i++;
        }

        return closestClinics;
    }

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "pub/query/map/nearby";

        if (topic.contains("map")) {
            queryDatabase(payload);
        }

        MqttMain.subscriptionManagers.get(topic).publishMessage(publishTopic, Arrays.toString(retrieveClosestClinics()));
    }
}
