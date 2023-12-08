package generalPackage.Main.DatabaseManagement.Schemas.Query;
import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

import org.bson.Document;

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
