package com.group20.dentanoid;

import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.TopicManagement.TopicManager;

public class ClinicService {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabaseConnection();

        // DatabaseManager.deleteClinicCollectionInstances(); // <-- For developers when testing
        // DatabaseManager.deleteInstancesByAttribute(DatabaseManager.clinicsCollection, "clinic_name", "Ds334535sgg"); // <-- For developers when testing

        MqttMain.initializeMqttConnection();
    }

    // Once this service has recieved the payload, it has to be managed
    public static void manageRecievedPayload(String topic, String payload) {
        System.out.println("**********************************************");
        System.out.println("MANAGE RECIEVED PAYLOAD");
        System.out.println(topic);
        System.out.println(payload);
        System.out.println("**********************************************");

        TopicManager topicManager = new TopicManager();
        topicManager.manageTopic(topic, payload);
    }
}