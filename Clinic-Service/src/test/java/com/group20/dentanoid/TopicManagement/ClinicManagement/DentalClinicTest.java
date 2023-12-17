package com.group20.dentanoid.TopicManagement.ClinicManagement;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import org.bson.Document;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

class DentalClinicTest {

    @Test
    void registerClinic() {
   
        // Define data variables
        String topic = "grp20/req/dental/clinics/register";
        String expectedClinicName = "Happy Teeth";

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
    void deleteClinic() {
    }

    @Test
    void getOneClinic() {
    }

    @Test
    void addEmployee() {
    }

    @Test
    void removeEmployee() {
    }
}