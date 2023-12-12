package com.group20.dentanoid.Main.TopicManagement.ClinicManagement;
import com.group20.dentanoid.Main.TopicManagement.TopicOperator;

public interface Clinic extends TopicOperator {
    public void registerClinic();
    public void deleteClinic();
    public void addEmployee();
    public void removeEmployee();
}
