package com.restaurant.management.authentication;

import com.restaurant.management.database.DatabaseInitializer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

class UserDAOTest {

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize the database before each test
        DatabaseInitializer.initialize();  // Ensure this initializes your database, like creating tables
    }

    // Positive Test Case
    @Test
    public void testGetUserRole_Success() throws SQLException {
        UserDAO userDAO = new UserDAO();

        // Add a user to the database first
        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("hashedpassword");
        user.setRole("manager");

        userDAO.addUser(user);  // Add user to the DB

        // Test the getUserRole method
        String role = userDAO.getUserRole("testuser");

        assertEquals("manager", role);  // Verify that the role is correct
    }

    // Negative Test Case
    @Test
    public void testGetUserRole_UserNotFound() throws SQLException {
        UserDAO userDAO = new UserDAO();

        // Try retrieving a role for a non-existent user
        String role = userDAO.getUserRole("nonExistentUser");

        // We expect the role to be null
        assertNull(role, "Expected null when user does not exist");
    }

    // Edge Case Test
    @Test
    public void testGetUserRole_EmptyUsername() throws SQLException {
        UserDAO userDAO = new UserDAO();

        // Try retrieving a role for an empty username
        String role = userDAO.getUserRole("");

        // We expect the role to be null for an empty username
        assertNull(role, "Expected null when username is empty");
    }
}

