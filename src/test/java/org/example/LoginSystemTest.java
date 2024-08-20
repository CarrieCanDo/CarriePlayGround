package org.example;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginSystemTest {
    private LoginSystem loginSystem;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock the connection, statement, and result set
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        // Create an instance of the LoginSystem and inject the mock connection
        loginSystem = new LoginSystem();
        loginSystem.setConnection(mockConnection); // You might need to expose a setter or constructor to set the mock connection
    }

    @Test
    public void testAuthenticateSuccess() throws Exception {
        // Setup the mock behavior for a successful authentication
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn("hashedPassword123");

        // Call the method under test
        boolean result = loginSystem.authenticate("user1", "password123");

        // Verify the result and mock interactions
        assertTrue(result);
        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setString(1, "user1");
        verify(mockResultSet).getString("password");
    }

    @Test
    public void testAuthenticateFailure() throws Exception {
        // Setup the mock behavior for a failed authentication
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Call the method under test
        boolean result = loginSystem.authenticate("user1", "wrongPassword");

        // Verify the result and mock interactions
        assertFalse(result);
        verify(mockConnection).prepareStatement(anyString());
        verify(mockStatement).setString(1, "user1");
    }
}
