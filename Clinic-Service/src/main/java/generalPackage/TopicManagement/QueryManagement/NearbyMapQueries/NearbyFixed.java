package generalPackage.TopicManagement.QueryManagement.NearbyMapQueries;

import generalPackage.DatabaseManagement.DatabaseManager;
import generalPackage.DatabaseManagement.PayloadParser;
import generalPackage.DatabaseManagement.Schemas.Query.NearbyFixedQuerySchema;
import generalPackage.Utils.Entry;
import generalPackage.Utils.Utils;

public class NearbyFixed extends NearbyClinics {
    private int numberOfClinicsToQuery;

    public NearbyFixed(String topic, String payload) {
        super(topic, payload);
    }

    @Override
    public void readPayloadAttributes(String payload) {
        getNumberOfClinicsToQuery(payload);
        getReferencePosition(payload);
    }

    @Override
        public void getReferencePosition(String payload) {
        Object user_position = PayloadParser.getAttributeFromPayload(payload, "reference_position", new NearbyFixedQuerySchema());
        System.out.println(user_position);
        userCoordinates = Utils.convertStringToDoubleArray(user_position.toString().split(","));
    }

    private void getNumberOfClinicsToQuery(String payload) {
        int requestedPayloadNumber = Integer.parseInt(PayloadParser.getAttributeFromPayload(payload, "nearby_clinics_number", new NearbyFixedQuerySchema()).toString());
        int numberOfExistingClinics = (int)DatabaseManager.clinicsCollection.countDocuments();
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
