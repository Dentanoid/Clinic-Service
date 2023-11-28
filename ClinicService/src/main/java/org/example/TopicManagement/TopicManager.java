package org.example.TopicManagement;

public class TopicManager {
    public Clinic clinic;

    public Clinic getClinic(String topic, String payload) {
        if (topic.contains("dental")) {
            return new DentalClinic(topic, payload);
        }
        return null; // This is where we extend the code to account for different types of clinics beyond the industry of teeth
    }

    public void manageTopic(String topic, String payload) {
        clinic = getClinic(topic, payload);
    }
}
