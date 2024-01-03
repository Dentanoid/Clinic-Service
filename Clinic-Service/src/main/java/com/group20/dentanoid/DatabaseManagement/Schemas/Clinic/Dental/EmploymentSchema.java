package com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.Dental;
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

    public void registerAddData(String clinic_id, String dentist_id, String dentist_name, String requestID) {
        this.clinic_id = clinic_id;
        this.dentist_id = dentist_id;
        this.dentist_name = dentist_name;
        this.requestID = requestID;
    }

    public void registerRemoveData(String clinic_id, String dentist_id, String requestID) {
        this.clinic_id = clinic_id;
        this.dentist_id = dentist_id;
        this.requestID = requestID;
    }
    
    /*
        Since all values in the payload doesn't cover all attributes in this schema,
        this function retrieves the remaining attributes' values.

        PARAMETERS:
        * payload: The JSON string sent from an external component via MQTT.
        * operation: The type of operation to perform that is specified in the corresponding
                     method in DentalClinic.java. It's potential values are ["add", "remove"].
    */
    public void assignAttributesFromPayload(String payload, String operation) {
        Gson gson = new Gson();
        EmploymentSchema payloadObject = gson.fromJson(payload, getClass());
        String dentistId = operation.equals("add") ? UUID.randomUUID().toString() : payloadObject.dentist_id;

        if (operation.equals("add")) {
            registerAddData(
                payloadObject.clinic_id,
                dentistId,
                payloadObject.dentist_name,
                payloadObject.requestID
            );
        } else {
            registerRemoveData(
                payloadObject.clinic_id,
                dentistId,
                payloadObject.requestID
            );
        }
    }
}
