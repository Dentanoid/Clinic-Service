package com.group20.dentanoid.TopicManagement;

/*
    This interface is a 'super interface' of 'Clinic.java' and 'Map.java' and is the general abstraction of all
    possible operations we can derive from all artifacts of operations directly dependent on topic-subscriptions
    i.e any form of action that is performed when an external component publishes a request to this microservice
 */

public interface TopicOperator {
    public void executeRequestedOperation();
}
