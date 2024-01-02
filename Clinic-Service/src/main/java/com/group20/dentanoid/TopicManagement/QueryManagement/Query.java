package com.group20.dentanoid.TopicManagement.QueryManagement;
import org.bson.Document;

import com.group20.dentanoid.TopicManagement.TopicOperator;

public interface Query extends TopicOperator {
    public void queryDatabase();
    public String parsePublishMessage(Document payloadDoc, String operation);
}
