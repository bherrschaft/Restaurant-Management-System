import com.restaurant.management.authentication.Authentication;
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

    public static void main(String[] args) throws SQLException, NoSuchAlgorithmException {

        try {
            DatabaseInitializer.initialize();
            Authentication auth = new Authentication();
            auth.initUsers();
            MenuItem initMenu = new MenuItem();
            initMenu.initMenuItems();
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
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (auth.login(username, password)) {
                System.out.println("Login successful!");
                showMainMenu();
            } else {
                System.out.println("Login failed! Invalid username or password.");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println("Error logging in: " + e.getMessage());
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
                    // TODO create option for sales report
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
        System.out.println("0. Go Back\n");
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
            List<Order> orders = orderDAO.getAllOrders();
            if (orders == null || orders.isEmpty()) {
                System.out.println("No orders found.");
                return;
            }

            System.out.println("\n=== Orders ===");
            for (Order order : orders) {
                System.out.printf("Order ID: %d, Table ID: %d, Total: $%.2f, Status: %s%n",
                        order.getOrderId(), order.getTableId(), order.getTotalPrice(), order.getStatus());
                System.out.println("Items:");
                List<OrderItem> items = order.getItems();
                if (items != null && !items.isEmpty()) {
                    for (OrderItem item : items) {
                        System.out.printf("  - Item ID: %d, Quantity: %d%n",
                                item.getItemId(), item.getQuantity());
                    }
                } else {
                    System.out.println("  No items found for this order.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving orders: " + e.getMessage());
        }
    }


    private static void createOrder() {
        try {
            System.out.print("Enter Table ID: ");
            int tableId = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            List<OrderItem> items = new ArrayList<>();
            while (true) {
                System.out.print("Enter Item ID (or 0 to finish): ");
                int itemId = scanner.nextInt();
                if (itemId == 0) break;
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                OrderItem orderItem = new OrderItem();
                orderItem.setItemId(itemId);
                orderItem.setQuantity(quantity);

                items.add(orderItem);
            }

            // Prompt for order status
            System.out.print("Enter Order Status (e.g., Waiting, In Progress, Completed): ");
            String status = scanner.nextLine();

            Order order = new Order();
            order.setTableId(tableId);
            order.setItems(items);
            order.setTotalPrice(calculateTotalPrice(items));  // Implement this method to calculate the total
            order.setStatus(status);  // Set the status based on user input

            orderDAO.addOrder(order);
            System.out.println("Order created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }


    private static double calculateTotalPrice(List<OrderItem> items) {
        // Implement logic to calculate the total price of the order based on items
        // You can fetch item prices from the database using MenuDAO or similar
        return 0.0;
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
            System.out.print("Enter table size (number of seats): ");
            int size = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter table status (Available/Reserved/Occupied): ");
            String status = scanner.nextLine();

            Table table = new Table();
            table.setSize(size);
            table.setStatus(status);

            tableDAO.addTable(table);
            System.out.println("Table created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
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










}
