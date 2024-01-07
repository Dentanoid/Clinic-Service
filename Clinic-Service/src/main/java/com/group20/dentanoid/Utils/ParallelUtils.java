package com.group20.dentanoid.Utils;
import org.bson.Document;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.group20.dentanoid.BackendMapAPI.ValidatedClinic;
import com.group20.dentanoid.DatabaseManagement.DatabaseManager;
import com.group20.dentanoid.DatabaseManagement.PayloadParser;
import com.group20.dentanoid.TopicManagement.ClinicManagement.Dental.DentalClinic;

public class ParallelUtils {
    private static String communicationData;

    public static void startChildProcess() {
        try {
            String os = OSValidator.getOperatingSystem();
            if (os.equals("Windows")) {
                Runtime.getRuntime().exec("cmd.exe /c start bash childprocess-api.sh");
            } else {
                Runtime.getRuntime().exec("/bin/bash /c childprocess-api.sh"); // If error, try "/bin/bash /c start bash childprocess-api.sh"
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void instantiateChildProcess(Document communicationDoc, String communicationFilePath) {
        try {
            Utils.writeToFile(communicationFilePath, communicationDoc.toJson());

            startChildProcess();
            // Runtime.getRuntime().exec("cmd.exe /c start bash childprocess-api.sh"); // Start child process
            responseHandler(communicationDoc, communicationFilePath);
        }
        catch (Exception e){
           System.out.println("Error: " + e);
        }
    }

    /*
        * The responsehandler is executed in a seperate thread and listens for a
          response that the child process has induced from BackendMapAPI.
     */
    private static void responseHandler(Document communicationDoc, String communicationFilePath) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable parallelTask = new Runnable() {
        public void run() {
            Integer childProcessResponseStatus;
            try {
                childProcessResponseStatus = getChildProcessStatus(communicationFilePath);

                /*
                    Since status isn't 500, the system is still running. Status 200 implies that the Backend API
                    found the clinic as an already existing coorporation, and 404 signifies that no such clinic was
                    found and that a 'fictitious' clinic without data such as ratings and address is created.
                 */
                if (childProcessResponseStatus == 200 || childProcessResponseStatus == 404) {
                    try {
                        Gson gson = new Gson();
                        ValidatedClinic clinicResponseObj = gson.fromJson(communicationData, ValidatedClinic.class);

                        if (childProcessResponseStatus == 200) { // Only append the additional values to the clinics that has them: Assign their respective values
                            clinicResponseObj.assignDataAttributes(communicationDoc, false);
                        } else { // Delete the variables that were temporarily used in the JSON communication with BackendMapAPI
                            clinicResponseObj.removeDataAttributes(communicationDoc);
                        }

                        communicationDoc.remove("status");
                        DatabaseManager.clinicsCollection.insertOne(communicationDoc);
                    }
                    catch (Exception exception) {
                        System.out.println(exception.getMessage());
                    }
                    executor.shutdown();
                    DentalClinic.publishToExternalComponent("register");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        };

        executor.scheduleAtFixedRate(parallelTask, 0, 100, TimeUnit.MILLISECONDS);
    }

    /*
        Read the the 'status' attribute of the JSON file used for communication
        between the two entities.
     */
    private static Integer getChildProcessStatus(String communicationFilePath) throws Exception {

        try {
            communicationData = Utils.readFile(communicationFilePath);
            ValidatedClinic clinicQueryResult = (ValidatedClinic)PayloadParser.getObjectFromPayload(communicationData, ValidatedClinic.class);
            return clinicQueryResult.getStatus();
        } catch (Exception e) {
            System.out.println("Could not read file");
            return 404;
        }
    }
}
