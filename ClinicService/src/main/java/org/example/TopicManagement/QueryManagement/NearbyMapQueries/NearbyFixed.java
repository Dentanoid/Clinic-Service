package org.example.TopicManagement.QueryManagement.NearbyMapQueries;

import org.example.DatabaseManagement.DatabaseManager;
import org.example.DatabaseManagement.PayloadParser;
import org.example.DatabaseManagement.Schemas.Query.NearbyFixedQuerySchema;
import org.example.DatabaseManagement.Schemas.Query.NearbyRadiusQuerySchema;
import org.example.Utils.Entry;
import org.example.Utils.Utils;

public class NearbyFixed extends NearbyClinics {

    private int numberOfClinicsToQuery;

    public NearbyFixed(String topic, String payload) {
        super(topic, payload);
    }

    @Override
    public void queryDatabase(String payload) { // Redundant to override
        System.out.println("fixed query database");

        readPayloadAttributes(payload);
        iterateThroughClinics(userCoordinates);
    }

    @Override
    public void readPayloadAttributes(String payload) { // Redundant to override
        getNumberOfClinicsToQuery(payload);
        getUserPosition(payload);
    }

    @Override
        public void getUserPosition(String payload) {
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "user_position", new NearbyFixedQuerySchema());
        System.out.println(user_position);
        userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
    }

    @Override
    public void getNumberOfClinicsToQuery(String payload) {
        int requestedPayloadNumber = Integer.parseInt(PayloadParser.getAttributeFromPayload(payload, "nearby_clinics_number", new NearbyFixedQuerySchema()).toString());
        int numberOfExistingClinics = (int)DatabaseManager.clinicsCollection.countDocuments();
        // n = Math.min(requestedPayloadNumber, numberOfExistingClinics);
        setN(Math.min(requestedPayloadNumber, numberOfExistingClinics));
    }

    public void setN(int value) {
        numberOfClinicsToQuery = value;
    }

    @Override
    public int getN() {
        return numberOfClinicsToQuery;
    }

    @Override
    public void addPQElement(Entry element) {
        pq.add(element);

        if (pq.size() > getN()) { // Delete element with maximum distance to conform to given quantity-constraint of clinics to return
            pq.poll();
        }
    }
}
