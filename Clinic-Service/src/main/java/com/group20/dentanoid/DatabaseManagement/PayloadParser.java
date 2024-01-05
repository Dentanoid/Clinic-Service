package com.group20.dentanoid.DatabaseManagement;
import com.group20.dentanoid.DatabaseManagement.Schemas.CollectionSchema;
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
    public static String parsePublishMessage(Document payloadDoc, String requestID, String status) {
        payloadDoc.append("requestID", requestID);
        payloadDoc.append("status", status);
        return payloadDoc.toJson();
    }

    // Change the structure of the JSON by adding parent attributes
    public static String restructurePublishMessage(String payloadData, String requestID, String status) {

        System.out.println(payloadData);

        // TODO: Refactor in Utils.js
        String clinicsTest = "clinics";
        String clinicsQuoted = "\"" + clinicsTest + "\"";

        String requestIdTest = "requestID";
        String reqQuoted = "\"" + requestIdTest + "\"";

        String statusTest = "status";
        String statusQuoted = "\"" + statusTest + "\"";

        Map<String, Object> map = new HashMap<>();
        /*
        map.put("clinics", payloadData.toString());
        map.put("requestID", requestID.toString());
        map.put("status", Integer.parseInt(status));
        */

        map.put(clinicsQuoted + ": ", payloadData.toString());
        map.put(reqQuoted + ": ", requestID.toString());
        map.put(statusQuoted + ": ", Integer.parseInt(status));

        return map.toString();

        /*
        Gson gson = new Gson();
        return gson.toJson(map);
        */
    }

    // ------------------------------------------------------------------------------------

    public static String convertDocArrToJSON(Document[] docs) {

        String finalString = "[";

        // TODO: Replace with StringBuilder later
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

    public static String convertDocsToJSON(Document[] docs, String attributeName) {

        /*
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(attributeName, docs.toString());

        String finalString = "";
        System.out.println(docs[0].toJson());

        FindIterable<Document> collectionInstances = docs;
        Iterator<Document> it = collectionInstances.iterator();
        docs.to

        collectionInstances.toString()

        while (it.hasNext()) {
            Document currentInstance = it.next();
            finalString += currentInstance.toJson();
        }
        return jsonObject.toString();
        */

        System.out.println(Arrays.toString(docs));
        return Arrays.toString(docs);
    }

    public static String convertDocumentToJSON(Document doc) {
        Set<String> keys = doc.keySet();
        Iterator<String> keysIterator = keys.iterator();

        HashMap<String, Object> map = new HashMap<>();
        JsonObject jsonObject = new JsonObject();

        while(keysIterator.hasNext()) {
            String currentKey = keysIterator.next();
            // map.put(currentKey, doc.get(currentKey));
            jsonObject.addProperty(currentKey, doc.get(currentKey).toString());
        }

        /*
        System.out.println(jsonObject);
        System.out.println(jsonObject.toString());
        */

        return jsonObject.toString();
    }

    public static String convertDocumentsToJSON(Document[] docs) {
        JsonObject jsonObject = new JsonObject();
        System.out.println(docs.length);

        for (int i = 0; i < docs.length; i++) {
            Set<String> keys = docs[i].keySet();
            Iterator<String> keysIterator = keys.iterator();
            
            while(keysIterator.hasNext()) {
                String currentKey = keysIterator.next();
                jsonObject.addProperty(currentKey, docs[i].get(currentKey).toString());
            }
        }

        System.out.println(jsonObject);
        System.out.println(jsonObject.toString());

        return createJSONPayload( new HashMap<>() {{
            put("gg", "g");
        }});
    }
}
