package org.example.DatabaseManagement.Schemas.Query;

import java.util.ArrayList;

import org.bson.Document;
import org.example.DatabaseManagement.Schemas.CollectionSchema;

public class NearbyQuerySchema implements CollectionSchema {
    String nearby_clinics_number;
    String user_position;


    public NearbyQuerySchema() {
        this.nearby_clinics_number = " ";
        this.user_position = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("nearby_clinics_number", this.nearby_clinics_number)
        .append("user_position", this.user_position);
    }
}
