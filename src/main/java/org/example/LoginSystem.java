package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.regex.Pattern;

public class LoginSystem {
    private Connection connection;

    public LoginSystem() {
        try {
            // Establish SQLite connection by default
            connection = DriverManager.getConnection("jdbc:sqlite:icecream_shop.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Allow injection of a mock connection for testing
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void addUser(String username, String password, Role role) {
        if (!isValidUsername(username)) {
            System.out.println("Invalid username format.");
            return;
        }

        try {
            String hashedPassword = hashPassword(password);
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.setString(3, role.toString());
                pstmt.executeUpdate();
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("password").equals(hashedPassword);
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Role getUserRole(String username) {
        try {
            String sql = "SELECT role FROM users WHERE username = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return Role.valueOf(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Validate username using Regex
    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9_]{5,15}$";
        return Pattern.matches(regex, username);
    }
}
