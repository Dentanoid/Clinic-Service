package generalPackage.DatabaseManagement.Schemas.Clinic;

import org.bson.Document;
import generalPackage.DatabaseManagement.Schemas.CollectionSchema;

public class ClinicDeleteSchema implements CollectionSchema {
    String clinic_name;
    String clinic_id;

    public ClinicDeleteSchema() {
        this.clinic_name = " ";
        this.clinic_id = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_name", this.clinic_name)
        .append("clinic_id", this.clinic_id);
    }
}
