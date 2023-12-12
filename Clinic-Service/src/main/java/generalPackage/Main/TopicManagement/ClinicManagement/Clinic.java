package generalPackage.Main.TopicManagement.ClinicManagement;
import generalPackage.Main.TopicManagement.TopicOperator;

public interface Clinic extends TopicOperator {
    public void registerClinic();
    public void deleteClinic();
    public void addEmployee();
    public void removeEmployee();
}
