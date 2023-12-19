package com.group20.dentanoid.DatabaseManagement.Schemas.Query;
import org.bson.Document;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

// The payload structure that contains a fixed number of nearby clinics to return
public class NearbyFixedQuerySchema implements CollectionSchema {
    private String number;
    private String reference_position;
    private String requestID;

    public NearbyFixedQuerySchema() {
        this.number = " ";
        this.reference_position = " ";
        this.requestID = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("number", this.number)
        .append("reference_position", this.reference_position)
        .append("requestID", this.requestID);
    }

    public String getRequestId() {
        return this.requestID;
    }
}
