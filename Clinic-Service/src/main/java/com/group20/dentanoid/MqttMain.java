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
    public static HashMap<String, MqttMain> subscriptionManagers; // Format: // <"subTopic", new MqttMain Object>
    private static final String broker = "tcp://broker.hivemq.com:1883";
    private static MqttClient client;

    static MemoryPersistence persistence = new MemoryPersistence();
    // Topic requirement:
    // {string1}/{string2}

    // string1 --> Define client
    // string2 --> Define action

    // Topic keywords: 'dental', {register, add, remove}

    private static final String[] subscriptions = {
            // Clinics
            "sub/dental/clinic/register", // grp20/dental/clinic/register
            "sub/dental/clinic/dentist/add", // grp20/dental/clinic/add
            "sub/dental/clinic/dentist/remove", // grp20/dental/clinic/dentist/remove
            "sub/dental/clinic/delete", // grp20/dental/clinic/delete

            // Queries
            "sub/query/map/nearby/fixed",
            "sub/query/map/nearby/radius"
    };

    int qos = 0;

    // Create new instances of MqttMain that are mapped to their respective topics
    public static void initializeMqttConnection() {
        subscriptionManagers = new HashMap<>();

        for (int i = 0; i < subscriptions.length; i++) {
            subscriptionManagers.put(subscriptions[i], new MqttMain());
            subscriptionManagers.get(subscriptions[i]).subscribe(subscriptions[i]);
        }
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
            String username = "Test";
            String password = "Test123";
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
}
