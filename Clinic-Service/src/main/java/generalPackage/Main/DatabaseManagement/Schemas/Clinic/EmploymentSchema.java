package generalPackage.Main.DatabaseManagement.Schemas.Clinic;
import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

import org.bson.Document;

// This schema covers the payload-cases where an employee is to be added or removed from the clinic
public class EmploymentSchema implements CollectionSchema {
    String clinic_name;
    String clinic_id;
    String employee_name;

    public EmploymentSchema() {
        this.clinic_name = " ";
        this.clinic_id = " ";
        this.employee_name = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_name", this.clinic_name)
        .append("clinic_id", this.clinic_id)
        .append("employee_name", this.employee_name);
    }
}
