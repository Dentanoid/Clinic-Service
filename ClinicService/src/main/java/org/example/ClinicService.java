package org.example;

import java.util.Map;

import org.bson.Document;
import org.example.DatabaseManagement.DatabaseManager;

public class ClinicService {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabaseConnection();
        MqttMain.initializeMqttConnection();
    }

    // Once this service has recieved the payload, it has to be managed
    public static void manageRecievedPayload(String topic, String payload) {
        System.out.println("**********************************************");
        System.out.println("MANAGE RECIEVED PAYLOAD");
        System.out.println(topic);
        System.out.println(payload);
        System.out.println("**********************************************");

        /*
        // TopicManager topicManager = new TopicManager(topic, payload);
        TopicManager topicManager = new TopicManager();
        topicManager.manageTopic(topic, payload);
        */
    }
}