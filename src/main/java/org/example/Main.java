package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            LoginSystem loginSystem = new LoginSystem();
            StaffManager staffManager = new StaffManager();

            // Pre-populate users for testing
            loginSystem.addUser("manager1", "password123", Role.MANAGER);
            loginSystem.addUser("staff1", "password456", Role.STAFF);

            Scanner scanner = new Scanner(System.in);

            // Login process
            System.out.println("Enter username: ");
            String username = scanner.nextLine();
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            if (loginSystem.authenticate(username, password)) {
                Role userRole = loginSystem.getUserRole(username);
                System.out.println("Login successful! Role: " + userRole);

                // If the user is a manager, allow access to staff management features
                if (userRole == Role.MANAGER) {
                    boolean running = true;
                    while (running) {
                        System.out.println("1. Add Staff");
                        System.out.println("2. Edit Staff");
                        System.out.println("3. Delete Staff");
                        System.out.println("4. View All Staff");
                        System.out.println("5. Logout");
                        int choice = scanner.nextInt();
                        scanner.nextLine();  // Consume newline

                        switch (choice) {
                            case 1:
                                // Add staff
                                System.out.println("Enter Staff ID: ");
                                String staffID = scanner.nextLine();
                                System.out.println("Enter Staff Name: ");
                                String staffName = scanner.nextLine();
                                System.out.println("Enter Staff Role (STAFF/MANAGER): ");
                                String roleInput = scanner.nextLine();
                                Role staffRole = Role.valueOf(roleInput.toUpperCase());
                                System.out.println("Enter Hours Worked: ");
                                int hoursWorked = scanner.nextInt();
                                scanner.nextLine();  // Consume newline
                                Staff newStaff = new Staff(staffID, staffName, staffRole, hoursWorked);
                                staffManager.addStaff(newStaff, userRole);
                                break;

                            case 2:
                                // Edit staff
                                System.out.println("Enter Staff ID to Edit: ");
                                String editStaffID = scanner.nextLine();
                                System.out.println("Enter New Name: ");
                                String newName = scanner.nextLine();
                                System.out.println("Enter New Hours Worked: ");
                                int newHoursWorked = scanner.nextInt();
                                scanner.nextLine();  // Consume newline
                                staffManager.editStaff(editStaffID, newName, newHoursWorked, userRole);
                                break;

                            case 3:
                                // Delete staff
                                System.out.println("Enter Staff ID to Delete: ");
                                String deleteStaffID = scanner.nextLine();
                                staffManager.deleteStaff(deleteStaffID, userRole);
                                break;

                            case 4:
                                // View all staff
                                for (Staff staff : staffManager.getStaffList()) {
                                    System.out.println(staff.getStaffID() + ": " + staff.getName() + " - " + staff.getRole() + " - " + staff.getHoursWorked() + " hours");
                                }
                                break;

                            case 5:
                                // Logout
                                running = false;
                                System.out.println("Logged out.");
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                } else {
                    // If the user is a regular staff member, limit their access
                    System.out.println("Access restricted. Only managers can manage staff.");
                }
            } else {
                System.out.println("Login failed. Please try again.");
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

