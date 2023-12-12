package com.group20.dentanoid.DatabaseManagement.Schemas.Query;
import org.bson.Document;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

// The payload structure that contains a fixed number of nearby clinics to return
public class NearbyFixedQuerySchema implements CollectionSchema {
    String nearby_clinics_number;
    String reference_position;


    public NearbyFixedQuerySchema() {
        this.nearby_clinics_number = " ";
        this.reference_position = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("nearby_clinics_number", this.nearby_clinics_number)
        .append("reference_position", this.reference_position);
    }
}
