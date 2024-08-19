package com.restaurant.management.dao;

import com.restaurant.management.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public List<InventoryItem> getAllInventoryItems() throws SQLException {
        List<InventoryItem> items = new ArrayList<>();
        String query = "SELECT * FROM Inventory";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                InventoryItem item = new InventoryItem();
                item.setIngredientId(rs.getInt("ingredient_id"));
                item.setIngredientName(rs.getString("ingredient_name"));
                item.setQuantityInStock(rs.getInt("quantity_in_stock"));
                item.setLowStockAlertThreshold(rs.getInt("low_stock_alert_threshold"));
                items.add(item);
            }
        }
        return items;
    }

    public void updateInventoryItem(InventoryItem item) throws SQLException {
        String query = "UPDATE Inventory SET quantity_in_stock = ? WHERE ingredient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, item.getQuantityInStock());
            pstmt.setInt(2, item.getIngredientId());
            pstmt.executeUpdate();
        }
    }

    public void checkLowStock() throws SQLException {
        String query = "SELECT * FROM Inventory WHERE quantity_in_stock <= low_stock_alert_threshold";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Low stock alert: " + rs.getString("ingredient_name"));
            }
        }
    }
}
