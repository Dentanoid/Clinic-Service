package org.example.TopicManagement;
import org.example.TopicManagement.ClinicManagement.Clinic;
import org.example.TopicManagement.ClinicManagement.DentalClinic;
import org.example.TopicManagement.QueryManagement.Query;
import org.example.TopicManagement.QueryManagement.NearbyMapQueries.NearbyClinics;

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
            Query testQuery = new NearbyClinics(topic, payload);
            testQuery.executeRequestedOperation(topic, payload);
            return new NearbyClinics(topic, payload);
        }
        return null; // This is where we extend the code for more types of query operations
    }

    public void manageTopic(String topic, String payload) {
        topicOperator = getTopicOperator(topic, payload);
    }
}
