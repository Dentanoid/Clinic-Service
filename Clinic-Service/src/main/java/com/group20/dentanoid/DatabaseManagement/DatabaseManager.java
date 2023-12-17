package com.group20.dentanoid.DatabaseManagement;

import static com.mongodb.client.model.Filters.eq;

import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.group20.dentanoid.Utils.Entry;
import com.group20.dentanoid.Utils.Utils;

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

    /*
        FUNCTION:
        * Perform a linear search on a specific attribute inside of a db-instance that contains multiple documents and attributes.
        * Returns an integer to confirm the existence and the location of the inputted attribute.
    
        PARAMETERS:
        * instanceInDB: An existing document in the database containing multiple attributes and objects.
        * listParentName: The name of the attribute inside of 'instanceInDB' that contains a list of documents.
        * attributeName: The name of the attribute to be found inside of 'listParentName'.
        * queryValue: The value inside of the document that we are searching for.
    */
    public static int getIndexOfNestedInstanceList(Document instanceInDB, String listParentName, String attributeName, String queryValue) {
        List<Document> listParent = (List<Document>) instanceInDB.get(listParentName);

        String currentId = "-1";
        Integer i = -1;
        while (i < listParent.size() - 1 && !currentId.equals(queryValue)) {
            currentId = listParent.get(++i).get(attributeName).toString();
            System.out.println(currentId);
        }

        return currentId.equals(queryValue) ? i : -1;
    }

    /*
       Update instances in the database by first defining a query whose values matches the documents to be
       updated, and then replacing their content with what's contained in the'newContent' parameter
     */
    public static void updateInstanceByAttributeFilter(String attributeName, String filterValue, Document newContent) {
        Bson query = eq(attributeName, filterValue);
        DatabaseManager.clinicsCollection.replaceOne(query, newContent);
    }

    public static void replaceDocument(Document docA, Document docB) {
        docA.clear();
        docA.putAll(docB);
    }
}
