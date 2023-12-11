package generalPackage.Main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import generalPackage.GoogleAPI.ValidatedClinic;
import generalPackage.Main.DatabaseManagement.DatabaseManager;
import generalPackage.Main.DatabaseManagement.PayloadParser;
import generalPackage.Main.TopicManagement.TopicManager;

public class ClinicService {
    public static void main(String[] args) throws IOException, InterruptedException {
        DatabaseManager.initializeDatabaseConnection();

        /*
            The two methods below are temporarily used in this script and
            helps speed up the testing process for the developers.
         */
        // DatabaseManager.deleteClinicCollectionInstances();
        // DatabaseManager.deleteInstancesByAttribute(DatabaseManager.clinicsCollection, "clinic_name", "clinic654");

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


    // ------------------------ WILL BE REFACTORED SOON ----------------------------

    public static void readValidatedClinic() throws Exception {
        System.out.println("readValidatedClinic()");

        String file = "Clinic-Service\\src\\main\\java\\generalPackage\\GoogleAPI\\validatedClinic.json";
        String jsonString = readFileAsString(file);

        System.out.println("jsonString:");
        System.out.println(jsonString);

        // TODO: Refactor further
        ValidatedClinic validatedClinic = (ValidatedClinic) PayloadParser.getObjectFromPayload(jsonString, ValidatedClinic.class);
        System.out.println(validatedClinic.getRatings());
        System.out.println(validatedClinic.getTotalUserRatings());
        System.out.println(validatedClinic.getPhotoURL());
    }

    public static String readFileAsString(String file)throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}