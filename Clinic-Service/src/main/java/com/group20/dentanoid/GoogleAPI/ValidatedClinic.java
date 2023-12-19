package com.group20.dentanoid.GoogleAPI;

import java.util.ArrayList;

public class ValidatedClinic {
    // Regular clinic attributes
    private String clinic_name;
    private String clinic_id;
    private String position;
    private ArrayList<String> employees;

    // Google API validation attributes
    private float ratings;
    private Integer total_user_ratings;
    private String photoURL;

    public ValidatedClinic() {
    }

    public String getClinicName() {
        return this.clinic_name;
    }

    public String getClinicId() {
        return this.clinic_id;
    }

    public String getPosition() {
        return this.position;
    }

    public ArrayList<String> getEmployees() {
        return this.employees;
    }

    public float getRatings() {
        return this.ratings;
    }

    public Integer getTotalUserRatings() {
        return this.total_user_ratings;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }
}
