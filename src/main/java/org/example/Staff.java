package org.example;

public class Staff {
    private String staffID;
    private String name;
    private Role role;
    private int hoursWorked;

    public Staff(String staffID, String name, Role role, int hoursWorked) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.hoursWorked = hoursWorked;
    }

    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

