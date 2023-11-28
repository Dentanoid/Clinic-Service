package org.example.TopicManagement;
import org.example.TopicManagement.QueryManagement.NearbyClinics;
import org.example.TopicManagement.QueryManagement.Query;

public class TopicManager {

    // Idea: Create interface of 'Clinic' and 'Query' called 'TopicOperator' so that I can create that object in ClinicService.java
    public Clinic clinic;
    public Query query;
    public TopicOperator topicOperator;

    public TopicOperator getTopicOperator(String topic, String payload) {
        if (topic.contains("clinic")) {
            return getClinic(topic, payload);
        }
        else if (topic.contains("query")) {
            return getQuery(topic, payload);
        }

        return null;
    }

    public Clinic getClinic(String topic, String payload) {
        if (topic.contains("dental")) {
            return new DentalClinic(topic, payload);
        }
        return null; // This is where we extend the code to account for different types of clinics beyond the industry of teeth
    }

    public Query getQuery(String topic, String payload) {
        if (topic.contains("nearby")) {
            return new NearbyClinics(topic, payload);
        }
        return null; // This is where we extend the code for more types of query operations
    }

    public void manageTopic(String topic, String payload) {
        // clinic = getClinic(topic, payload);
        topicOperator = getTopicOperator(topic, payload);
    }
}
