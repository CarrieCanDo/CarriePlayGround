package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:icecream_shop.db";

    // Method to establish a connection to the SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Example method to add a user to the database
    public void addUser(String username, String hashedPassword, String role) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Example method to authenticate a user
    public boolean authenticateUser(String username, String passwordHash) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");
                return storedPasswordHash.equals(passwordHash);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Example method to get the role of a user
    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
