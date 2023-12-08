package generalPackage.Main.TopicManagement.QueryManagement.NearbyMapQueries;

import generalPackage.Main.DatabaseManagement.PayloadParser;
import generalPackage.Main.DatabaseManagement.Schemas.Query.NearbyRadiusQuerySchema;
import generalPackage.Utils.Entry;
import generalPackage.Utils.Utils;

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
    public void readPayloadAttributes(String payload) {
        getReferencePosition(payload);
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
    public void getReferencePosition(String payload) {
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "reference_position", new NearbyRadiusQuerySchema());
        userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
    }

    private void getRadius(String payload) {
        radius = Double.parseDouble(PayloadParser.getAttributeFromPayload(payload, "radius", new NearbyRadiusQuerySchema()).toString());
    }
}
