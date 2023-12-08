package generalPackage.Main.TopicManagement.ClinicManagement;

import java.util.List;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

import generalPackage.GoogleAPI.ValidatedClinic;
import generalPackage.Main.ClinicService;
import generalPackage.Main.MqttMain;
import generalPackage.Main.DatabaseManagement.DatabaseManager;
import generalPackage.Main.DatabaseManagement.PayloadParser;
import generalPackage.Main.DatabaseManagement.Schemas.Clinic.ClinicCreateSchema;
import generalPackage.Main.DatabaseManagement.Schemas.Clinic.ClinicDeleteSchema;
import generalPackage.Main.DatabaseManagement.Schemas.Clinic.EmploymentSchema;

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
            FileWriter file = new FileWriter("Clinic-Service\\src\\main\\java\\generalPackage\\GoogleAPI\\validatedClinic.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("JSON file created: "+jsonObject);
        
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
        // ------------------------------------------------------------
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
