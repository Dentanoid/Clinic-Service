package generalPackage.DatabaseManagement;

import java.util.Iterator;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import generalPackage.Utils.Entry;
import generalPackage.Utils.Utils;

public class DatabaseManager {
    public static MongoClient client;
    public static MongoDatabase clinicDatabase;    
    public static MongoCollection<Document> clinicsCollection;

    public static void initializeDatabaseConnection() {
        client = MongoClients.create("mongodb+srv://DentistUser:1234@dentistsystemdb.7rnyky8.mongodb.net/?retryWrites=true&w=majority");
        clinicDatabase = client.getDatabase("ClinicService");
        clinicsCollection = clinicDatabase.getCollection("Clinics");
    }

    // A temporary method for the developers to delete everything to save time in development process
    public static void deleteClinicCollectionInstances() {
        clinicsCollection.deleteMany(new Document());
    }

    public static void deleteInstancesByAttribute(MongoCollection<Document> collection, String attributeIdentifier, String queryValue) {
        FindIterable<Document> collectionInstances = collection.find();
        Iterator<Document> it = collectionInstances.iterator();

        while (it.hasNext()) {
            Document currentInstance = it.next();

            if (currentInstance.get(attributeIdentifier).toString().equals(queryValue)) {
                collection.deleteOne(currentInstance);
            }
        }
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
