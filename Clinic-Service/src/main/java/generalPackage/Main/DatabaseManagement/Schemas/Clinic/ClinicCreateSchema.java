package generalPackage.Main.DatabaseManagement.Schemas.Clinic;

import java.util.ArrayList;

import org.bson.Document;

import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

public class ClinicCreateSchema implements CollectionSchema {
    String clinic_name;
    String clinic_id;
    String position;
    ArrayList<String> employees;

    public ClinicCreateSchema() {
        this.clinic_name = " ";
        this.clinic_id = " ";
        this.position = " ";
        this.employees = new ArrayList<>(); 
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_name", this.clinic_name)
        .append("clinic_id", this.clinic_id)
        .append("position", this.position)
        .append("employees", this.employees);
    }
}
