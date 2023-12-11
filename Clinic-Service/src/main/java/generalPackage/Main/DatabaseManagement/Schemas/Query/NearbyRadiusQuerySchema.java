package generalPackage.Main.DatabaseManagement.Schemas.Query;
import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

import org.bson.Document;

public class NearbyRadiusQuerySchema implements CollectionSchema {
    private String radius;
    private String reference_position;
    private String requestID;

    public NearbyRadiusQuerySchema() {
        this.radius = " ";
        this.reference_position = " ";
        this.requestID = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("radius", this.radius)
        .append("reference_position", this.reference_position)
        .append("requestId", this.requestID);
    }

    public String getRequestId() {
        return this.requestID;
    }

    @Override
    public void assignAttributesFromPayload(String payload) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignAttributesFromPayload'");
    }
}
