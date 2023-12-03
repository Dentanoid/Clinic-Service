package org.example.TopicManagement.QueryManagement.NearbyMapQueries;

import org.example.TopicManagement.QueryManagement.Query;
import org.example.Utils.Entry;

/*
  Note for developers: This class contains the general functionalities for Nearby-Search-Queries.
    Declaring abstract methods would requrie us to use them in NearbyClinics.java (we only want to
    use them in its subclasses 'NearbyRadius.java' and 'NearbyFixed.java' while maintaining the access
    to them in NearbyClinics.java to perform general operations using polymorphism)
*/
public abstract class NearbyQuery implements Query {
    public void addPQElement(Entry element) {}

    public int getN() { return -1; }

    public void getNumberOfClinicsToQuery(String payload) {} // TODO: Delete from this class
    public void getUserPosition(String payload) {} // Change name: 'referencePosition'
}
