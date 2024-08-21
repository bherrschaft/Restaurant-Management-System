package com.restaurant.management.models;

import com.restaurant.management.dao.MenuDAO;

import java.sql.SQLException;

public class MenuItem {
    private int itemId;
    private String itemName;
    private String description;
    private int preparationTime;
    private double price;
    private String ingredients;

    MenuDAO menuDAO = new MenuDAO();
    public MenuItem(){

    }

    public MenuItem(int id){
        this.itemId = id;
    }

    public MenuItem(String itemName, String description, int preparationTime, double price, String ingredients) {
        this.itemName = itemName;
        this.description = description;
        this.preparationTime = preparationTime;
        this.price = price;
        this.ingredients = ingredients;
    }


    public MenuItem(int itemId, String itemName, String description, int preparationTime, double price, String ingredients) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.preparationTime = preparationTime;
        this.price = price;
        this.ingredients = ingredients;
    }

    public void initMenuItems() throws SQLException {
        MenuItem vegieBurg = new MenuItem();
        if (menuDAO.getAllMenuItems() == null){
            vegieBurg.itemName = "Vegie Burger";
            vegieBurg.description = "A vegan burger";
            vegieBurg.preparationTime = 2;
            vegieBurg.price = 5.99;
            vegieBurg.ingredients = "Buns, Veggie, Lettuce, Tomato, Pickles, Ketchup";
        }
    }
    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
