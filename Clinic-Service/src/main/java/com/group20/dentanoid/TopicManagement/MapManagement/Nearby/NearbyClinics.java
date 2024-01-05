package com.group20.dentanoid.TopicManagement.MapManagement.Nearby;

import com.group20.dentanoid.MqttMain;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Map.Nearby.NearbyFixedQuerySchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Map.Nearby.NearbyRadiusQuerySchema;
import com.group20.dentanoid.Utils.Entry;
import com.group20.dentanoid.Utils.MqttUtils;
import com.group20.dentanoid.Utils.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.bson.Document;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;

public class NearbyClinics extends NearbyQuery {
    public PriorityQueue<Entry> pq; // Max heap priority que with key-value pairs contained in customized class 'Entry'
    
    /*
     This variable has two use cases:
        * Represents user's current position if the selected map mode in Patient Client is 'Nearby'
        * Represents the searched position if the selected map mode is 'Search'
     */
    public double[] referenceCoordinates;

    public String topic;
    public String payload;

    public NearbyClinics(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    @Override
    public void queryDatabase() {
        readPayloadAttributes(); 
        iterateThroughClinics();
    }

    // Linear search through every DB-Instance reading 'location' values and comparing them to the user's global coordinates
    public void iterateThroughClinics() {
        pq = new PriorityQueue<Entry>(Collections.reverseOrder());

        FindIterable<Document> clinics = DatabaseManager.clinicsCollection.find();
        Iterator<Document> it = clinics.iterator();

        while (it.hasNext()) {
            Document currentClinic = it.next();

            try {
                double[] currentClinicCoordinates = Utils.convertStringToDoubleArray(currentClinic.get("position").toString().split(","));

                double distanceInKm = Utils.haversineFormula(referenceCoordinates, currentClinicCoordinates);
                addPQElement(new Entry(distanceInKm, currentClinic));
            }
            catch (Exception e) {
                System.out.println(String.format("The 'position' attribute of clinic '%s' has the wrong format", currentClinic.get("clinic_name")));
            }
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
    private String formatRetrievedClinics(Document[] clinics, CollectionSchema querySchema) {
        Gson gson = new Gson();

        // Payload attributes
        String statusCode = "-1";
        String requestID = PayloadParser.getAttributeFromPayload(payload, "requestID", querySchema).toString();
        String clinicsJson = "-1";

        try {
            // clinicsJson = PayloadParser.convertDocumentsToJSON(clinics);
            // clinicsJson = PayloadParser.convertDocumentToJSON(clinics[0]);
            clinicsJson = PayloadParser.convertDocsToJSON(clinics, "clinics");
            // clinicsJson = gson.toJson(clinics); // gson.toJson() --> generates weird string
            statusCode = clinicsJson.length() > 0 ? "200" : "404";
        } catch (Exception e) {
            statusCode = "500";
        }

        return PayloadParser.restructurePublishMessage(clinicsJson, requestID, statusCode);
    }

    // Directs the codeflow to either 'radius' or 'fixed number' query of clinics
    @Override
    public void executeRequestedOperation() {
        CollectionSchema publishSchema;
        NearbyClinics queryKey; // Current query is used as a key to access the object's corresponding priority queue

        Object[] definedQuerySettings = defineQueryType();

        queryKey = (NearbyClinics) definedQuerySettings[0];
        publishSchema = (CollectionSchema) definedQuerySettings[1];

        queryKey.queryDatabase();

        Document[] clinicsToDisplay = retrieveClosestClinics(queryKey.getN(), queryKey); // Pass the key containing its own priority que
        String publishMessage = formatRetrievedClinics(clinicsToDisplay, publishSchema);

        System.out.println(clinicsToDisplay);
        System.out.println(publishMessage);

        MqttMain.publish(MqttUtils.mapPublishFormat, publishMessage);
    }

    /*
        The query type is distinguished by two settings:

        1) queryKey: The object and the functionality in its class (either NearbyRadius or NearbyFixed)
        2) publishSchema: The schema and its contained payload parameters (RadiusQuerySchema or FixedQuerySchema)
     */
    private Object[] defineQueryType() {
        NearbyClinics queryKey;
        CollectionSchema publishSchema;

        // Patient API tells this service to run a 'fixed number' query
        if (topic.contains(MqttUtils.mapOperations[1])) {
            queryKey = new NearbyFixed(MqttUtils.mapPublishFormat, payload);  
            publishSchema = new NearbyFixedQuerySchema(); 
        }
        // Patient API tells this service to run a 'radius' query
        else {
            queryKey = new NearbyRadius(MqttUtils.mapPublishFormat, payload);
            publishSchema = new NearbyRadiusQuerySchema();
        }

        return new Object[] { queryKey, publishSchema };
    }
}
