package org.example.DatabaseManagement;

import java.util.Iterator;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
// import org.example4.DatabaseManagement.Schemas.Appointments; //org.example4 = org.example
// import org.example4.DatabaseManagement.Schemas.CollectionSchema;

import static com.mongodb.client.model.Filters.eq;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {
    public static MongoClient client;
    public static MongoDatabase clinicDatabase;    
    public static MongoCollection<Document> clinicsCollection;
    public static MongoCollection<Document> utilsCollection;

    public static void initializeDatabaseConnection() {
        client = MongoClients.create("mongodb+srv://DentistUser:1234@dentistsystemdb.7rnyky8.mongodb.net/?retryWrites=true&w=majority");
        clinicDatabase = client.getDatabase("ClinicService");
        clinicsCollection = clinicDatabase.getCollection("Clinics");
        utilsCollection = clinicDatabase.getCollection("Utils");
    }

    // A temporary method for the developers to delete everything to save time in development process
    public static void deleteClinicCollectionInstances() {
        clinicsCollection.deleteMany(new Document());
    }

    public static void printAllAttributesOfCollection(MongoCollection<Document> collection, String attributeName) {
        FindIterable<Document> documents = collection.find();
        Iterator<Document> it = documents.iterator();
        while (it.hasNext()) {
            Document doc = it.next();
            System.out.println(doc.get(attributeName));
            System.out.println(doc.toJson());
        }
    }
}
