package com.group20.dentanoid.TopicManagement.ClinicManagement;

import com.group20.dentanoid.TopicManagement.TopicOperator;

public interface Clinic extends TopicOperator {
    public void registerClinic(String payload);
    public void deleteClinic(String payload);
    public void addEmployee(String payload);
    public void removeEmployee(String payload);
}
