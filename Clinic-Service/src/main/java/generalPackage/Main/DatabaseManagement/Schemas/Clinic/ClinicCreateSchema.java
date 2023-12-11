package generalPackage.Main.DatabaseManagement.Schemas.Clinic;

import java.util.ArrayList;

import org.bson.Document;

import com.google.gson.Gson;

import generalPackage.Main.DatabaseManagement.Schemas.CollectionSchema;

public class ClinicCreateSchema implements CollectionSchema {
    private String clinic_name;
    private String position;
    private String clinic_id;
    private ArrayList<String> employees;

    public ClinicCreateSchema(String clinic_id) {
        this.clinic_name = " ";
        this.clinic_id = clinic_id;
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

    public void registerData(String clinic_name, String position, String clinic_id, ArrayList<String> employees) {
        this.clinic_name = clinic_name;
        this.position = position;
        this.clinic_id = clinic_id;
        this.employees = employees;
    }

    @Override
    public void assignAttributesFromPayload(String payload) {
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
    }
}
