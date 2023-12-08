package generalPackage.Main.TopicManagement.QueryManagement.NearbyMapQueries;

import generalPackage.Main.TopicManagement.QueryManagement.Query;
import generalPackage.Utils.Entry;

/*
  Note for developers: This class contains the general functionalities for Nearby-Search-Queries.
    Declaring abstract methods would requrie us to use them in NearbyClinics.java (we only want to
    use them in its subclasses 'NearbyRadius.java' and 'NearbyFixed.java' while maintaining the access
    to them in NearbyClinics.java to perform general operations using polymorphism)
*/
public abstract class NearbyQuery implements Query {
    public void addPQElement(Entry element) {}
    public int getN() { return -1; }
    public void getReferencePosition(String payload) {}
    public void readPayloadAttributes(String payload) {}
}
