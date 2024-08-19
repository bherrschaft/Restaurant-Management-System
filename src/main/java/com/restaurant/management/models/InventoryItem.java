package com.restaurant.management.models;

public class InventoryItem {
    private int ingredientId;
    private String ingredientName;
    private int quantityInStock;
    private int lowStockAlertThreshold;

    // Getters and Setters
    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public int getLowStockAlertThreshold() {
        return lowStockAlertThreshold;
    }

    public void setLowStockAlertThreshold(int lowStockAlertThreshold) {
        this.lowStockAlertThreshold = lowStockAlertThreshold;
    }
}
