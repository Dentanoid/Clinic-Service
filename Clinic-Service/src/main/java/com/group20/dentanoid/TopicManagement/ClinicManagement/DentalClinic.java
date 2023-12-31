package com.group20.dentanoid.TopicManagement.ClinicManagement;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;

import com.group20.dentanoid.GoogleAPI.ValidatedClinic;
import com.group20.dentanoid.Utils.Entry;
import com.group20.dentanoid.Utils.MqttUtils;
import com.group20.dentanoid.Utils.Utils;
import com.group20.dentanoid.ClinicService;
import com.group20.dentanoid.MqttMain;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.ClinicSchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.EmploymentSchema;

public class DentalClinic implements Clinic {
    private String publishMessage = "-1";

    private Document payloadDoc = null;
    private String topic;
    private String payload;

    // Actual payload response values
    private String status = "";
    private String requestID = "";
    private String clinicsData = "-1";

    // Name of payload attributes
    private String clinic_id = "clinic_id";
    private String reqID = "requestID";
    private String clinicValidationJSON;
    
    public DentalClinic(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public void registerClinic() {
        payloadDoc = getClinicDocument("create");
        // requestID = payloadDoc.remove(reqID).toString();

        // Note for developers: This code is in development
        JSONObject jsonObject = new JSONObject();
        ValidatedClinic clinicRequestObj = (ValidatedClinic) PayloadParser.getObjectFromPayload(payload, ValidatedClinic.class);

        // TOOD: Refactor this - PayloadParser.createJSONObject()
        jsonObject.put("clinic_name", clinicRequestObj.getClinicName());
        jsonObject.put("clinic_id", clinicRequestObj.getClinicId());
        jsonObject.put("position", clinicRequestObj.getPosition());
        jsonObject.put("employees", clinicRequestObj.getEmployees());
        jsonObject.put("ratings", "-1");
        jsonObject.put("photoURL", "-1");
        jsonObject.put("address", "-1");
        jsonObject.put("status", "-1");

        try {
            FileWriter file = new FileWriter("Clinic-Service\\src\\main\\java\\com\\group20\\dentanoid\\GoogleAPI\\nodejsTest\\clinic.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // ------------------------------------------------------------
        try {
            // TODO: Account for bin and mac os - cmd.exe = windows
            Process myChildProcess = Runtime.getRuntime().exec("cmd.exe /c start bash childprocess-api.sh");
            parallelTest();
        }
        catch (Exception e){
           System.out.println("Error: " + e);
        }
    }

    private void parallelTest() { // TODO: Refactor parallelTest() and childProcess in ParallelUtils.java
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable helloRunnable = new Runnable() {
        public void run() {
            Integer currentStatus;
            try {
                currentStatus = getClinicStatus();

                if (currentStatus == 200 || currentStatus == 404) {
                try {
                    // TODO: Refactor with existing method in PayloadParser.java
                    Gson gson = new Gson();
                    ValidatedClinic clinicObj = gson.fromJson(clinicValidationJSON, ValidatedClinic.class);

                    if (currentStatus == 200) {
                        payloadDoc.append("ratings", clinicObj.getRatings());
                        payloadDoc.append("photoURL", clinicObj.getPhotoURL());
                        payloadDoc.append("address", clinicObj.getAddress());
                    }

                    requestID = payloadDoc.remove(reqID).toString();
                    payloadDoc.remove("status");

                    DatabaseManager.clinicsCollection.insertOne(payloadDoc);
                    publishToExternalComponent("register");
                }
                 catch (Exception exception) {
                  status = "500";
                }
                    executor.shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        };

        executor.scheduleAtFixedRate(helloRunnable, 0, 100, TimeUnit.MILLISECONDS);
    }



    // ---------------------------------------------------------------

    // REFACTOR TO Utils.java or PayloadParser.java

    private Integer getClinicStatus() throws Exception {
        // String jsonString = readFileAsString("Clinic-Service\\src\\main\\java\\com\\group20\\dentanoid\\GoogleAPI\\nodejsTest\\clinic.json");
        clinicValidationJSON = readFileAsString("Clinic-Service\\src\\main\\java\\com\\group20\\dentanoid\\GoogleAPI\\nodejsTest\\clinic.json");

        Gson gson = new Gson();
        // ValidatedClinic retrievedClinic = gson.fromJson(jsonString, ValidatedClinic.class);
        ValidatedClinic retrievedClinic = gson.fromJson(clinicValidationJSON, ValidatedClinic.class);
        return retrievedClinic.getStatus();
    }

    private String readFileAsString(String file)throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    // ---------------------------------------------------------------




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

    private void publishToExternalComponent(String operation) {
        parsePublishMessage();
        publishMessage((MqttUtils.clinicsPublishFormat + operation));
    }

    // Publishes a JSON message to an external component ('Dentist API' or 'Patient API')
    private void publishMessage(String publishTopic) {
        if (publishMessage != "-1") {
            System.out.println(publishMessage);
            MqttMain.publish(publishTopic, publishMessage);
        } else {
            System.out.println("Status 404 - Did not find DB-instance based on the given topic");
        }
    }

    private void parsePublishMessage() {
        publishMessage = clinicsData.equals("-1") ?
            PayloadParser.parsePublishMessage(payloadDoc, requestID, status) : PayloadParser.restructurePublishMessage(clinicsData, requestID, status);
    }

    private Document getClinicById(String clinicId) {
        return DatabaseManager.clinicsCollection.find(eq(clinic_id, clinicId)).first();
    }

    public String getPublishMessage() {
        return this.publishMessage;
    }
}
