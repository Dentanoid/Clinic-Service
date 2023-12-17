package com.group20.dentanoid.TopicManagement.ClinicManagement;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class DentalClinicTest {
    @Test
    void registerClinic() {
   
        // Define data variables
        String topic = "grp20/req/dental/clinics/register";
        String expectedClinicName = "Happy Teeth - Unit Test Clinic";

        DatabaseManager.initializeDatabaseConnection();

        // Create payload
        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("clinic_name", expectedClinicName);
            put("position", "50.22,79.8");
            put("requestID", "requestID174");
        }});

        DentalClinic dentalClinic = new DentalClinic(topic, payload);
        Document registeredClinic = PayloadParser.convertPayloadToDocumentGeneral(dentalClinic.getPublishMessage());
        String actualClinicName = registeredClinic.getString("clinic_name");

        assertEquals(expectedClinicName, actualClinicName);
    }

    @Test
    void getOneClinic() {
        String topic = "grp20/req/dental/clinics/get/one";
        DatabaseManager.initializeDatabaseConnection();

        Document doc = PayloadParser.findDocumentByAttributeValue(DatabaseManager.clinicsCollection, "clinic_name", "Happy Teeth - Unit Test Clinic");
        String clinicId = doc.getString("clinic_id");

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("clinic_id", clinicId);
            put("requestID", "requestID1253");
        }});

        DentalClinic dentalClinic = new DentalClinic(topic, payload);
        Document retrievedClinic = PayloadParser.convertPayloadToDocumentGeneral(dentalClinic.getPublishMessage());

        assertNotNull(retrievedClinic);
    }

    @Test
    void addEmployee() {

    }

    @Test
    void removeEmployee() {
    }

    @Test
    void deleteClinic() {
    }
}