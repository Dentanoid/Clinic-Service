package generalPackage.Main.DatabaseManagement.Schemas.Clinic;
import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

import java.util.ArrayList;
import java.util.UUID;

import org.bson.Document;

import com.google.gson.Gson;

// This schema covers the payload-cases where an employee is to be added or removed from the clinic
public class EmploymentSchema implements CollectionSchema { // TODO: Add requestID as an attribute and return it in the publishMessage
    private String clinic_id;
    private String dentist_id;

    public EmploymentSchema() {
        this.clinic_id = " ";
        this.dentist_id = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_id", this.clinic_id)
        .append("dentist_id", this.dentist_id);
    }

    public void registerData(String clinic_id, String dentist_id) {
        this.clinic_id = clinic_id;
        this.dentist_id = dentist_id;
    }
    
    @Override
    public void assignAttributesFromPayload(String payload, String operation) { // operation = ["add", "remove"]
        Gson gson = new Gson();
        EmploymentSchema myObjTest = gson.fromJson(payload, getClass());
        String dentistId = operation.equals("add") ? UUID.randomUUID().toString() : myObjTest.dentist_id;

        registerData(
            // Data in payload
            myObjTest.clinic_id,

            // Data not in payload
            dentistId
        );
    }

    @Override
    public void assignAttributesFromPayload(String payload) { // TODO: Refactor soon
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignAttributesFromPayload'");
    }
}
