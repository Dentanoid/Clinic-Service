package com.group20.dentanoid;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttMain {
    private static final String broker = "tcp://broker.hivemq.com:1883";
    private static MqttClient client;

    static MemoryPersistence persistence = new MemoryPersistence();
    // Topic requirement:
    // {string1}/{string2}

    // string1 --> Define client
    // string2 --> Define action

    // Topic keywords: 'dental', {register, add, remove}


    /*
     * Represents all the general types that the codeflow deals with by inspecting
     * the topic. These can be found as subfolders (ClinicManagement,
     * QueryManagement)
     * inside 'TopicManagement' folder.
     */
    public static String[] topicArtifacts = {
        "clinics",
        "query"
    };

    /*
     * The String-arrays below consists of words that define the intended codeflow
     * of the
     * service. The codeflow depends on the subscription topic, therefore the each
     * string-element
     * below is a substring of the subscription-topics that is used to execute the
     * desired method.
     * 
     * Example: "register" --> registerClinic() in DentalClinic.java
     */

    // The defining words in the topic that forwards the codeflow to the desired
    // method of operation
    public static String[] clinicTopicKeywords = {

            /*
             * TYPE OF CLINIC: (Used in 'TopicManagement' folder)
             * If we want to extend the code to apply to more types of clinics
             * such as hospital clinics, then we add the corresponding string here
             */
            "dental",

            /*
             * CLINIC OPERATIONS: (Used in 'ClinicManagement' folder)
             */
            "register",
            "create",
            "add",
            "remove",
            "delete"
    };

    public static String[] queryTopicKeywords = {
            /*
             * TYPE OF QUERY: (Used in 'TopicManagement' folder)
             * If we want to extend the code to apply to more types of queries (beyond the
             * theme of nearby elements
             * in relative to a specified position) such as Depth First Search
             */
            "nearby",

            /*
             * QUERY OPERATIONS: (Used in 'QueryManagement' folder)
             */
            "radius",
            "fixed"
    };

    private static final String[] subscriptions = {
            // Clinics: CREATE & DELETE
            "grp20/req/dental/clinics/register",
            "grp20/req/dental/clinics/add",
            "grp20/req/dental/clinics/remove",
            "grp20/req/dental/clinics/delete",

            // Clinics: GET
            "grp20/dental/req/clinics/get/one",
            "grp20/dental/req/clinics/get/all",

            // Queries: GET
            "grp20/req/map/query/nearby/fixed/get",
            "grp20/req/map/query/nearby/radius/get"
    };

    private static int qos = 0;

    // Create new instances of MqttMain that are mapped to their respective topics
    public static void initializeMqttConnection() throws MqttException {
        getInstance().subscribe(subscriptions);
    }

    public void publishMessage(String topic, String content) {
        // String clientId = MqttClient.generateClientId();
        // MemoryPersistence persistence = new MemoryPersistence();

        // MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        // MqttConnectOptions connOpts = new MqttConnectOptions();
        // connOpts.setCleanSession(true);
        // System.out.println("Connecting to broker: " + broker);
        // sampleClient.connect(connOpts);
        // System.out.println("Connected");
        // System.out.println("Publishing message: " + content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            // Code to be executed in the new thread
            try {
                getInstance().publish(topic, message);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }

        });

        executorService.shutdown();
        System.out.println("Message published");

        /*
         * sampleClient.disconnect(); //
         * System.out.println("Disconnected"); //
         * System.exit(0); //
         */
    }

    public static MqttClient getInstance() throws MqttException {
        if (client == null) {
            String username = "username";
            String password = "password";
            String clientid = MqttClient.generateClientId();
            client = new MqttClient(broker, clientid, persistence);

            // connect options
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            // options.setConnectionTimeout(60);
            // options.setKeepAliveInterval(60);
            // setup callback
            client.setCallback(new MqttCallback() {

                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                }

                public void messageArrived(String topic, MqttMessage message) throws MqttException {

                    if (message.isRetained())
                        return;
                    System.out.println("topic: " + topic);
                    System.out.println("Qos: " + message.getQos());
                    System.out.println("message content: " + new String(message.getPayload()));

                    ClinicService.manageRecievedPayload(topic, new String(message.getPayload()));
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });

            client.connect(options);
        }
        return client;
    }

    public void subscribe(String topic) {
        try {
            getInstance().subscribe(topic, qos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void publish(String topic, String content) {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {

            try {
                getInstance().publish(topic, message);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
        System.out.println("Message published");
    }

    public static MqttClient getClient() {
        return client;
    }
}
