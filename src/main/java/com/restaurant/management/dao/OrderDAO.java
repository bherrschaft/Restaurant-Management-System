package com.restaurant.management.dao;
import com.restaurant.management.models.Order;  // Ensure this import is present
import com.restaurant.management.models.OrderItem;  // Ensure this import is present
import com.restaurant.management.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
public class OrderDAO {

    // Modified method to add an order
    public void addOrder(Order order) throws SQLException {
        // Check if the table exists
        if (!tableExists(order.getTableId())) {
            throw new SQLException("Table with ID " + order.getTableId() + " does not exist.");
        }

        // Check if each item in the order exists and has a valid quantity
        for (OrderItem item : order.getItems()) {
            if (!itemExists(item.getItemId())) {
                throw new SQLException("Item with ID " + item.getItemId() + " does not exist.");
            }
            if (item.getQuantity() <= 0) {
                throw new SQLException("Quantity for item ID " + item.getItemId() + " must be greater than 0.");
            }
        }

        // Format the date for SQLite (as a string)
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getDate());

        // Proceed with inserting the order if validations pass
        String query = "INSERT INTO Orders (table_id, total_price, status, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, order.getTableId());
            pstmt.setDouble(2, order.getTotalPrice());
            pstmt.setString(3, order.getStatus());
            pstmt.setString(4, dateString); // Set the date
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
            }

            // Add each order item to the database
            for (OrderItem item : order.getItems()) {
                addOrderItem(item, order.getOrderId());
            }
        }
    }
    private void addOrderItem(OrderItem item, int orderId) throws SQLException {
        String query = "INSERT INTO OrderItems (order_id, item_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, item.getItemId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.executeUpdate();
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setTableId(rs.getInt("table_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setStatus(rs.getString("status"));
                order.setItems(getOrderItems(order.getOrderId()));
                orders.add(order);
            }
        }
        return orders;
    }

    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setItemId(rs.getInt("item_id"));
                item.setQuantity(rs.getInt("quantity"));
                items.add(item);
            }
        }
        return items;
    }
    // Method to update the status of an order
    public void updateOrderStatus(int orderId, String status) throws SQLException {
        if (!orderExists(orderId)) {
            throw new SQLException("Order with ID " + orderId + " does not exist.");
        }

        String query = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }

    }
    public void deleteOrder(int orderId) throws SQLException {
        // First, delete all order items associated with the order
        String deleteOrderItemsQuery = "DELETE FROM OrderItems WHERE order_id = ?";
        // Then, delete the order itself
        String deleteOrderQuery = "DELETE FROM Orders WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);

            try (PreparedStatement deleteOrderItemsStmt = conn.prepareStatement(deleteOrderItemsQuery);
                 PreparedStatement deleteOrderStmt = conn.prepareStatement(deleteOrderQuery)) {

                // Delete order items
                deleteOrderItemsStmt.setInt(1, orderId);
                deleteOrderItemsStmt.executeUpdate();

                // Delete the order
                deleteOrderStmt.setInt(1, orderId);
                deleteOrderStmt.executeUpdate();

                // Commit the transaction
                conn.commit();
                // took out double throw error
            } finally {
                // Reset the auto-commit mode to true
                conn.setAutoCommit(true);
            }
        }
    }
    // Method to check if an order exists by its ID
    public boolean orderExists(int orderId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    // Method to check if a table exists by its ID
    public boolean tableExists(int tableId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Tables WHERE table_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, tableId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    // Method to check if a menu item exists by its ID
    public boolean itemExists(int itemId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Menu WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
