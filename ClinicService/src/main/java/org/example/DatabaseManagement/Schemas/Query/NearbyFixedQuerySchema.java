package org.example.DatabaseManagement.Schemas.Query;
import org.bson.Document;
import org.example.DatabaseManagement.Schemas.CollectionSchema;

// The payload structure that contains a fixed number of nearby clinics to return
public class NearbyFixedQuerySchema implements CollectionSchema {
    String nearby_clinics_number;
    String user_position;


    public NearbyFixedQuerySchema() {
        this.nearby_clinics_number = " ";
        this.user_position = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("nearby_clinics_number", this.nearby_clinics_number)
        .append("user_position", this.user_position);
    }
}
