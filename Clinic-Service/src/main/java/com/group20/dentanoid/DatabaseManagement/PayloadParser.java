package com.group20.dentanoid.DatabaseManagement;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;
import com.group20.dentanoid.Utils.Utils;

import org.bson.Document;
import org.bson.conversions.Bson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PayloadParser {
    public static Object getAttributeFromPayload(String payload, String attributeName, CollectionSchema classSchema) {
        Gson gson = new Gson();
        CollectionSchema schemaObject = gson.fromJson(payload, classSchema.getClass());
        return schemaObject.getDocument().get(attributeName);
    }

    public static Object getObjectFromPayload(String payload, Class<?> classType) {
        Gson gson = new Gson();
        return gson.fromJson(payload, classType);
    }

    public static Object getAttributeFromDatabaseInstance(String objectId, String attributeName, MongoCollection<Document> collection) {
        Document doc = findDocumentByAttributeValue(collection, "_id", objectId);
        return doc.get(attributeName);
    }

    // Convert the payload-string to a document that can be stored in the database
    public static Document convertPayloadToDocument(String payload, CollectionSchema classSchema) {
        Gson gson = new Gson();
        CollectionSchema schemaClass = gson.fromJson(payload, classSchema.getClass());
        return schemaClass.getDocument();
    }

    // Convert a json-formatted string to a document without any schema-bounds
    public static Document convertJSONToDocument(String jsonString) {
        Document doc = Document.parse(jsonString);
        return doc;
    }

    // Get ObjectId of already existing DB-instance that has content identical to the payload
    public static String getObjectId(String payload, CollectionSchema classSchema, MongoCollection<Document> collection) {
        Document payloadDoc = convertPayloadToDocument(payload, classSchema);
        Document objectIdDoc = queryBySchema(collection, payloadDoc);
        return objectIdDoc == null ? "-1" : objectIdDoc.get("_id").toString();
    }

    // Takes payload as input and queries it according to the attributes of a schema. Returns the documents that has identical content as the payload
    public static Document queryBySchema(MongoCollection<Document> collection, Document payloadDocument) {
        Bson schemaQueryConditions = new Document(payloadDocument);
        Document result = collection.find(schemaQueryConditions).first();
        return result;
    }

    public static Document findDocumentByAttributeValue(MongoCollection<Document> collection, String attributeName, String attributeValue) {
        return collection.find(eq(attributeName, attributeValue)).first();
    }

    public static Document savePayloadDocument(String payload, CollectionSchema collectionSchema, MongoCollection<Document> collection) {
        Document payloadDoc = convertPayloadToDocument(payload, collectionSchema);

        collection.insertOne(payloadDoc);
        return payloadDoc;
    }

    public static String createJSONPayload(HashMap<String, String> map) {
        JsonObject jsonObject = new JsonObject();
        
        for (String key : map.keySet()) {
            jsonObject.addProperty(key, map.get(key));
        }

        Gson gson = new Gson();
        String payload = gson.toJson(jsonObject); // 

        return payload;
    }

    // Append the attributes that are used in every MQTT request
    public static String parsePublishMessage(Document payloadDoc, String requestID, Integer status) {
        payloadDoc.append("requestID", requestID);
        payloadDoc.append("status", status);
        return payloadDoc.toJson();
    }

    // Change the structure of the JSON by adding parent attributes
    public static String restructurePublishMessage(String payloadData, String requestID, Integer status) {
        Map<String, Object> map = new HashMap<>();

        map.put(Utils.quoteString("clinics") + ": ", payloadData);
        map.put(Utils.quoteString("requestID") + ": ", Utils.quoteString(requestID));
        map.put(Utils.quoteString("status") + ": ", status);

        return map.toString();
    }

    // Parse an array of Documents to JSON
    public static String convertDocArrToJSON(Document[] docs) {
        String finalString = "[";

        for (int i = 0; i < docs.length; i++) {
            finalString += docs[i].toJson();

            if (i != docs.length - 1) {
                finalString += ", ";
            } else {
                finalString += "]";
            }
        }

        // Catch edge case when no clinics are returned
        if (docs.length == 0) {
            finalString += "]";
        }

        return finalString;
    }
}
