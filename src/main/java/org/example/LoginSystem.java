package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class LoginSystem {
    private Map<String, User> userDatabase = new HashMap<>();
    private final String filePath = "users.txt"; // File to store user data

    public LoginSystem() {
        // Load users from file when the system starts
        loadUsersFromFile();
    }

    public void addUser(String username, String password, Role role) throws NoSuchAlgorithmException, IOException {
        String hashedPassword = hashPassword(password);
        userDatabase.put(username, new User(username, hashedPassword, role));
        saveUsersToFile(); // Save the updated user database to the file
    }

    public boolean authenticate(String username, String password) throws NoSuchAlgorithmException {
        User user = userDatabase.get(username);
        return user != null && user.getHashedPassword().equals(hashPassword(password));
    }

    public Role getUserRole(String username) {
        User user = userDatabase.get(username);
        return user != null ? user.getRole() : null;
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

    // Save user data to a file
    private void saveUsersToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : userDatabase.values()) {
                writer.write(user.getUsername() + "," + user.getHashedPassword() + "," + user.getRole());
                writer.newLine();
            }
        }
    }

    // Load user data from a file
    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String hashedPassword = parts[1];
                    Role role = Role.valueOf(parts[2]);
                    userDatabase.put(username, new User(username, hashedPassword, role));
                }
            }
        } catch (IOException e) {
            System.out.println("No existing user data found. Starting fresh.");
        }
    }
}
