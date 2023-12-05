package generalPackage.TopicManagement.ClinicManagement;

import generalPackage.TopicManagement.TopicOperator;

public interface Clinic extends TopicOperator {
    public void registerClinic(String payload);
    public void deleteClinic(String payload);
    public void addEmployee(String payload);
    public void removeEmployee(String payload);
}
