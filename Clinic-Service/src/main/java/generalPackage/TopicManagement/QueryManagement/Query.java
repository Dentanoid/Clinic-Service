package generalPackage.TopicManagement.QueryManagement;
import generalPackage.TopicManagement.TopicOperator;

public interface Query extends TopicOperator {
    public void queryDatabase(String payload);
}
