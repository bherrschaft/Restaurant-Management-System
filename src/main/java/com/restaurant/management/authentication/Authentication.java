package com.restaurant.management.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Authentication {

    private UserDAO userDAO;

    // Constructor
    public Authentication() {
        userDAO = new UserDAO();
    }

    // Login Method
    public boolean login(String username, String password) throws SQLException, NoSuchAlgorithmException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            String hashedPassword = hashPassword(password);
            if (hashedPassword.equals(user.getPasswordHash())) {
                System.out.println("Login successful for user: " + username);
                return true;
            }
        }
        System.out.println("Login failed.");
        return false;
    }

    // Role-Based Permission Check Method
    public boolean hasPermission(String username, String requiredRole) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        return user != null && user.getRole().equalsIgnoreCase(requiredRole);
    }

    // Example of a Role-Specific Task for Managers
    public boolean performManagerTask(String username) throws SQLException {
        if (hasPermission(username, "manager")) {
            // Manager-specific functionality here
            System.out.println("Manager task performed for: " + username);
            return true;
        }
        System.out.println("Access denied: User " + username + " is not a manager.");
        return false;
    }

    // User Initialization Method
    public void initUsers() throws SQLException, NoSuchAlgorithmException {
        // Initializing users
        User user = new User();
        String userName = "Brittany";
        String user1pass = "pass";
        if (userDAO.getUserByUsername(userName) == null) {
            user.setUsername(userName);
            user.setPasswordHash(hashPassword(user1pass));
            user.setRole("staff");
            userDAO.addUser(user);
        }

        User user2 = new User();
        String user2Name = "Brandon";
        String user2pass = "pass";
        if (userDAO.getUserByUsername(user2Name) == null) {
            user2.setUsername(user2Name);
            user2.setPasswordHash(hashPassword(user2pass));
            user2.setRole("staff");
            userDAO.addUser(user2);
        }

        User user3 = new User();
        String user3Name = "Daylen";
        String user3pass = "pass";
        if (userDAO.getUserByUsername(user3Name) == null) {
            user3.setUsername(user3Name);
            user3.setPasswordHash(hashPassword(user3pass));
            user3.setRole("staff");
            userDAO.addUser(user3);
        }

        User user4 = new User();
        String user4Name = "Lexi";
        String user4pass = "pass";
        if (userDAO.getUserByUsername(user4Name) == null) {
            user4.setUsername(user4Name);
            user4.setPasswordHash(hashPassword(user4pass));
            user4.setRole("manager");
            userDAO.addUser(user4);
        }
    }

    // New: View Sales Report Method
    public boolean viewSalesReport(String username) throws SQLException {
        if (hasPermission(username, "manager")) {
            // Simulate showing sales report
            System.out.println("Displaying sales report for user: " + username);
            return true;
        }
        System.out.println("Access denied: User " + username + " is not a manager.");
        return false;
    }

    // Password Hashing Method
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



