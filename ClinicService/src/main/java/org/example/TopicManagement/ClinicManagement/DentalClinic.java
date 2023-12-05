package org.example.TopicManagement.ClinicManagement;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.MqttMain;
import org.example.DatabaseManagement.DatabaseManager;
import org.example.DatabaseManagement.PayloadParser;
import org.example.DatabaseManagement.Schemas.Clinic.ClinicDeleteSchema;
import org.example.DatabaseManagement.Schemas.Clinic.ClinicCreateSchema;
import org.example.DatabaseManagement.Schemas.Clinic.EmploymentSchema;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.BasicDBObject;

public class DentalClinic implements Clinic {
    private Document payloadDoc = null;
    
    public DentalClinic(String topic, String payload) {
        executeRequestedOperation(topic, payload);
    }

    public void executeRequestedOperation(String topic, String payload) {
        String publishTopic = "";

        // Register clinic
        if (topic.contains("register")) {
            registerClinic(payload);
            publishTopic = "pub/dentist/clinic/register";
        }
        // Add dentist to clinic
        else if (topic.contains("add")) {
            addEmployee(payload);
            publishTopic = "pub/dental/clinic/dentist/add";
        }
        // Delete dentist from clinic
        else if (topic.contains("remove")) {
            removeEmployee(payload);
            publishTopic = "pub/dental/clinic/dentist/remove";
        }
        else if (topic.contains("delete")) {
            deleteClinic(payload);
            publishTopic = "pub/dental/clinic/delete";   
        }

        if (payloadDoc != null) {
            MqttMain.subscriptionManagers.get(topic).publishMessage(publishTopic, payloadDoc.toJson());
        } else {
            System.out.println("Status 404 - Did not find DB-instance based on the given topic");
        }
    }

    public void registerClinic(String payload) {
        System.out.println("Store new registered clinic!");
        payloadDoc = PayloadParser.savePayloadDocument(payload, new ClinicCreateSchema(), DatabaseManager.clinicsCollection);
    }

    public void deleteClinic(String payload) {
        System.out.println("Delete clinic!");
        String objectId =  PayloadParser.getObjectId(payload, new ClinicDeleteSchema(), DatabaseManager.clinicsCollection);

        if (objectId != "-1") {
            payloadDoc = PayloadParser.findDocumentById(objectId, DatabaseManager.clinicsCollection);
            DatabaseManager.clinicsCollection.findOneAndDelete(payloadDoc);
        } else {
            System.out.println("Requested item does not exist in DB");
        }
    }

    public void addEmployee(String payload) { // NOTE: Refactor addEmploye() and removeEmployee() later
        Object clinicName = PayloadParser.getAttributeFromPayload(payload, "clinic_name", new EmploymentSchema());
        Object newEmployeeName = PayloadParser.getAttributeFromPayload(payload, "employee_name", new EmploymentSchema());

        Document myDoc = DatabaseManager.clinicsCollection.find(eq("clinic_name", clinicName)).first();
        Document myDoc2 = DatabaseManager.clinicsCollection.find(eq("clinic_name", clinicName)).first();

        // DB-Instance was found
        if (myDoc != null) {
            List<String> employeesNew = (List<String>)myDoc2.get("employees");
            employeesNew.add((String)newEmployeeName);
            myDoc2.replace("employees", employeesNew);

            DatabaseManager.clinicsCollection.replaceOne(myDoc, myDoc2);
            System.out.println("Employee successfully added to clinic");
        } else {
            System.out.println("The employee in the clinic wasn't found");
        }
    }

    public void removeEmployee(String payload) { // NOTE: Refactor addEmploye() and removeEmployee() later
        Object clinicName = PayloadParser.getAttributeFromPayload(payload, "clinic_name", new EmploymentSchema());
        Object employeeToDelete = PayloadParser.getAttributeFromPayload(payload, "employee_name", new EmploymentSchema());

        Document myDoc = DatabaseManager.clinicsCollection.find(eq("clinic_name", clinicName)).first();
        Document myDoc2 = DatabaseManager.clinicsCollection.find(eq("clinic_name", clinicName)).first();

        // DB-Instance was found
        if (myDoc != null) {
            List<String> employees = (List<String>)myDoc2.get("employees");
            employees.remove(employeeToDelete);
            myDoc2.replace("employees", employees);

            DatabaseManager.clinicsCollection.replaceOne(myDoc, myDoc2);
            System.out.println("Employee successfully removed from clinic");
        } else {
            System.out.println("The employee in the clinic wasn't found");
        }
    }
}