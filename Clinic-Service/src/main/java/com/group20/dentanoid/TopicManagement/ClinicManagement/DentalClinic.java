package com.group20.dentanoid.TopicManagement.ClinicManagement;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;

import com.group20.dentanoid.Utils.MqttUtils;
import com.group20.dentanoid.Utils.ParallelUtils;
import com.group20.dentanoid.MqttMain;
import com.group20.dentanoid.BackendMapAPI.ValidatedClinic;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.ClinicSchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.EmploymentSchema;

public class DentalClinic implements Clinic {
    private static String communicationFilePath = "Clinic-Service\\src\\main\\java\\com\\group20\\dentanoid\\BackendMapAPI\\clinic.json";
    private static String publishMessage = "-1";
    private static Document payloadDoc = null;

    // Payload response values
    private static String status = "";
    private static String requestID = "";
    private static String clinicsData = "-1";

    private String topic;
    private String payload;

    // Name of payload response attributes
    private String clinic_id = "clinic_id";
    private String reqID = "requestID";
    
    public DentalClinic(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public void registerClinic() {
        payloadDoc = getClinicDocument("create");
        requestID = payloadDoc.remove(reqID).toString();

        ValidatedClinic clinicRequestObj = (ValidatedClinic) PayloadParser.getObjectFromPayload(payload, ValidatedClinic.class);
        clinicRequestObj.assignDataAttributes(payloadDoc, true);

        payloadDoc.append("status", "-1");
        payloadDoc.replace("position", clinicRequestObj.getPosition());

        ParallelUtils.instantiateChildProcess(payloadDoc, communicationFilePath);
    }

    // Delete a clinic by accessing corresponding 'clinic_id'
    public void deleteClinic() {
        payloadDoc = getClinicDocument("delete");
        requestID = payloadDoc.getString(reqID);

        String clinicId = payloadDoc.get(clinic_id).toString();
        Document clinicToDelete = getClinicById(clinicId);

        if (clinicToDelete != null) {
            try {
                DatabaseManager.replaceDocument(payloadDoc, clinicToDelete);
                DatabaseManager.clinicsCollection.deleteOne(clinicToDelete);
            } catch (Exception exception) {
                status = "500";
            }
        } else {
            status = "404";
        }
    }

    public void getClinics() {
        Document doc = PayloadParser.convertJSONToDocument(payload);

        // If 'clinic_id' is specified in the GET request, return one clinic
        if (doc.get("clinic_id") != null) {
            getOneClinic();
        } else {
            getAllClinics();
        }
    }

    // Get a clinic by 'clinic_id'
    public void getOneClinic() {
        payloadDoc = getClinicDocument("getOne");
        requestID = payloadDoc.getString(reqID);

        String clinicId = payloadDoc.get(clinic_id).toString();
        Document clinic = getClinicById(clinicId);

        if (clinic != null) {
            try {
                Gson gson = new Gson();
                clinicsData = gson.toJson(clinic);
            }
            catch (Exception e) {
                status = "500";
            }
        } else {
            status = "404";
        }
    }

    // Get all existing clinics in 'clinicsCollection'
    public void getAllClinics() {
        Document retrievedPayloadDoc = getClinicDocument("getAll");
        FindIterable<Document> allRegisteredClinics = DatabaseManager.clinicsCollection.find();

        requestID = retrievedPayloadDoc.get(reqID).toString();

        try {
            Iterator<Document> it = allRegisteredClinics.iterator();
            ArrayList<Document> clinics = new ArrayList<>();

            while (it.hasNext()) {
                Document currentClinic = it.next();
                clinics.add(currentClinic);
            }

            Gson gson = new Gson();
            clinicsData = gson.toJson(clinics);
        }
        catch (Exception e) {
            status = "500";
        }
    }

    // Add a new object {dentist_name: "name", dentist_id: "id"} to 'employees' array for the specified clinic
    public void addEmployee() {
        updateClinicEmployees("add");
    }

    // Remove the object inside of 'employees' that has the specified 'dentist_id'
    public void removeEmployee() {
        updateClinicEmployees("remove");
    }

    // Register or delete clinic from system
    private Document getClinicDocument(String operation) {
        ClinicSchema clinicObject = new ClinicSchema();
        clinicObject.assignAttributesFromPayload(payload, operation);
        return clinicObject.getDocument();
    }

     // Accounts for addition and removal of dentists
    private void updateClinicEmployees(String operation) {
        EmploymentSchema employmentObject = new EmploymentSchema();
        employmentObject.assignAttributesFromPayload(payload, operation);

        payloadDoc = employmentObject.getDocument();
        requestID = payloadDoc.remove(reqID).toString();

        String clinicId = payloadDoc.get(clinic_id).toString();
        
        Document clinic = getClinicById(clinicId);
        Document updateDoc = getClinicById(clinicId);

        if (clinic != null) {
            try {
                List<Document> employees = (List<Document>) clinic.get("employees"); 
                String dentistId = payloadDoc.get("dentist_id").toString();
                
                // Add dentist to clinic
                if (operation.equals("add")) {                    
                    String dentistName = payloadDoc.get("dentist_name").toString();

                    employees.add(new Document()
                        .append("dentist_id", dentistId)
                        .append("dentist_name", dentistName));
                }
                // Remove dentist from clinic
                else {
                    int dentistIdx = DatabaseManager.getIndexOfNestedInstanceList(clinic, "employees", "dentist_id", dentistId);

                    if (dentistIdx != -1) {
                        employees.remove(dentistIdx);

                    } else {
                        status = "404";
                    }
                }

                updateDoc.replace("employees", employees);
                DatabaseManager.updateInstanceByAttributeFilter(clinic_id, clinicId, updateDoc);
            }
            catch (Exception exception) {
                status = "500";
            }
        } else {
            status = "404";
        }
    }

    /*
     Direct the codeflow to the method that deals with the
     requested operation specified in the mqtt subscription-topic.
     This method returns the corresponding topic to publish to,
     based on the performed operation.
    */
    public void executeRequestedOperation() {
        status = "200";
        String operation = "-1";

        // Register clinic
        if (topic.contains(MqttUtils.clinicOperations[0])) {
            registerClinic();
        }
        else {
            // Add dentist to clinic
            if (topic.contains(MqttUtils.clinicOperations[2])) {
                addEmployee();
                operation = "add";
            }
            // Remove dentist from clinic
            else if (topic.contains(MqttUtils.clinicOperations[3])) {
                removeEmployee();
                operation = "remove";
            }
            // Delete clinic
            else if (topic.contains(MqttUtils.clinicOperations[4])) {
                deleteClinic();
                operation = "delete"; 
            }
            // Get a clinic by its id or get all clinics
            else if (topic.contains("get")) {
                getClinics();
                operation = "get";
            }

            publishToExternalComponent(operation);
        }
    }

    public static void publishToExternalComponent(String operation) {
        defineParsingOperation();
        publishClinicMessage((MqttUtils.clinicsPublishFormat + operation));
    }

    private static void publishClinicMessage(String publishTopic) {
        if (publishMessage != "-1") {
            MqttMain.publish(publishTopic, publishMessage);
        } else {
            System.out.println("Status 404 - Did not find DB-instance based on the given topic");
        }
    }

    private static void defineParsingOperation() {
        publishMessage = clinicsData.equals("-1") ?
            PayloadParser.parsePublishMessage(payloadDoc, requestID, status) : PayloadParser.restructurePublishMessage(clinicsData, requestID, status);
    }

    private Document getClinicById(String clinicId) {
        return DatabaseManager.clinicsCollection.find(eq(clinic_id, clinicId)).first();
    }

    public String getPublishMessage() {
        return publishMessage;
    }
}
