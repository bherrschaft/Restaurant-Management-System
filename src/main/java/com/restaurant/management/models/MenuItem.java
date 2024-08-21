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
        MenuItem veggieBurg = new MenuItem();
        MenuItem cheeseBurger = new MenuItem();
        MenuItem chickenSandwich = new MenuItem();

        if (menuDAO.getAllMenuItems().size() < 5){
            veggieBurg.itemName = "Veggie Burger";
            veggieBurg.description = "A vegan burger";
            veggieBurg.preparationTime = 2;
            veggieBurg.price = 5.99;
            veggieBurg.ingredients = "Buns, Veggie, Lettuce, Tomato, Pickles, Ketchup";

            cheeseBurger.itemName = "Cheese Burger";
            cheeseBurger.description = "A cheese burger";
            cheeseBurger.preparationTime = 2;
            cheeseBurger.price = 5.99;
            cheeseBurger.ingredients = "Buns, Beef, Lettuce, Tomato, Pickles, Ketchup";

            chickenSandwich.itemName = "Cheese Burger";
            chickenSandwich.description = "A cheese burger";
            chickenSandwich.preparationTime = 2;
            chickenSandwich.price = 5.99;
            chickenSandwich.ingredients = "Buns, Beef, Lettuce, Tomato, Pickles, Ketchup";

            menuDAO.addMenuItem(cheeseBurger);
            menuDAO.addMenuItem(veggieBurg);
            menuDAO.addMenuItem(chickenSandwich);

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
