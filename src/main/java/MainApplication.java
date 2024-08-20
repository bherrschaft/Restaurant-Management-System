import com.restaurant.management.authentication.Authentication;
import com.restaurant.management.authentication.UserDAO;
import com.restaurant.management.dao.InventoryDAO;
import com.restaurant.management.dao.OrderDAO;
import com.restaurant.management.models.InventoryItem;
import com.restaurant.management.models.Order;
import com.restaurant.management.models.OrderItem;
import com.restaurant.management.database.DatabaseInitializer;
import com.restaurant.management.dao.MenuDAO;
import com.restaurant.management.dao.TableDAO;
import com.restaurant.management.models.MenuItem;
import com.restaurant.management.models.Table;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Authentication auth = new Authentication();
    private static final InventoryDAO inventoryDAO = new InventoryDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final MenuDAO menuDAO = new MenuDAO();
    private static final TableDAO tableDAO = new TableDAO();
    private static String username;
    private static String password;

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {

        try {
            DatabaseInitializer.initialize();
            Authentication auth = new Authentication();
            auth.initUsers();
            showInitialMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showInitialMenu() {
        while (true) {
            System.out.println("\n=== Kyle's Burger Shack ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    System.out.println("Exiting the system.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loginUser() {
        try {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            password = scanner.nextLine();

            if (auth.login(username, password)) {
                System.out.println("Login successful!");
                showMainMenu();  // Call the main menu after successful login
            } else {
                System.out.println("Login failed! Invalid username or password.");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    // Main Menu method
    private static void showMainMenu() {
        while (true) {
            System.out.println("\nLogin Successful!");
            System.out.println("Welcome "+ username);
            System.out.println("\nHi "+username+ ", please select from the items below:");
            System.out.println("1. Table Options");
            System.out.println("2. Order Options");
            System.out.println("3. Menu Options");
            System.out.println("4. Inventory Option");
            System.out.println("5. Sales Report");
            System.out.println("6. Log Off");
            System.out.println("7. End Session");
            System.out.print("\nEnter your choice: ");

            // Capture user input
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    tableOptionsMenu();
                    break;
                case 2:
                    orderManagementMenu();
                    break;
                case 3:
                    menuOptionsMenu();
                    break;
                case 4:
                    inventoryManagementMenu();
                    break;
                case 5:
                    salesReportMenu();
                    break;
                case 6:
                    System.out.println("Logging off...");
                    return;  // Exit the loop and logout
                case 7:
                    System.out.println("Ending session...");
                    System.exit(0);  // End the application
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void orderManagementMenu() {
    }

    // Placeholder methods for the options
    private static void tableOptionsMenu() {
        System.out.println("Table options logic here.");
        // Implementation goes here...
    }

    private static void menuOptionsMenu() {
        System.out.println("Menu options logic here.");
        // Implementation goes here...
    }

    private static void salesReportMenu() {
        System.out.println("Sales report logic here.");
        // Implementation goes here...
    }

    // Example: Inventory Management Menu
    private static void inventoryManagementMenu() {
        System.out.println("\n=== Inventory Management ===");
        System.out.println("1. View All Inventory Items");
        System.out.println("2. Update Inventory Item");
        System.out.println("3. Check Low Stock Items");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                viewAllInventoryItems();
                break;
            case 2:
                updateInventoryItem();
                break;
            case 3:
                checkLowStockItems();
                break;
            case 4:
                return;  // Go back to main menu
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewAllInventoryItems() {
        try {
            List<InventoryItem> items = inventoryDAO.getAllInventoryItems();
            System.out.println("\n=== Inventory Items ===");
            for (InventoryItem item : items) {
                System.out.printf("ID: %d, Name: %s, Quantity: %d, Low Stock Threshold: %d%n",
                        item.getIngredientId(), item.getIngredientName(), item.getQuantityInStock(), item.getLowStockAlertThreshold());
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving inventory items: " + e.getMessage());
        }
    }

    private static void updateInventoryItem() {
        try {
            System.out.print("Enter Ingredient ID to update: ");
            int ingredientId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            System.out.print("Enter new quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            InventoryItem item = new InventoryItem();
            item.setIngredientId(ingredientId);
            item.setQuantityInStock(quantity);

            inventoryDAO.updateInventoryItem(item);
            System.out.println("Inventory item updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating inventory item: " + e.getMessage());
        }
    }

    private static void checkLowStockItems() {
        try {
            System.out.println("\n=== Low Stock Items ===");
            inventoryDAO.checkLowStock();
        } catch (SQLException e) {
            System.out.println("Error checking low stock items: " + e.getMessage());
        }
    }

    // Similar structure should be followed for other methods like orderManagementMenu, createMenuItem, etc.
}
