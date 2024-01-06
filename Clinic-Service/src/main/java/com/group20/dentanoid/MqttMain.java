package com.group20.dentanoid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.group20.dentanoid.Utils.MqttUtils;

public class MqttMain {
    private static final String broker = "tcp://broker.hivemq.com:1883";
    private static MqttClient client;

    static MemoryPersistence persistence = new MemoryPersistence();
    private static final String[] subscriptions = MqttUtils.getAllSubscriptions();
    private static final int qos = 0;

    // Create new instances of MqttMain that are mapped to their respective topics
    public static void initializeMqttConnection() throws MqttException {
        getInstance().subscribe(subscriptions);
    }

    public void publishMessage(String topic, String content) {
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
    }

    public static MqttClient getInstance() throws MqttException {
        if (client == null) {
            String username = "username";
            String password = "password";
            String clientid = MqttClient.generateClientId();
            client = new MqttClient(broker, clientid, persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
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
    }

    public static MqttClient getClient() {
        return client;
    }
}
