package org.example;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {
    private List<Staff> staffList = new ArrayList<>();
    private final String filePath = "staff.txt"; // File to store staff data

    public StaffManager() {
        // Load staff data from file when the system starts
        loadStaffFromFile();
    }

    public void addStaff(Staff staff, Role userRole) {
        if (userRole == Role.MANAGER) {
            staffList.add(staff);
            saveStaffToFile(); // Save the updated staff list to the file
        } else {
            System.out.println("Permission denied. Only managers can add staff.");
        }
    }

    public void deleteStaff(String staffID, Role userRole) {
        if (userRole == Role.MANAGER) {
            staffList.removeIf(staff -> staff.getStaffID().equals(staffID));
            saveStaffToFile(); // Save the updated staff list to the file
        } else {
            System.out.println("Permission denied. Only managers can delete staff.");
        }
    }

    public void editStaff(String staffID, String newName, int newHoursWorked, Role userRole) {
        if (userRole == Role.MANAGER) {
            for (Staff staff : staffList) {
                if (staff.getStaffID().equals(staffID)) {
                    staff.setName(newName);
                    staff.setHoursWorked(newHoursWorked);
                    saveStaffToFile(); // Save the updated staff list to the file
                    break;
                }
            }
        } else {
            System.out.println("Permission denied. Only managers can edit staff.");
        }
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    // Save staff data to a file
    private void saveStaffToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Staff staff : staffList) {
                writer.write(staff.getStaffID() + "," + staff.getName() + "," + staff.getRole() + "," + staff.getHoursWorked());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load staff data from a file
    private void loadStaffFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String staffID = parts[0];
                    String name = parts[1];
                    Role role = Role.valueOf(parts[2]);
                    int hoursWorked = Integer.parseInt(parts[3]);
                    staffList.add(new Staff(staffID, name, role, hoursWorked));
                }
            }
        } catch (IOException e) {
            System.out.println("No existing staff data found. Starting fresh.");
        }
    }
}
