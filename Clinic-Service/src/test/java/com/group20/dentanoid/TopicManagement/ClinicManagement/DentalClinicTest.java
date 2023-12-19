package com.group20.dentanoid.TopicManagement.ClinicManagement;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.List;
import org.bson.Document;

import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(OrderAnnotation.class)

/*
    Test the DentalClinic.java class by running a sequence of
    tests representing the lifetime of a clinic:
    
    1) Registering a clinic in the DB

    2) Getting a clinic by its id

    3) Adding a dentist to the clinic

    4) Remvoing the dentist from the clinic

    5) Deleting the clinic from the DB
 */

class DentalClinicTest {
    @Test
    @Order(1)
    void registerClinic() {
        String topic = "grp20/req/dental/clinics/register";
        String expectedClinicName = "Happy Teeth - Unit Test Clinic";

        DatabaseManager.initializeDatabaseConnection();

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("clinic_name", expectedClinicName);
            put("position", "50.22,79.8");
            put("requestID", "requestID174");
        }});

        DentalClinic dentalClinic = new DentalClinic(topic, payload);
        dentalClinic.executeRequestedOperation();

        Document registeredClinic = PayloadParser.convertJSONToDocument(dentalClinic.getPublishMessage());
        String actualClinicName = registeredClinic.getString("clinic_name");

        assertEquals(expectedClinicName, actualClinicName);
    }

    @Test
    @Order(2)
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
        dentalClinic.executeRequestedOperation();

        Document retrievedClinic = PayloadParser.convertJSONToDocument(dentalClinic.getPublishMessage());

        assertNotNull(retrievedClinic);
    }

    @Test
    @Order(3)
    void addEmployee() {
        String topic = "grp20/req/dental/clinics/add";
        String expectedDentistName = "Olof";

        DatabaseManager.initializeDatabaseConnection();

        Document clinic = PayloadParser.findDocumentByAttributeValue(DatabaseManager.clinicsCollection, "clinic_name", "Happy Teeth - Unit Test Clinic");
        String clinicId = clinic.getString("clinic_id");

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("clinic_id", clinicId);
            put("dentist_name", expectedDentistName);
            put("requestID", "requestID1253");
        }});

        DentalClinic dentalClinic = new DentalClinic(topic, payload);
        dentalClinic.executeRequestedOperation();

        Document jsonResponse = PayloadParser.convertJSONToDocument(dentalClinic.getPublishMessage());

        String actualDentistName = jsonResponse.getString("dentist_name");
        assertEquals(expectedDentistName, actualDentistName);
    }

    @Test
    @Order(4)
    /*
        Since 'clinic_id' and 'dentist_id' are automatically generated in the previous tests,
        we cannot predict their values. Instead, we use the name of the clinic and dentist to
        access and test them.
    */
    void removeEmployee() {
        String topic = "grp20/req/dental/clinics/remove";
        DatabaseManager.initializeDatabaseConnection();

        Document clinic = PayloadParser.findDocumentByAttributeValue(DatabaseManager.clinicsCollection, "clinic_name", "Happy Teeth - Unit Test Clinic");
        String clinicId = clinic.getString("clinic_id");

        int i = DatabaseManager.getIndexOfNestedInstanceList(clinic, "employees", "dentist_name", "Olof");
        List<Document> employees = (List<Document>) clinic.get("employees");

        String expectedDentistId = employees.get(i).getString("dentist_id");

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("clinic_id", clinicId);
            put("dentist_id", expectedDentistId);
            put("requestID", "requestID1253");
        }});

        DentalClinic dentalClinic = new DentalClinic(topic, payload);
        dentalClinic.executeRequestedOperation();

        Document jsonResponse = PayloadParser.convertJSONToDocument(dentalClinic.getPublishMessage());
        String actualDentistId = jsonResponse.getString("dentist_id");
        
        assertEquals(expectedDentistId, actualDentistId);
    }

    @Test
    @Order(5)
    void deleteClinic() {
        String topic = "grp20/req/dental/clinics/delete";
        DatabaseManager.initializeDatabaseConnection();

        Document clinic = PayloadParser.findDocumentByAttributeValue(DatabaseManager.clinicsCollection, "clinic_name", "Happy Teeth - Unit Test Clinic");
        String expectedClinicId = clinic.getString("clinic_id");

        String payload = PayloadParser.createJSONPayload(new HashMap<>() {{
            put("clinic_id", expectedClinicId);
            put("requestID", "requestID1253");
        }});

        DentalClinic dentalClinic = new DentalClinic(topic, payload);
        dentalClinic.executeRequestedOperation();

        Document jsonResponse = PayloadParser.convertJSONToDocument(dentalClinic.getPublishMessage());
        String actualClinicId = jsonResponse.getString("clinic_id");
        
        assertEquals(expectedClinicId, actualClinicId);
    }
}