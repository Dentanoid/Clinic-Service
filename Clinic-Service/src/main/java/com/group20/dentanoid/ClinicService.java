package com.group20.dentanoid;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.TopicManagement.TopicManager;

public class ClinicService {
    public static void main(String[] args) throws IOException, InterruptedException, MqttException {
        DatabaseManager.initializeDatabaseConnection();
        MqttMain.initializeMqttConnection();

        /*
            The two methods below are temporarily used in this script and
            helps speed up the testing process for the developers.
        */
        // DatabaseManager.deleteClinicCollectionInstances();
        // DatabaseManager.deleteInstancesByAttribute(DatabaseManager.clinicsCollection, "clinic_name", "SmileSpace");
    }

    /*
        This method is executed from MqttMain.java once the clinic service has recieved a payload
        from one of the subscription topics. It takes two parameters 'topic' and 'payload' and
        forwards it an instance of TopicManager.java which in turn directs the codeflow to the
        correct '.java' class to perform the requested action.
    */
    public static void manageRecievedPayload(String topic, String payload) {
        TopicManager topicManager = new TopicManager();
        topicManager.manageTopic(topic, payload);
    }
}