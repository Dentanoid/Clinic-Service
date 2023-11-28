package org.example.TopicManagement;

public interface Clinic {
    public void executeRequestedOperation(String topic, String payload);
    public void registerClinic(String payload);
    public void addEmployee(String payload);
    public void removeEmployee(String payload);
}
