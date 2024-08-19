package com.restaurant.management.dao;

import com.restaurant.management.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public void addOrder(Order order) throws SQLException {
        String query = "INSERT INTO Orders (table_id, total_price, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, order.getTableId());
            pstmt.setDouble(2, order.getTotalPrice());
            pstmt.setString(3, order.getStatus());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderId(rs.getInt(1));
            }

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

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String query = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }
    }
}
