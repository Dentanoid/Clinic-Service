package generalPackage.Main.DatabaseManagement.Schemas.Clinic;

import org.bson.Document;

import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

public class ClinicDeleteSchema implements CollectionSchema {
    private String clinic_name;
    private String clinic_id;

    public ClinicDeleteSchema() {
        this.clinic_name = " ";
        this.clinic_id = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_name", this.clinic_name)
        .append("clinic_id", this.clinic_id);
    }

    @Override
    public void assignAttributesFromPayload(String payload) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignAttributesFromPayload'");
    }
}
