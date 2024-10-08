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
import com.restaurant.management.reports.SalesReport;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.text.SimpleDateFormat;

public class MainApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Authentication auth = new Authentication();
    private static final InventoryDAO inventoryDAO = new InventoryDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final MenuDAO menuDAO = new MenuDAO();
    private static final TableDAO tableDAO = new TableDAO();
    private static final SalesReport salesReport = new SalesReport(); // initialized SalesReport

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {

        try {
            DatabaseInitializer.initialize();
            Authentication auth = new Authentication();
            auth.initUsers();
            InventoryDAO initInv = new InventoryDAO();
            initInv.initInventoryItems();
            MenuItem initMenu = new MenuItem();
            initMenu.initMenuItems();
            showInitialMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showInitialMenu() {
        while (true) {
            try {
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
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();  // Consume invalid input
            }
        }
    }



    private static void loginUser() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (auth.login(username, password)) {

                String role = UserDAO.getUserRole(username);
                if ("manager".equalsIgnoreCase(role)) {
                    System.out.println("Logged in as:" + "(" + role.toUpperCase() + ")");
                    showMainMenu();
                }else {
                    System.out.println("Logged in as:" + "(" + role.toUpperCase() + ")");
                    showStaffMenu();
                }
            } else {
                System.out.println("Login failed! Invalid username or password.");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }
    private static void showStaffMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Table Options");
            System.out.println("2. Order Options");
            System.out.println("3. Menu Options");
            System.out.println("4. Inventory Options");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    userManagementMenu();
                    break;
                case 2:
                    orderManagementMenu();
                    break;
                case 3:
                    menuManagement();
                    break;
                case 4:
                    inventoryManagementMenu();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return; // Exit the loop and logout
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Table Options");
            System.out.println("2. Order Options");
            System.out.println("3. Menu Options");
            System.out.println("4. Inventory Options");
            System.out.println("5. Sales Report");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    userManagementMenu();
                    break;
                case 2:
                    orderManagementMenu();
                    break;
                case 3:
                    menuManagement();
                    break;
                case 4:
                    inventoryManagementMenu();
                    break;
                case 5:
                    generateDailyReport();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return; // Exit the loop and logout
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void userManagementMenu() {
        System.out.println("\n=== Table Options ===");
        System.out.println("1. Assign a Table");
        System.out.println("2. Update a table");
        System.out.println("3. Print table list");
        System.out.println("4. Back to Main Menu");
        System.out.println("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                createTable();
                break;
            case 2:
                updateTableStatus();
                break;
            case 3:
                viewAllTables();
                break;


            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

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
                return;
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

    private static void orderManagementMenu() {
        System.out.println("\n=== Order Management ===");
        System.out.println("1. View Orders");
        System.out.println("2. Create Order");
        System.out.println("3. Update Order Status");
        System.out.println("4. Delete Order");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                viewOrders();
                break;
            case 2:
                createOrder();
                break;
            case 3:
                updateOrderStatus();
                break;
            case 4:
                deleteOrder();
                break;
            case 5:
                return;  // Go back to the main menu
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewOrders() {
        try {
            System.out.println("Retrieving orders...");
            List<Order> orders = orderDAO.getAllOrders();
            System.out.println("\n=== Orders ===");

            if (orders == null || orders.isEmpty()) {
                System.out.println("No orders found.");
                return;
            }

            for (Order order : orders) {
                System.out.printf("\nOrder ID: %d\nTable ID: %d\nTotal: $%.2f\nStatus: %s%n",
                        order.getOrderId(), order.getTableId(), order.getTotalPrice(), order.getStatus());

                List<OrderItem> items = order.getItems();
                if (items != null && !items.isEmpty()) {
                    System.out.println("Items:");
                    for (OrderItem item : items) {
                        System.out.printf("  - Item ID: %d, Quantity: %d%n", item.getItemId(), item.getQuantity());
                    }
                }// else {
//                    System.out.println("  No items found for this order.");
//                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving orders: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private static void createOrder() {
        try {
            System.out.print("Enter Table ID: ");
            int tableId = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.println();
            List<OrderItem> items = new ArrayList<>();
            while (true) {

                System.out.println("=== Menu Items ===");
                System.out.println("1. Cheese Burger    ($5.99)");
                System.out.println("2. Veggie Burger    ($5.99)");
                System.out.println("3. Chicken Sandwich ($5.99)");
                System.out.println("0. To Total Order");
                System.out.print("Enter Item Number: ");
                int itemId = scanner.nextInt();
                scanner.nextLine();
                if (itemId == 0) break;

                /*
                added conditional statement below so that staff cannot select quantity of 0, when placing an order. bb
                 */

                int quantity;
                while (true) {
                    System.out.print("Enter Quantity: ");
                    quantity = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    if (quantity > 0) {
                        break;
                    } else {
                        System.out.println("Quantity must be greater than 0. Please enter a valid quantity");
                    }
                }



                OrderItem orderItem = new OrderItem();  // Create a new OrderItem instance
                orderItem.setItemId(itemId);            // Set the item ID
                orderItem.setQuantity(quantity);        // Set the quantity

                items.add(orderItem);                   // Add the item to the list
            }


            double totalPrice = calculateTotalPrice(items);
            System.out.printf("Total Price: $%.2f%n", totalPrice);


                // Prompt for order status
            System.out.print("Enter Order Status (Waiting, Preparing, Ready): ");
            String status = scanner.nextLine();

            Order order = new Order();
            order.setTableId(tableId);
            order.setItems(items);
            order.setTotalPrice(calculateTotalPrice(items));  // Implement this method to calculate the total
            order.setStatus(status);  // Set the status based on user input
            order.setDate(new Date()); // Set the date //updated line 325 added setDate

            orderDAO.addOrder(order);
            System.out.println("Order created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }


    private static double calculateTotalPrice(List<OrderItem> items) {
        double totalPrice = 0.0;
        try {
            MenuDAO menuDAO = new MenuDAO();  // Instantiate the MenuDAO to fetch item prices
            for (OrderItem item : items) {
                double price = menuDAO.getItemPrice(item.getItemId());  // Assuming this method exists in MenuDAO
                totalPrice += price * item.getQuantity();
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total price: " + e.getMessage());
        }
        return totalPrice;
    }

    private static void updateOrderStatus() {
        try {
            System.out.print("Enter Order ID: ");
            int orderId = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter new status (Waiting, Preparing, Ready): ");
            String status = scanner.nextLine();

            orderDAO.updateOrderStatus(orderId, status);
            System.out.println("Order status updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
        }
    }

    private static void deleteOrder() {
        try {
            System.out.print("Enter Order ID to delete: ");
            int orderId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            orderDAO.deleteOrder(orderId);
            System.out.println("Order deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting order: " + e.getMessage());
        }
    }

    private static void createMenuItem() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine();

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter preparation time (in minutes): ");
            int preparationTime = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter price: ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter the new ingredients: ");
            String ingredients = scanner.nextLine();

            MenuItem menuItem = new MenuItem(itemName,description,preparationTime,price,ingredients);



            menuDAO.addMenuItem(menuItem);
            System.out.println("Menu item created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating menu item: " + e.getMessage());
        }
    }


    private static void createTable() {
        try {
            System.out.print("Enter table size (1-6 of customers): ");
            int size = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter table status (Available/Reserved/Occupied): ");
            String status = scanner.nextLine();

            Table table = new Table();
            table.setSize(size);
            table.setStatus(status);
            tableDAO.addTable(table);
            System.out.println("Table created successfully with ID: " + table.getTableId());
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /*
    private static void updateTableStatus() {
        try {
            // Prompt the user for table ID
            System.out.print("Enter Table ID to update: ");
            int tableId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            // Prompt the user for new status
            System.out.print("Enter new status for the table (e.g., 'Available', 'Occupied', 'Reserved'): ");
            String status = scanner.nextLine();

            // Update the table status using TableDAO
            tableDAO.updateTableStatus(tableId, status);
            System.out.println("Table status updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating table status: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }
    */

    private static void updateTableStatus() {
        try {
            // Prompt the user for table ID
            System.out.print("Enter Table ID to update: ");
            int tableId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            // Prompt the user for new status
            System.out.print("Enter new status for the table (e.g., 'Available', 'Occupied', 'Reserved'): ");
            String status = scanner.nextLine();

            // Debugging: Print the values being passed
            System.out.println("Updating table with ID: " + tableId + " to status: " + status);

            // Update the table status using TableDAO and get rows affected
            int rowsAffected = tableDAO.updateTableStatus(tableId, status);

            // Check if any row was updated
            if (rowsAffected > 0) {
                System.out.println("Table status updated successfully!");

                // Fetch the updated table and print its status
                Table updatedTable = tableDAO.getAllTables().stream()
                        .filter(table -> table.getTableId() == tableId)
                        .findFirst()
                        .orElse(null);

                if (updatedTable != null) {
                    System.out.println("Updated table status: " + updatedTable.getStatus());
                }

            } else {
                System.out.println("No rows were updated. Please check the table ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating table status: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }



    private static void viewAllTables() {
        try {
            // Fetch all tables from the TableDAO
            List<Table> tables = tableDAO.getAllTables();

            // Display the table information to the user
            System.out.println("\n=== All Tables ===");
            for (Table table : tables) {
                System.out.printf("Table ID: %d, Size: %d, Status: %s%n",
                        table.getTableId(), table.getSize(), table.getStatus());
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving tables: " + e.getMessage());
        }
    }










//Daylen 433 - 533


    // creating menu management menu
// createMenuItem();
    public static void updateMenuItemPrompt() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for the item ID
        System.out.print("Enter the ID of the menu item to update: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Prompt for the new item name
        System.out.print("Enter the new item name: ");
        String itemName = scanner.nextLine();

        // Prompt for the new description
        System.out.print("Enter the new description: ");
        String description = scanner.nextLine();

        // Prompt for the new preparation time
        System.out.print("Enter the new preparation time (in minutes): ");
        int preparationTime = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Prompt for the new price
        System.out.print("Enter the new price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        // Prompt for the new ingredients
        System.out.print("Enter the new ingredients: ");
        String ingredients = scanner.nextLine();

        // Create the MenuItem object with the new values
        MenuItem item = new MenuItem(itemId, itemName, description, preparationTime, price, ingredients);

        // Update the menu item in the database
        try {
            MenuDAO.updateMenuItem(item);
            System.out.println("Menu item updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating menu item: " + e.getMessage());
        }
    }

    // deletion method per ID
    public static void deleteMenuItem() {
        System.out.print("Enter ID of the menu item to delete: ");
        int itemId = scanner.nextInt();
        scanner.nextLine();


        try {
            MenuDAO.deleteMenuItem(itemId);
            System.out.println("Menu item deleted successfully!");
        }catch(SQLException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
        }
    }



    public static void menuManagement() {
        System.out.println("\n=== Menu Management ===");
        System.out.println("1. Add Menu Item");
        System.out.println("2. Update Menu Item");
        System.out.println("3. Delete Menu Item");
        System.out.println("4. Back to Main Menu");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                createMenuItem();
                break;
            case 2:
                updateMenuItemPrompt();
                break;
            case 3:
                deleteMenuItem();

        }

    }






























































































//Lexi 534 - 634




































































































//brittany 635 - 735

    //added method to generate Sales Report
    private static void generateDailyReport() {
        try {
            salesReport.generateDailyReport();  // Call the method to generate the report
        } catch (SQLException | IOException e) {
            System.out.println("Error generating sales report: " + e.getMessage());
        }
    }




}
