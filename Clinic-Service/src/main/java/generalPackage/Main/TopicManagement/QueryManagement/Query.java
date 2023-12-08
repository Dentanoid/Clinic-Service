package generalPackage.Main.TopicManagement.QueryManagement;
import generalPackage.Main.TopicManagement.TopicOperator;

public interface Query extends TopicOperator {
    public void queryDatabase(String payload);
}
