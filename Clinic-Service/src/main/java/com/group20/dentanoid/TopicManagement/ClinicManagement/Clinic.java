package com.group20.dentanoid.TopicManagement.ClinicManagement;
import com.group20.dentanoid.TopicManagement.TopicArtifact;

public interface Clinic extends TopicArtifact {
    public void registerClinic();
    public void deleteClinic();
    public void addEmployee();
    public void removeEmployee();
}
