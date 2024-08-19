package com.restaurant.management.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password_hash TEXT NOT NULL," +
                "role TEXT NOT NULL" +
                ");";

        String createMenuTable = "CREATE TABLE IF NOT EXISTS Menu (" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item_name TEXT NOT NULL," +
                "description TEXT," +
                "preparation_time INTEGER," +
                "price REAL," +
                "ingredients TEXT" +
                ");";

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "table_id INTEGER," +
                "total_price REAL," +
                "status TEXT NOT NULL" +
                ");";

        String createOrderItemsTable = "CREATE TABLE IF NOT EXISTS OrderItems (" +
                "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER," +
                "item_id INTEGER," +
                "quantity INTEGER" +
                ");";

        String createTablesTable = "CREATE TABLE IF NOT EXISTS Tables (" +
                "table_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "size INTEGER," +
                "status TEXT NOT NULL" +
                ");";

        String createInventoryTable = "CREATE TABLE IF NOT EXISTS Inventory (" +
                "ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ingredient_name TEXT NOT NULL," +
                "quantity_in_stock INTEGER," +
                "low_stock_alert_threshold INTEGER" +
                ");";

        String createStaffTable = "CREATE TABLE IF NOT EXISTS Staff (" +
                "staff_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "hours_worked INTEGER" +
                ");";

        String createReservationsTable = "CREATE TABLE IF NOT EXISTS Reservations (" +
                "reservation_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_name TEXT NOT NULL," +
                "reservation_time TEXT NOT NULL," +
                "table_id INTEGER" +
                ");";

        String createSpecialOffersTable = "CREATE TABLE IF NOT EXISTS SpecialOffers (" +
                "offer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT," +
                "discount_amount REAL," +
                "deal_details TEXT" +
                ");";

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createMenuTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);
            stmt.execute(createTablesTable);
            stmt.execute(createInventoryTable);
            stmt.execute(createStaffTable);
            stmt.execute(createReservationsTable);
            stmt.execute(createSpecialOffersTable);
        }
    }
}
