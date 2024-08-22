package com.restaurant.management.reports;

import com.restaurant.management.database.DatabaseConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SalesReport {

    public void generateDailyReport() throws SQLException, IOException {
        String dailySalesQuery = "SELECT total_price, order_id FROM Orders WHERE DATE(order_date) = DATE('now')";
        String mostPopularItemsQuery = "SELECT m.item_name, COUNT(*) AS item_count " +
                "FROM OrderItems oi " +
                "JOIN Menu m ON oi.item_id = m.item_id " +
                "GROUP BY m.item_name " +
                "ORDER BY item_count DESC LIMIT 3";

        String tableSalesQuery = "SELECT table_id, SUM(total_price) AS table_sales " +
                "FROM Orders " +
                "GROUP BY table_id " +
                "ORDER BY table_sales DESC LIMIT 3";
        String detailedOrdersQuery = "SELECT o.order_id, o.table_id, m.item_name, oi.quantity, m.price " +
                "FROM Orders o " +
                "JOIN OrderItems oi ON o.order_id = oi.order_id " +
                "JOIN Menu m ON oi.item_id = m.item_id " +
                "ORDER BY o.order_id";


        String reportPath = "sales_report.txt";

        try (Connection conn = DatabaseConnection.getConnection();
             BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath))) {

            // Write report header
            writer.write("-----------------------------\n");
            writer.write("Daily Sales Report\n");
            writer.write("Date: " + new Date().toString() + "\n");
            writer.write("-----------------------------\n");

            // Total Revenue Calculation
            double totalRevenue = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(dailySalesQuery);
                 ResultSet rs = pstmt.executeQuery()) {

                System.out.println("Checking Orders for today:");

                while (rs.next()) {
                    double orderTotal = rs.getDouble("total_price");
                    int orderId = rs.getInt("order_id");

                    // Print statement for debugging
                    System.out.println("Order ID: " + orderId + ", Total Price: " + orderTotal);

                    totalRevenue += orderTotal;
                }

                // Write total revenue to the report
                writer.write("Total Revenue: $" + String.format("%.2f", totalRevenue) + "\n");

            } catch (SQLException e) {
                System.out.println("Error executing daily sales query: " + e.getMessage());
            }

            // Most Popular Items Calculation
            writer.write("\nMost Popular Items:\n");
            try (PreparedStatement popularItemsPstmt = conn.prepareStatement(mostPopularItemsQuery);
                 ResultSet popularItemsRs = popularItemsPstmt.executeQuery()) {

                int rank = 1;
                while (popularItemsRs.next()) {
                    writer.write(rank + ". " + popularItemsRs.getString("item_name") + ": " +
                            popularItemsRs.getInt("item_count") + " orders\n");
                    rank++;
                }
            }

            // Table Sales Calculation
            writer.write("\nTable Sales:\n");
            try (PreparedStatement tableSalesPstmt = conn.prepareStatement(tableSalesQuery);
                 ResultSet tableSalesRs = tableSalesPstmt.executeQuery()) {

                int rank = 1;
                while (tableSalesRs.next()) {
                    writer.write(rank + ". Table " + tableSalesRs.getInt("table_id") + ": $" +
                            String.format("%.2f", tableSalesRs.getDouble("table_sales")) + "\n");
                    rank++;
                }
            }

            // Detailed Orders
            writer.write("\nDetailed Orders:\n");
            try (PreparedStatement detailedOrdersPstmt = conn.prepareStatement(detailedOrdersQuery);
                 ResultSet detailedOrdersRs = detailedOrdersPstmt.executeQuery()) {

                int lastOrderId = -1;
                while (detailedOrdersRs.next()) {
                    int orderId = detailedOrdersRs.getInt("order_id");
                    if (orderId != lastOrderId) {
                        writer.write("\nOrder ID: #" + String.format("%03d", orderId) + "\n");
                        writer.write("Table ID: " + detailedOrdersRs.getInt("table_id") + "\n");
                        lastOrderId = orderId;
                    }
                    writer.write(" - " + detailedOrdersRs.getString("item_name") + ": " +
                            detailedOrdersRs.getInt("quantity") + " ($" +
                            String.format("%.2f", detailedOrdersRs.getDouble("price")) + ")\n");
                }
            }

            // Flush and close the writer
            writer.flush();

            System.out.println("Sales report generated: " + reportPath);
        }
    }
}