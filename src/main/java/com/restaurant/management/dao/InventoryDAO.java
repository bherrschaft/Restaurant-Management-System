package com.restaurant.management.dao;

import com.restaurant.management.database.DatabaseConnection;
import com.restaurant.management.models.InventoryItem;  // Ensure this import is present
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public static List<InventoryItem> getAllInventoryItems() throws SQLException {
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


    public void initInventoryItems() throws SQLException {
        InventoryItem buns = new InventoryItem();
        InventoryItem veggieBurg = new InventoryItem();
        InventoryItem lettuce = new InventoryItem();
        InventoryItem tomato = new InventoryItem();
        InventoryItem pickles= new InventoryItem();
        InventoryItem ketchup = new InventoryItem();
        InventoryItem cheese = new InventoryItem();
        InventoryItem beef = new InventoryItem();
        InventoryItem mustard = new InventoryItem();
        InventoryItem chicken = new InventoryItem();

        if (InventoryDAO.getAllInventoryItems().size() < 10) {
            buns.setIngredientName("buns");
            buns.setQuantityInStock(10);
            buns.setLowStockAlertThreshold(2);

            veggieBurg.setIngredientName("veggie patty");
            veggieBurg.setQuantityInStock(8);
            veggieBurg.setLowStockAlertThreshold(2);

            lettuce.setIngredientName("lettuce");
            lettuce.setQuantityInStock(4);
            lettuce.setLowStockAlertThreshold(2);

            tomato.setIngredientName("tomato");
            tomato.setQuantityInStock(15);
            tomato.setLowStockAlertThreshold(2);

            pickles.setIngredientName("pickles");
            pickles.setQuantityInStock(20);
            pickles.setLowStockAlertThreshold(2);

            ketchup.setIngredientName("ketchup");
            ketchup.setQuantityInStock(16);
            ketchup.setLowStockAlertThreshold(2);

            cheese.setIngredientName("cheese");
            cheese.setQuantityInStock(18);
            cheese.setLowStockAlertThreshold(2);

            beef.setIngredientName("beef");
            beef.setQuantityInStock(13);
            beef.setLowStockAlertThreshold(2);

            mustard.setIngredientName("mustard");
            mustard.setQuantityInStock(9);
            mustard.setLowStockAlertThreshold(2);

            chicken.setIngredientName("chicken");
            chicken.setQuantityInStock(5);
            chicken.setLowStockAlertThreshold(2);

            addInventoryItem(buns);
            addInventoryItem(veggieBurg);
            addInventoryItem(lettuce);
            addInventoryItem(tomato);
            addInventoryItem(pickles);
            addInventoryItem(ketchup);
            addInventoryItem(cheese);
            addInventoryItem(beef);
            addInventoryItem(mustard);
            addInventoryItem(chicken);



        }

        /*InventoryItem flour = new InventoryItem();
        flour.setIngredientName("Flour");
        flour.setQuantityInStock(5000); // 5000 grams
        flour.setLowStockAlertThreshold(1000); // Alert if stock falls below 1000 grams

        InventoryItem sugar = new InventoryItem();
        sugar.setIngredientName("Sugar");
        sugar.setQuantityInStock(3000); // 3000 grams
        sugar.setLowStockAlertThreshold(500); // Alert if stock falls below 500 grams

        // Adding items to the inventory
        addInventoryItem(flour);
        addInventoryItem(sugar);

        System.out.println("Inventory items added successfully!");*/
    }


    public void addInventoryItem(InventoryItem item) throws SQLException {
        String query = "INSERT INTO Inventory (ingredient_name, quantity_in_stock, low_stock_alert_threshold) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, item.getIngredientName());
            pstmt.setInt(2, item.getQuantityInStock());
            pstmt.setInt(3, item.getLowStockAlertThreshold());
            pstmt.executeUpdate();
        }
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
