package com.group20.dentanoid.TopicManagement;

// This interface is a 'super interface' of 'Clinic.java' and 'Query.java'
// and is the general abstraction of all possible operations we can derive
// from the topic:
// Clinic operations: Clinic.java --> DentalClinic.java
// Query operations: Query.java --> NearbyClinics.java
public interface TopicOperator {
    public void executeRequestedOperation();
}
