package com.group20.dentanoid.TopicManagement.ClinicManagement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;

import com.group20.dentanoid.GoogleAPI.ValidatedClinic;
import com.group20.dentanoid.Utils.Entry;
import com.group20.dentanoid.Utils.Utils;
import com.group20.dentanoid.ClinicService;
import com.group20.dentanoid.MqttMain;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.ClinicSchema;
import com.group20.dentanoid.DatabaseManagement.Schemas.Clinic.EmploymentSchema;

public class DentalClinic implements Clinic {
    private String publishTopic = "-1";
    private Document payloadDoc = null;
    private String publishMessage = "-1";

    private String topic;
    private String payload;

    private String status = "";
    private String requestID = "";
    private String clinicsData = "-1";

    // TODO: Make 'topic' and 'payload' a private String attribute to avoid reduntant passing as a parameter across all methods
    
    public DentalClinic(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
        executeRequestedOperation();
    }

    public void executeRequestedOperation() {
        publishTopic = runRequestedMethod();
        publishMessage();
    }

    public void registerClinic() {
        payloadDoc = getClinicDocument("create", new ClinicSchema());
        requestID = payloadDoc.remove("requestID").toString();

        try {
            DatabaseManager.clinicsCollection.insertOne(payloadDoc);
        }
        catch (Exception exception) {
            status = "500";
        }

        // Note for developers: This code is in development
        /*
        JSONObject jsonObject = new JSONObject();
        ValidatedClinic clinicRequestObj = (ValidatedClinic) PayloadParser.getObjectFromPayload(payload, ValidatedClinic.class);

        // TOOD: Refactor this
        jsonObject.put("clinic_name", clinicRequestObj.getClinicName());
        jsonObject.put("clinic_id", clinicRequestObj.getClinicId());
        jsonObject.put("position", clinicRequestObj.getPosition());
        jsonObject.put("employees", clinicRequestObj.getEmployees());
        jsonObject.put("ratings", "-1");
        jsonObject.put("total_user_ratings", "-1");
        jsonObject.put("photoURL", "-1");

        try {
            FileWriter file = new FileWriter("Clinic-Service\\src\\main\\java\\com.group20.dentanoid\\GoogleAPI\\validatedClinic.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("JSON file created: "+ jsonObject);
        
        // ------------------------------------------------------------
        try {
            // Write to validatedClinic.json - Change attributes 'clinic_name' and 'position'


            // TODO: Account for bin and mac os - cmd.exe = windows
            Process myChildProcess = Runtime.getRuntime().exec("cmd.exe /c start bash bash-api.sh");

            // TODO: Refactor further
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            ClinicService.readValidatedClinic();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                1000 
            );
        }
        catch (Exception e){
           System.out.println("Error: " + e);
        }
        */
        // ------------------------------------------------------------
    }

    public void deleteClinic() {
        payloadDoc = getClinicDocument("delete", new ClinicSchema());
        requestID = payloadDoc.getString("requestID");
        String clinicId = payloadDoc.get("clinic_id").toString();

        Document clinicToDelete = getClinicById(clinicId);

        if (clinicToDelete != null) {
            try {
                payloadDoc = clinicToDelete; // TODO: Find a better code solution for this
                DatabaseManager.clinicsCollection.deleteOne(clinicToDelete);
            } catch (Exception exception) {
                status = "500";
            }
        } else {
            status = "404";
        }
    }

    public void getOneClinic() {
        payloadDoc = getClinicDocument("getOne", new ClinicSchema());
        requestID = payloadDoc.get("requestID").toString();
        String clinicId = payloadDoc.get("clinic_id").toString();

        Document clinic = getClinicById(clinicId);

        if (clinic != null) {
            try {
                Gson gson = new Gson();
                clinicsData = gson.toJson(clinic);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                status = "500";
            }
        } else {
            status = "404";
        }
    }

    public void getAllClinics() {
        Document retrievedPayloadDoc = getClinicDocument("getAll", new ClinicSchema());
        FindIterable<Document> allRegisteredClinics = DatabaseManager.clinicsCollection.find();

        requestID = retrievedPayloadDoc.get("requestID").toString();

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
            System.out.println(e.getMessage());
            status = "500";
        }
    }

    public void addEmployee() {
        updateClinicEmployees("add");
    }

    public void removeEmployee() {
        updateClinicEmployees("remove");
    }

    // Register or delete clinic from system
    private Document getClinicDocument(String operation, CollectionSchema collectionSchema) { // TODO: Replace this method in the method below where we use EmploymentSchema
        CollectionSchema clinicObject = new ClinicSchema();
        clinicObject.assignAttributesFromPayload(payload, operation);
        return clinicObject.getDocument();
    }

     // Accounts for addition and removal of dentists
    private void updateClinicEmployees(String operation) { // TODO: Refactor further according to single responsibility principle
        EmploymentSchema employmentObject = new EmploymentSchema();
        employmentObject.assignAttributesFromPayload(payload, operation);

        payloadDoc = employmentObject.getDocument();
        requestID = payloadDoc.remove("requestID").toString();

        String clinicId = payloadDoc.get("clinic_id").toString();
        
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

                    if (dentistIdx != -1) { // If the dentist to delete exists in database
                        employees.remove(dentistIdx);

                    } else {
                        status = "404";
                    }
                }

                updateDoc.replace("employees", employees);

                DatabaseManager.updateInstanceByAttributeFilter("clinic_id", clinicId, updateDoc);

                String employeeOperation = operation.equals("add") ? "added to" : "removed from";
                System.out.println("Employee successfully " + employeeOperation +  " clinic");
            }
            catch (Exception exception) {
                status = "500";
            }
        } else {
            status = "404";
        }
    }

    // Publishes a JSON message to an external component ('Dentist API' or 'Patient API')
    private void publishMessage() {
        if (publishMessage != "-1") {
            MqttMain.publish(publishTopic, publishMessage);
        } else {
            System.out.println("Status 404 - Did not find DB-instance based on the given topic");
        }
    }

    /*
     Direct the codeflow to the method that deals with the
     requested operation specified in the mqtt subscription-topic.
     This method returns the corresponding topic to publish to,
     based on the performed operation.
    */
    private String runRequestedMethod() { // TODO: Impose a more strict structure of the topics such that we don't have to manually assign 'publishTopic' in each if-statement, but rather adding substring with a StringBuilder
        String publishTopic = "-1";
        status = "200";

        // TODO:
        // 1) Refactor away the if-statements below
        // 2) Each action below is defined as the last substring of the topic. Use a general pattern check in the if-statements

        // Register clinic
        if (topic.contains(MqttMain.clinicTopicKeywords[1])) {
            registerClinic();
            publishTopic = "grp20/res/dental/clinics/register";
        }
        // Add dentist to clinic
        else if (topic.contains(MqttMain.clinicTopicKeywords[3])) {
            addEmployee();
            publishTopic = "grp20/res/dental/clinics/add";
        }
        // Delete dentist from clinic
        else if (topic.contains(MqttMain.clinicTopicKeywords[4])) {
            removeEmployee();
            publishTopic = "grp20/res/dental/clinics/remove";
        }
        else if (topic.contains(MqttMain.clinicTopicKeywords[5])) {
            deleteClinic();
            publishTopic = "grp20/res/dental/clinics/delete";   
        }
        else if (topic.contains("all")) {
            getAllClinics();
            publishTopic = "grp20/dental/res/clinics/get/all";
        }
        else if (topic.contains("get")) {
            getOneClinic();
            publishTopic = "grp20/dental/res/clinics/get/one";
        }

        parsePublishMessage();
        System.out.println(publishMessage);

        return publishTopic;
    }

    @Override
    public void parsePublishMessage() {
        if (clinicsData.equals("-1")) {
            publishMessage = PayloadParser.parsePublishMessage(payloadDoc, requestID, status);
        } else {
            publishMessage = PayloadParser.parsePublishMessage(clinicsData, requestID, status);
        }
    }

    private Document getClinicById(String clinicId) {
        return DatabaseManager.clinicsCollection.find(eq("clinic_id", clinicId)).first();
    }
}
