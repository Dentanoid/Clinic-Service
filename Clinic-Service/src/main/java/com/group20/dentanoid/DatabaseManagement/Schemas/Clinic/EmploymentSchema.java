package com.group20.dentanoid.DatabaseManagement.Schemas.Clinic;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

import java.util.UUID;
import org.bson.Document;
import com.google.gson.Gson;

// This schema covers the payload-cases where an employee is to be added or removed from the clinic.
public class EmploymentSchema implements CollectionSchema {
    private String clinic_id;
    private String dentist_id;
    private String requestID;

    public EmploymentSchema() {
        this.clinic_id = " ";
        this.dentist_id = " ";
        this.requestID = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_id", this.clinic_id)
        .append("dentist_id", this.dentist_id)
        .append("requestID", this.requestID);
    }

    public void registerData(String clinic_id, String dentist_id, String requestID) {
        this.clinic_id = clinic_id;
        this.dentist_id = dentist_id;
        this.requestID = requestID;
    }
    
    @Override
    public void assignAttributesFromPayload(String payload, String operation) { // Potential values for 'operation' = ["add", "remove"]
        Gson gson = new Gson();
        EmploymentSchema myObjTest = gson.fromJson(payload, getClass());
        String dentistId = operation.equals("add") ? UUID.randomUUID().toString() : myObjTest.dentist_id; // TODO: Replace UUID.randomUUID with the dentist_id retrieved from dentist_client

        registerData(
            myObjTest.clinic_id,
            dentistId,
            myObjTest.requestID
        );
    }

    @Override
    public void assignAttributesFromPayload(String payload) { // TODO: Refactor soon
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignAttributesFromPayload'");
    }
}
