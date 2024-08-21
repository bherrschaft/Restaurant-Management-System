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
        String query = "SELECT * FROM Orders WHERE DATE(order_date) = DATE('now')";
        String mostPopularItemsQuery = "SELECT item_id, COUNT(*) as item_count " +
                "FROM OrderItems " +
                "GROUP BY item_id " +
                "ORDER BY item_count DESC " +
                "LIMIT 1";
        String mostActiveTableQuery = "SELECT table_id, COUNT(*) as table_count " +
                "FROM Orders " +
                "GROUP BY table_id " +
                "ORDER BY table_count DESC " +
                "LIMIT 1";

        // File path for the report
        String reportPath = "sales_report.txt";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             PreparedStatement popularItemsPstmt = conn.prepareStatement(mostPopularItemsQuery);
             PreparedStatement mostActiveTablePstmt = conn.prepareStatement(mostActiveTableQuery);
             BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath))) {

            ResultSet rs = pstmt.executeQuery();

            // Write report header
            writer.write("-----------------------------\n");
            writer.write("Daily Sales Report\n");
            writer.write("Date: " + new Date().toString() + "\n");
            writer.write("-----------------------------\n");

            // Total Revenue Calculation
            double totalRevenue = 0;
            while (rs.next()) {
                totalRevenue += rs.getDouble("total_price");
            }

            // Write total revenue to the report
            writer.write("Total Revenue: $" + String.format("%.2f", totalRevenue) + "\n");

            // Most Popular Item Calculation
            ResultSet popularItemsRs = popularItemsPstmt.executeQuery();
            if (popularItemsRs.next()) {
                int mostPopularItemId = popularItemsRs.getInt("item_id");
                int itemCount = popularItemsRs.getInt("item_count");

                // Write the most popular item to the report
                writer.write("Most Popular Item ID: " + mostPopularItemId + " (Ordered " + itemCount + " times)\n");
            } else {
                writer.write("Most Popular Item: No items ordered today.\n");
            }

            // Table with the Most Orders Calculation
            ResultSet mostActiveTableRs = mostActiveTablePstmt.executeQuery();
            if (mostActiveTableRs.next()) {
                int mostActiveTableId = mostActiveTableRs.getInt("table_id");
                int tableCount = mostActiveTableRs.getInt("table_count");

                // Write the table with the most orders to the report
                writer.write("Most Active Table ID: " + mostActiveTableId + " (Served " + tableCount + " orders)\n");
            } else {
                writer.write("Most Active Table: No tables were served today.\n");
            }

            // Flush and close the writer
            writer.flush();

            // Output success message
            System.out.println("Sales report generated: " + reportPath);
        }
    }
}