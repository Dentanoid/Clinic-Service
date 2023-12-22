package com.group20.dentanoid;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.file.Files;
import java.nio.file.Paths;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.group20.dentanoid.GoogleAPI.ValidatedClinic;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
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
        // DatabaseManager.deleteInstancesByAttribute(DatabaseManager.clinicsCollection, "clinic_name", "TestClinic");
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


    // ------------------------ WILL BE REFACTORED SOON ----------------------------

    public static void readValidatedClinic() throws Exception {
        String jsonString = readFileAsString("Clinic-Service\\src\\main\\java\\com\\group20\\dentanoid\\GoogleAPI\\public\\validatedClinic.json");

        System.out.println("******************************");
        System.out.println(jsonString);
        System.out.println("******************************");

        // Convert JSON into a Java object:
        ValidatedClinic validatedClinic = (ValidatedClinic) PayloadParser.getObjectFromPayload(jsonString, ValidatedClinic.class);
        System.out.println(validatedClinic.getClinicName());
        System.out.println(validatedClinic.getRatings());
        System.out.println(validatedClinic.getTotalUserRatings());
        System.out.println(validatedClinic.getPhotoURL());
    }

    public static String readFileAsString(String file)throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}