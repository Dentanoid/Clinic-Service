package org.example.TopicManagement.QueryManagement;
import org.example.TopicManagement.TopicOperator;

public interface Query extends TopicOperator {
    public void queryDatabase(String payload, String mode);
}
