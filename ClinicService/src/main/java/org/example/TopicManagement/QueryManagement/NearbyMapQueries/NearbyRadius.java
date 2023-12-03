package org.example.TopicManagement.QueryManagement.NearbyMapQueries;

import org.example.DatabaseManagement.DatabaseManager;
import org.example.DatabaseManagement.PayloadParser;
import org.example.DatabaseManagement.Schemas.Query.NearbyFixedQuerySchema;
import org.example.DatabaseManagement.Schemas.Query.NearbyRadiusQuerySchema;
import org.example.TopicManagement.QueryManagement.Query;
import org.example.Utils.Entry;
import org.example.Utils.Utils;

public class NearbyRadius extends NearbyClinics {
    private Integer maximumClinicsInQuery = 20;
    private Double radius = -1.0;
    private Integer numberOfFoundClinics;

    public NearbyRadius(String topic, String payload) {
        super(topic, payload);
        numberOfFoundClinics = 0;
    }

    @Override
    public int getN() {
        return numberOfFoundClinics;
    }

    @Override
    public void queryDatabase(String payload) { // redundant ovveride
        System.out.println("radius query database");

        readPayloadAttributes(payload);
        iterateThroughClinics(userCoordinates);   
    }

    @Override
    public void readPayloadAttributes(String payload) { // May be redundan to ovveride, since we can put 'getRadius()' in 'getUserPosition()'
        // getNumberOfClinicsToQuery(payload);
        getUserPosition(payload);
        getRadius(payload);
    }

    @Override
    public void addPQElement(Entry element) {
        if (element.getKey() <= radius) { // Display clinic if it is within the requested radius
            pq.add(element);
            incrementFoundClinics();
        }

        if (pq.size() > maximumClinicsInQuery) { // Delete element with maximum distance to conform to given quantity-constraint of clinics to return
            pq.poll();
        }
    }

    private void incrementFoundClinics() {
        if (numberOfFoundClinics < maximumClinicsInQuery) {
            numberOfFoundClinics++;
        }
    }

    /*
    @Override
    public void getNumberOfClinicsToQuery(String payload) {     
       int numberOfExistingClinics = (int)DatabaseManager.clinicsCollection.countDocuments();
       n = Math.min(maximumClinicsInQuery, numberOfExistingClinics);
       System.out.println("n = " + n); 
    }
    */

    @Override
    public void getUserPosition(String payload) {
        System.out.println(payload);
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "reference_position", new NearbyRadiusQuerySchema());
        System.out.println(user_position);
        userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
    }

    private void getRadius(String payload) {
        radius = Double.parseDouble(PayloadParser.getAttributeFromPayload(payload, "radius", new NearbyRadiusQuerySchema()).toString());
    }
}
