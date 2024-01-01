package com.group20.dentanoid.BackendMapAPI;
import java.util.ArrayList;

import org.bson.Document;

public class ValidatedClinic { // TODO: Rename to ValidatedClinicManager.java?
    // Regular clinic attributes
    private String clinic_name;
    private String clinic_id;
    private String position;
    private ArrayList<String> employees;

    // Additional data for existing clinics
    private String ratings;
    private String photoURL;
    private String address;

    private Integer status;

    public ValidatedClinic() {
    }

    public String getClinicName() {
        return this.clinic_name;
    }

    public String getClinicId() {
        return this.clinic_id;
    }

    public String getPosition() {
        checkPositionFormat();
        return this.position;
    }

    // Catch the case where dental clinic owner is oblivious and seperated lat,lng with an extra space
    private void checkPositionFormat() {
        String[] coordinates = this.position.split(",");
        String latitudeStartCharacter = String.valueOf(coordinates[1].charAt(0));

        if (latitudeStartCharacter.equals(" ")) {
            coordinates[1] = coordinates[1].substring(1);
        }

        this.position = String.join(",", coordinates);
    }

    public ArrayList<String> getEmployees() {
        return this.employees;
    }

    public String getRatings() {
        return this.ratings;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getAddress() {
        return this.address;
    }

    public void assignDataAttributes(Document payloadDoc, boolean initialize) {
        String ratingsValue = initialize ? "-1" : this.getRatings();
        String photoValue = initialize ? "-1" : this.getPhotoURL();
        String addressValue = initialize ? "-1" : this.getAddress();

        payloadDoc.append("ratings", ratingsValue);
        payloadDoc.append("photoURL", photoValue);
        payloadDoc.append("address", addressValue);
    }

    public void removeDataAttributes(Document payloadDoc) {
        payloadDoc.remove("ratings");
        payloadDoc.remove("photoURL");
        payloadDoc.remove("address");
    }
}
