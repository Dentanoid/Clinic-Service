package com.group20.dentanoid.DatabaseManagement.Schemas.Clinic;

import java.util.ArrayList;
import java.util.UUID;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;

public class ClinicSchema implements CollectionSchema {
    private String clinic_name;
    private String position;
    private String clinic_id;
    private ArrayList<String> employees;
    private String requestID; // TODO: Add requestID for the remaining register-methods below

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
    private void registerCreateClinicData(String clinic_name, String position, String clinic_id, ArrayList<String> employees) {
        this.clinic_name = clinic_name;
        this.position = position;
        this.clinic_id = clinic_id;
        this.employees = employees;
    }

    // DELETE: Use the clinic's id to find it in the DB
    private void registerClinicIdentifier(String clinic_id, String requestID) {
        this.clinic_id = clinic_id;
        this.requestID = requestID;
    }

    private void registerRequestID(String requestID) {
        this.requestID = requestID;
    }

    // Assign values to the attributes of the schema based on the specified clinic operation
    @Override
    public void assignAttributesFromPayload(String payload, String operation) { // String operation = ["create", "delete", "getOne", "getAll"]
        Gson gson = new Gson();
        ClinicSchema myObjTest = gson.fromJson(payload, getClass());

        // TODO: Refactor if-statements - IDEA: Interface or Abstract class 'DataRegisterer.java'

        // The payload include
        if (operation.equals("create")) { // IDEA: Refactor register-data-parameters into ArrayList<String>, add them in the operation-if-statements and register(ArrayList<String> attributes) becomes a general method
            registerCreateClinicData(
                // Data included in payload:
                myObjTest.clinic_name,
                myObjTest.position,

                // Data automatically defined:
                UUID.randomUUID().toString(),
                new ArrayList<>()
            );
        }
        /*
         Deleting and reading one clinic only needs one attribute
         in the payload to find the requested clinic in the DB: 'clinic_id'
        */
        else if (operation.equals("delete") || operation.equals("getOne")) {
            registerClinicIdentifier(
                myObjTest.clinic_id,
                myObjTest.requestID
                );
        }
        else if (operation.equals("getAll")) {
            registerRequestID(myObjTest.requestID);
        }
    }

    @Override
    public void assignAttributesFromPayload(String payload) {
        /*
        Gson gson = new Gson();
        ClinicCreateSchema myObjTest = gson.fromJson(payload, getClass());

        registerData(
            // Data in payload
            myObjTest.clinic_name,
            myObjTest.position,

            // Data not in payload
            clinic_id, // TODO: Remove constructor and do UUID.random() on here instead
            new ArrayList<>()
        );
        */
    }
}
