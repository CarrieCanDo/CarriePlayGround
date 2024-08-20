package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class StaffManagerTest {
    private StaffManager staffManager;
    private Connection mockConnection;
    private PreparedStatement mockStatement;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock the connection and statement
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);

        // Mock the prepareStatement call to return the mocked statement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        // Create an instance of StaffManager and inject the mock connection
        staffManager = new StaffManager();
        staffManager.setConnection(mockConnection); // Inject mock connection
    }

    @Test
    public void testAddStaff() throws SQLException {
        // Simulate adding a staff member
        Staff staff = new Staff("S001", "John Doe", Role.STAFF, 40);
        staffManager.addStaff(staff, Role.MANAGER);

        // Verify that the correct SQL was prepared
        verify(mockConnection).prepareStatement("INSERT INTO staff (staffID, name, role, hoursWorked) VALUES (?, ?, ?, ?)");
        verify(mockStatement).setString(1, "S001");
        verify(mockStatement).setString(2, "John Doe");
        verify(mockStatement).setString(3, "STAFF");
        verify(mockStatement).setInt(4, 40);
        verify(mockStatement).executeUpdate();
    }

    // Additional tests can follow a similar pattern
}
