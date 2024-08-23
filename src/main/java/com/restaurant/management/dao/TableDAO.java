package com.restaurant.management.dao;
import com.restaurant.management.models.Table;  // Ensure this import is present
import com.restaurant.management.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {

    public List<Table> getAllTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        String query = "SELECT * FROM Tables";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Table table = new Table();
                table.setTableId(rs.getInt("table_id"));
                table.setSize(rs.getInt("size"));
                table.setStatus(rs.getString("status"));
                tables.add(table);
            }
        }
        return tables;
    }

    public void addTable(Table table) throws SQLException {
        if (table.getSize() <=0) {
            throw new SQLException("Table size must be greater than zero.");
        }
        if (table.getSize() > 6) {
            throw new SQLException("Table size cannot exceed 6.");
        }

        String query = "INSERT INTO Tables (size, status) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, table.getSize());
            pstmt.setString(2, table.getStatus());
            pstmt.executeUpdate();

            // Retrieve the generated key (table_id)
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    table.setTableId(generatedKeys.getInt(1));  // Assign the generated table ID to the object
                } else {
                    throw new SQLException("Creating table failed, no ID obtained.");
                }
            }

        }
    }

    public int updateTableStatus(int tableId, String status) throws SQLException {
        String query = "UPDATE Tables SET status = ? WHERE table_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Debugging: Print the table ID and new status before execution
            System.out.println("Updating table with ID: " + tableId + " to status: " + status);

            pstmt.setString(1, status);
            pstmt.setInt(2, tableId);

            // Execute the update and capture the number of affected rows
            int rowsAffected = pstmt.executeUpdate();

            // Debugging: Print the number of affected rows
            System.out.println("Rows affected by update: " + rowsAffected);

            //Check if no rows were affected, meaning if the table id doesn't exist
            if (rowsAffected == 0) {
                throw new SQLException("Table with ID: " + tableId + "does not exist.");
            }

            // Return the number of rows affected to the caller
            return rowsAffected;
        }
    }


    /*
    public void updateTableStatus(int tableId, String status) throws SQLException {
        String query = "UPDATE Tables SET status = ? WHERE table_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, tableId);
            pstmt.executeUpdate();
        }
    }

     */

}
