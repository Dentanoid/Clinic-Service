package com.group20.dentanoid.Main.TopicManagement.QueryManagement.NearbyMapQueries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import org.bson.Document;

import com.group20.dentanoid.Main.MqttMain;
import com.group20.dentanoid.Main.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.Main.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.Main.DatabaseManagement.Schemas.CollectionSchema;
import com.group20.dentanoid.Main.DatabaseManagement.Schemas.Query.NearbyFixedQuerySchema;
import com.group20.dentanoid.Main.DatabaseManagement.Schemas.Query.NearbyRadiusQuerySchema;
import com.group20.dentanoid.Utils.Entry;
import com.group20.dentanoid.Utils.Utils;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;

public class NearbyClinics extends NearbyQuery {
    public PriorityQueue<Entry> pq; // Max heap priority que with key-value pairs contained in customized class 'Entry'
    
    /*
     Represents user's current position if the selected map mode in Patient Client is 'Nearby'
     Represents the searched position if the selected map mode is 'Search'
     */
    public double[] referenceCoordinates;

    public NearbyClinics(String topic, String payload) {
    }

    @Override
    public void queryDatabase(String payload) {
        readPayloadAttributes(payload); 
        iterateThroughClinics(referenceCoordinates); // BUG HERE
    }

    // Linear search through every DB-Instance reading 'location' values and comparing them to the user's global coordinates
    public void iterateThroughClinics(double[] referenceCoordinates) {
        pq = new PriorityQueue<Entry>(Collections.reverseOrder());

        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();

        while (it.hasNext()) {
            Document currentClinic = it.next();
            double[] currentClinicCoordinates = Utils.convertStringToDoubleArray(currentClinic.get("position").toString().split(","));

            double distanceInKm = Utils.haversineFormula(referenceCoordinates, currentClinicCoordinates);
            addPQElement(new Entry(distanceInKm, currentClinic));
        }
    }

    /*
    Iterate through the max-heap priority que with N elements and turn it
    into a Document array from descending to ascending order
    */
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

    // Format the document-data of the clinics to display into a JSON-String that will be published to Patient API
    private String formatRetrievedClinics(Document[] clinics, String payload, CollectionSchema querySchema) {
        Gson gson = new Gson();

        // Payload attributes
        String statusCode = "500";
        String requestId = "-1";
        String clinicsJson = "-1";

        try {
            clinicsJson = gson.toJson(clinics);
            requestId = PayloadParser.getAttributeFromPayload(payload, "requestId", querySchema).toString();
            statusCode = requestId.length() > 0 ? "200" : "404";

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Publish message to Patient API
        Map<String, Object> map = new HashMap<>();
        map.put("clinics", clinicsJson.toString());
        map.put("requestID", requestId.toString());
        map.put("status", Integer.parseInt(statusCode));

        System.out.println(gson.toJson(map));
        return gson.toJson(map);
    }

    @Override
    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "grp20/req/map/nearby";

        CollectionSchema publishSchema;
        NearbyClinics queryKey; // Current query is used as a key to access the object's corresponding priority queue

        if (topic.contains(MqttMain.queryTopicKeywords[2])) { // "fixed"
            queryKey = new NearbyFixed(publishTopic, payload);  
            publishSchema = new NearbyFixedQuerySchema(); 
        }
        else {
            queryKey = new NearbyRadius(publishTopic, payload);
            publishSchema = new NearbyRadiusQuerySchema();
        }

        queryKey.queryDatabase(payload);

        Document[] clinicsToDisplay = retrieveClosestClinics(queryKey.getN(), queryKey);
        String publishMessage = formatRetrievedClinics(clinicsToDisplay, payload, publishSchema);

        MqttMain.subscriptionManagers.get(topic).publishMessage(publishTopic, publishMessage);
    }
}
