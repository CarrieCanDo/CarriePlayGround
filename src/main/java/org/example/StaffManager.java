package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {
    private Connection connection;

    public StaffManager() {
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

    public void addStaff(Staff staff, Role userRole) {
        if (userRole == Role.MANAGER) {
            String sql = "INSERT INTO staff (staffID, name, role, hoursWorked) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, staff.getStaffID());
                pstmt.setString(2, staff.getName());
                pstmt.setString(3, staff.getRole().toString());
                pstmt.setInt(4, staff.getHoursWorked());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Permission denied. Only managers can add staff.");
        }
    }

    public void deleteStaff(String staffID, Role userRole) {
        if (userRole == Role.MANAGER) {
            String sql = "DELETE FROM staff WHERE staffID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, staffID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Permission denied. Only managers can delete staff.");
        }
    }

    public void editStaff(String staffID, String newName, int newHoursWorked, Role userRole) {
        if (userRole == Role.MANAGER) {
            String sql = "UPDATE staff SET name = ?, hoursWorked = ? WHERE staffID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, newName);
                pstmt.setInt(2, newHoursWorked);
                pstmt.setString(3, staffID);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Permission denied. Only managers can edit staff.");
        }
    }

    public List<Staff> getStaffList() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String staffID = rs.getString("staffID");
                String name = rs.getString("name");
                Role role = Role.valueOf(rs.getString("role"));
                int hoursWorked = rs.getInt("hoursWorked");
                staffList.add(new Staff(staffID, name, role, hoursWorked));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
}
