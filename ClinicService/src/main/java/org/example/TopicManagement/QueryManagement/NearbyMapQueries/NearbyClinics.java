package org.example.TopicManagement.QueryManagement.NearbyMapQueries;

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
import org.example.TopicManagement.QueryManagement.Query;
import org.example.Utils.Entry;
import org.example.Utils.Utils;

import com.mongodb.client.FindIterable;

public class NearbyClinics extends NearbyQuery { // Previous: implements Query
    public PriorityQueue<Entry> pq; // Max heap priority que with key-value pairs contained in customized class 'Entry'
    public double[] userCoordinates; // Change to 'referenceCoordinates'

    public NearbyClinics(String topic, String payload) {
    }

    @Override
    public void queryDatabase(String payload) {
        readPayloadAttributes(payload);
        iterateThroughClinics(userCoordinates);
    }

    // Linear search through every DB-Instance reading 'location' values and comparing them to the user's global coordinates
    public void iterateThroughClinics(double[] userCoordinates) {
        pq = new PriorityQueue<Entry>(Collections.reverseOrder());

        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();

        while (it.hasNext()) {
            Document currentClinic = it.next();
            double[] currentClinicCoordinates = Utils.convertStringToDoubleArray(currentClinic.get("position").toString().split(","));

            double distanceInKm = Utils.haversineFormula(userCoordinates, currentClinicCoordinates);
            addPQElement(new Entry(distanceInKm, currentClinic));
        }
    }

    public void readPayloadAttributes(String payload) {
        getNumberOfClinicsToQuery(payload);
        getUserPosition(payload);
    }

    private Document[] retrieveClosestClinics(int n, NearbyClinics queryKey) {
        Document[] closestClinics = new Document[n];
        Iterator<Entry> iterator = queryKey.pq.iterator();

        Integer i = 0;
        while (iterator.hasNext()) {
            closestClinics[n - i - 1] = queryKey.pq.poll().getValue();
            i++;
        }

        return closestClinics;
    }

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "pub/query/map/nearby";
        NearbyClinics queryKey; // Current query is used as a key to access the object's corresponding priority queue

        // TODO: Make public static attribute of topic, split it and compare it in O(1) rather than O(n) checks with '.contains()'
        if (topic.contains("fixed")) {
            queryKey = new NearbyFixed(publishTopic, payload);   
        }
        else {
            queryKey = new NearbyRadius(publishTopic, payload);
        }

        queryKey.queryDatabase(payload);
        MqttMain.subscriptionManagers.get(topic).publishMessage(publishTopic, Arrays.toString(retrieveClosestClinics(queryKey.getN(), queryKey)));
    }
}
