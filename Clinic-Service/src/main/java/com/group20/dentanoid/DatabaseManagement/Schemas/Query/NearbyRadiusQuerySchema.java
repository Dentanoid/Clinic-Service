package com.group20.dentanoid.DatabaseManagement.Schemas.Query;
import org.bson.Document;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

public class NearbyRadiusQuerySchema implements CollectionSchema {
    String radius;
    String reference_position;

    public NearbyRadiusQuerySchema() {
        this.radius = " ";
        this.reference_position = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("radius", this.radius)
        .append("reference_position", this.reference_position);
    }
}
