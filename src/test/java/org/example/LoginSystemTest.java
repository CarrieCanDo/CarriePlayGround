package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        loginSystem.setConnection(mockConnection); // Inject the mock connection
    }

    @Test
    public void testAuthenticateSuccess() throws Exception {
        // Compute the hash of "password123" using the same logic as in LoginSystem
        String hashedPassword = hashPassword("password123");

        // Setup the mock behavior for a successful authentication
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("password")).thenReturn(hashedPassword);

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

    // Helper method to generate the correct hash for a password
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
