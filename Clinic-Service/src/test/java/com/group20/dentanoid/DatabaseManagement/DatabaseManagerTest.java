package com.group20.dentanoid.DatabaseManagement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {
    @Test
    void databaseConnectionUnitTest() {
        DatabaseManager.initializeDatabaseConnection();
        assertNotNull(DatabaseManager.clinicsCollection);
    }
}