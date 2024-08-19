package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginSystem {
    private Map<String, User> userDatabase = new HashMap<>();

    public void addUser(String username, String password, Role role) throws NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        userDatabase.put(username, new User(username, hashedPassword, role));
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
}

