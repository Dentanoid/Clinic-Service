package com.group20.dentanoid.DatabaseManagement.Schemas;

import org.bson.Document;

public interface CollectionSchema {
    public Document getDocument();
    public void assignAttributesFromPayload(String payload); // TODO: Get rid of this
    public void assignAttributesFromPayload(String payload, String operation);
}