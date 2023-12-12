package generalPackage.Main.DatabaseManagement.Schemas;

import org.bson.Document;

public interface CollectionSchema {
    public Document getDocument();
    public void assignAttributesFromPayload(String payload); // TODO: Get rid of this
    public void assignAttributesFromPayload(String payload, boolean registerEntity);
}