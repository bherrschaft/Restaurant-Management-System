package com.restaurant.management.dao;
import com.restaurant.management.models.Staff;  // Ensure this import is present
import com.restaurant.management.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    public void addStaff(Staff staff) throws SQLException {
        String query = "INSERT INTO Staff (name, role, hours_worked) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getRole());
            pstmt.setInt(3, staff.getHoursWorked());
            pstmt.executeUpdate();
        }
    }

    public void updateStaff(Staff staff) throws SQLException {
        String query = "UPDATE Staff SET name = ?, role = ?, hours_worked = ? WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getRole());
            pstmt.setInt(3, staff.getHoursWorked());
            pstmt.setInt(4, staff.getStaffId());
            pstmt.executeUpdate();
        }
    }

    public void deleteStaff(int staffId) throws SQLException {
        String query = "DELETE FROM Staff WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, staffId);
            pstmt.executeUpdate();
        }
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM Staff";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getInt("staff_id"));
                staff.setName(rs.getString("name"));
                staff.setRole(rs.getString("role"));
                staff.setHoursWorked(rs.getInt("hours_worked"));
                staffList.add(staff);
            }
        }
        return staffList;
    }
}
