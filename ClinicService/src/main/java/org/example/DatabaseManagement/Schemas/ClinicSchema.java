package org.example.DatabaseManagement.Schemas;

import java.util.ArrayList;

import org.bson.Document;

public class ClinicSchema implements CollectionSchema {
    String clinic_name;
    String clinic_id;
    String position;
    ArrayList<String> employees;

    public ClinicSchema() {
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
}
