package org.example;

import java.util.Map;

import org.bson.Document;
import org.example.DatabaseManagement.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabaseConnection();

        Document testToc = new Document("attribute1", 438);
        DatabaseManager.clinicsCollection.insertOne(testToc);
    }
}