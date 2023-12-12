package generalPackage.Main.TopicManagement;

import generalPackage.Main.MqttMain;
import generalPackage.Main.TopicManagement.ClinicManagement.Clinic;
import generalPackage.Main.TopicManagement.ClinicManagement.DentalClinic;
import generalPackage.Main.TopicManagement.QueryManagement.Query;
import generalPackage.Main.TopicManagement.QueryManagement.NearbyMapQueries.NearbyClinics;

public class TopicManager {
    private TopicOperator topicOperator;

    // Check whether topic requests for 'clinic' or 'query' operations
    public TopicOperator getTopicOperator(String topic, String payload) {
        if (topic.contains(MqttMain.topicArtifacts[0])) {
            return getClinic(topic, payload);
        }
        else if (topic.contains(MqttMain.topicArtifacts[1])) {
            return getQuery(topic, payload);
        }

        return null;
    }

    public Clinic getClinic(String topic, String payload) {
        if (topic.contains(MqttMain.clinicTopicKeywords[0])) {
            return new DentalClinic(topic, payload);
        }
        return null; // This is where we extend the code to account for different types of clinics beyond the industry of teeth
    }

    public Query getQuery(String topic, String payload) {
        if (topic.contains(MqttMain.queryTopicKeywords[0])) { // TODO: Refactor according to the convention followed in 'getClinic'
            Query nearbyQuery = new NearbyClinics(topic, payload);
            nearbyQuery.executeRequestedOperation(topic, payload);
            return new NearbyClinics(topic, payload);
        }
        return null; // This is where we extend the code for more types of query operations
    }

    public void manageTopic(String topic, String payload) {
        topicOperator = getTopicOperator(topic, payload);
    }
}
