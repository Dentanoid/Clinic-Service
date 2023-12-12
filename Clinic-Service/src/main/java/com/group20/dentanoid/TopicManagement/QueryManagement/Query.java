package com.group20.dentanoid.Main.TopicManagement.QueryManagement;
import com.group20.dentanoid.Main.TopicManagement.TopicOperator;

public interface Query extends TopicOperator {
    public void queryDatabase(String payload);
}
