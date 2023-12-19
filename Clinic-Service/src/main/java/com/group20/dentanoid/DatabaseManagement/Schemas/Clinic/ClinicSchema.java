package com.group20.dentanoid.DatabaseManagement.Schemas.Clinic;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

import java.util.ArrayList;
import java.util.UUID;

import org.bson.Document;
import com.google.gson.Gson;

public class ClinicSchema implements CollectionSchema {
    private String clinic_name;
    private String position;
    private String clinic_id;
    private ArrayList<String> employees;
    private String requestID;

    public ClinicSchema() { // IDEA: Run 'assignAttributesFromPayload()' in constructor
        this.clinic_name = " ";
        this.clinic_id = " ";
        this.position = " ";
        this.employees = new ArrayList<>(); 
        this.requestID = " ";
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_name", this.clinic_name)
        .append("clinic_id", this.clinic_id)
        .append("position", this.position)
        .append("employees", this.employees)
        .append("requestID", this.requestID);
    }

    // POST: Assign values to attributes at creation
    private void registerCreateClinicData(String clinic_name, String position, String clinic_id, ArrayList<String> employees, String requestID) {
        this.clinic_name = clinic_name;
        this.position = position;
        this.clinic_id = clinic_id;
        this.employees = employees;
        this.requestID = requestID;
    }

    // DELETE: Use the clinic's id to find it in the DB
    private void registerClinicIdentifier(String clinic_id, String requestID) {
        this.clinic_id = clinic_id;
        this.requestID = requestID;
    }

    private void registerRequestID(String requestID) {
        this.requestID = requestID;
    }

    /*
        Assign values to the attributes of the schema based on the specified clinic operation.

        PARAMETERS:
        * payload: The JSON string sent from an external component via MQTT.
        * operation:
            - The type of operation to perform that is specified in the corresponding
              method in DentalClinic.java.
            - Potential values: ["create", "delete", "getOne", "getAll"]
    */
    public void assignAttributesFromPayload(String payload, String operation) {
        Gson gson = new Gson();
        ClinicSchema payloadObject = gson.fromJson(payload, getClass());

        if (operation.equals("create")) {
            registerCreateClinicData(
                // Data included in payload:
                payloadObject.clinic_name,
                payloadObject.position,

                // Data automatically defined:
                UUID.randomUUID().toString(),
                new ArrayList<>(),
                payloadObject.requestID
            );
        }
        /*
         Deleting and reading one clinic only needs one attribute
         in the payload to find the requested clinic in the DB: 'clinic_id'
        */
        else if (operation.equals("delete") || operation.equals("getOne")) {
            registerClinicIdentifier(
                payloadObject.clinic_id,
                payloadObject.requestID
                );
        }
        else if (operation.equals("getAll")) {
            registerRequestID(payloadObject.requestID);
        }
    }
}
