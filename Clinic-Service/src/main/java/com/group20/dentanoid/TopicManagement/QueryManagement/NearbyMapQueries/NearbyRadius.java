package com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries;

import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.DatabaseManagement.Schemas.Query.NearbyRadiusQuerySchema;
import com.group20.dentanoid.Utils.Entry;
import com.group20.dentanoid.Utils.Utils;

public class NearbyRadius extends NearbyClinics {
    private static Integer maximumClinicsInQuery = 1000;
    private Double radius;
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
    public void readPayloadAttributes() {
        getReferencePosition();
        getRadius(payload);
    }

    @Override
    public void addPQElement(Entry element) {
        addClinicWithinRadius(element);

        // Delete element with maximum distance to conform to given quantity-constraint of clinics to return
        if (clinicsExceedsQuantityBoundary()) {
            pq.poll();
        }
    }

    // Display clinic if it is within the requested radius
    private void addClinicWithinRadius(Entry element) {
        if (element.getKey() <= radius) {
            pq.add(element);
            incrementFoundClinics();
        }
    }

    private boolean clinicsExceedsQuantityBoundary() {
        return pq.size() > maximumClinicsInQuery;
    }

    private void incrementFoundClinics() {
        if (numberOfFoundClinics < maximumClinicsInQuery) {
            numberOfFoundClinics++;
        }
    }

    @Override
    public void getReferencePosition() {
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "reference_position", new NearbyRadiusQuerySchema());
       referenceCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
    }

    private void getRadius(String payload) {
        radius = Double.parseDouble(PayloadParser.getAttributeFromPayload(payload, "radius", new NearbyRadiusQuerySchema()).toString());
    }
}
