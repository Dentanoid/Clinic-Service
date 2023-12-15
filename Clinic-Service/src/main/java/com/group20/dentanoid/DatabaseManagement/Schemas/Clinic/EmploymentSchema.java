package com.group20.dentanoid.DatabaseManagement.Schemas.Clinic;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

import java.util.UUID;
import org.bson.Document;
import com.google.gson.Gson;

// This schema covers the payload-cases where an employee is to be added or removed from the clinic.
public class EmploymentSchema implements CollectionSchema {
    private String clinic_id;
    private String dentist_id;
    private String dentist_name;
    private String requestID;

    public EmploymentSchema() {
        this.clinic_id = " ";
        this.dentist_id = " ";
        this.requestID = " ";
    }

    public EmploymentSchema(String dentist_id, String dentist_name) {
        this.dentist_id = dentist_id;
        this.dentist_name = dentist_name;
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_id", this.clinic_id)
        .append("dentist_id", this.dentist_id)
        .append("dentist_name", this.dentist_name)
        .append("requestID", this.requestID);
    }

    public void registerData(String clinic_id, String dentist_id, String dentist_name, String requestID) {
        this.clinic_id = clinic_id;
        this.dentist_id = dentist_id;
        this.dentist_name = dentist_name;
        this.requestID = requestID;
    }
    
    @Override
    public void assignAttributesFromPayload(String payload, String operation) { // Potential values for 'operation' = ["add", "remove"]
        Gson gson = new Gson();
        EmploymentSchema myObjTest = gson.fromJson(payload, getClass());

        String dentistId = operation.equals("add") ? UUID.randomUUID().toString() : myObjTest.dentist_id;
        String dentistName = operation.equals("add") ? myObjTest.dentist_name : "-1";

        registerData(
            myObjTest.clinic_id,
            dentistId,
            dentistName,
            myObjTest.requestID
        );
    }

    @Override
    public void assignAttributesFromPayload(String payload) { // TODO: Refactor soon
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignAttributesFromPayload'");
    }

    /*
    public String toString() {
        return "{g}";
    }
    */
}
