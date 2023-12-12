package generalPackage.Main.DatabaseManagement.Schemas.Clinic;

import java.util.ArrayList;
import java.util.UUID;

import org.bson.Document;

import com.google.gson.Gson;

import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

public class ClinicSchema implements CollectionSchema {
    private String clinic_name;
    private String position;
    private String clinic_id;
    private ArrayList<String> employees;

    public ClinicSchema() { // IDEA: Run 'assignAttributesFromPayload()' in constructor
        this.clinic_name = " ";
        this.clinic_id = " ";
        this.position = " ";
        this.employees = new ArrayList<>(); 
    }

    @Override
    public Document getDocument() {
        return new Document("clinic_name", this.clinic_name)
        .append("clinic_id", this.clinic_id)
        .append("position", this.position)
        .append("employees", this.employees);
    }

    // POST: Assign values to attributes at creation
    private void registerCreateClinicData(String clinic_name, String position, String clinic_id, ArrayList<String> employees) {
        this.clinic_name = clinic_name;
        this.position = position;
        this.clinic_id = clinic_id;
        this.employees = employees;
    }

    // DELETE: Use the clinic's id to find it in the DB
    private void registerDeleteClinicData(String clinic_id) {
        this.clinic_id = clinic_id;
    }

    @Override
    public void assignAttributesFromPayload(String payload, boolean createClinic) {
        Gson gson = new Gson();
        ClinicSchema myObjTest = gson.fromJson(payload, getClass());

        if (createClinic) {
            registerCreateClinicData(
                // Data in payload
                myObjTest.clinic_name,
                myObjTest.position,

                // Data not in payload
                UUID.randomUUID().toString(),
                new ArrayList<>()
            );
        } else {
            registerDeleteClinicData(myObjTest.clinic_id);
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
