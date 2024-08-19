package com.restaurant.management.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Authentication {

    private UserDAO userDAO;

    public Authentication() {
        userDAO = new UserDAO();
    }

    public boolean login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            String hashedPassword = hashPassword(password);
            return hashedPassword.equals(user.getPasswordHash());
        }
        return false;
    }

    public void register(String username, String password, String role) throws SQLException, NoSuchAlgorithmException {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashPassword(password));
        user.setRole(role);
        userDAO.addUser(user);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
