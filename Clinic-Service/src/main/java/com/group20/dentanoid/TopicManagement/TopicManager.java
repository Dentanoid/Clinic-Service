package com.group20.dentanoid.TopicManagement;

import com.group20.dentanoid.TopicManagement.ClinicManagement.Clinic;
import com.group20.dentanoid.TopicManagement.ClinicManagement.DentalClinic;
import com.group20.dentanoid.TopicManagement.QueryManagement.Query;
import com.group20.dentanoid.TopicManagement.QueryManagement.NearbyMapQueries.NearbyClinics;
import com.group20.dentanoid.Utils.MqttUtils;

public class TopicManager {
    private TopicOperator topicOperator;

    // Return the topic artifact (clinic or query) to perform operations on
    public TopicOperator getTopicOperator(String topic, String payload) {
        if (topic.contains(MqttUtils.topicArtifacts[0])) {
            return getClinic(topic, payload);
        }
        else if (topic.contains(MqttUtils.topicArtifacts[1])) {
            return getQuery(topic, payload);
        }

        return null; // This is where we extend the code to account for more types of artifacts
    }

    public Clinic getClinic(String topic, String payload) {
        if (topic.contains(MqttUtils.clinicTypes[0])) {
            return new DentalClinic(topic, payload);
        }
        return null; // This is where we extend the code to account for different types of clinics beyond the industry of teeth
    }

    public Query getQuery(String topic, String payload) {
        if (topic.contains(MqttUtils.queryTypes[0])) {
            return new NearbyClinics(topic, payload);
        }
        return null; // This is where we extend the code for more types of query operations
    }

    public void manageTopic(String topic, String payload) {
        topicOperator = getTopicOperator(topic, payload);
        topicOperator.executeRequestedOperation();
    }
}
