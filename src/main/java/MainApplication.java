import com.restaurant.management.authentication.Authentication;
import com.restaurant.management.authentication.UserDAO;
import com.restaurant.management.dao.*;
import com.restaurant.management.database.DatabaseInitializer;
import com.restaurant.management.reports.SalesReport;

import java.sql.SQLException;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;


public class MainApplication {

    public static void main(String[] args) {
        try {
            // Initialize the database
            DatabaseInitializer.initialize();

            // Create DAOs
            UserDAO userDAO = new UserDAO();
            MenuDAO menuDAO = new MenuDAO();
            OrderDAO orderDAO = new OrderDAO();
            TableDAO tableDAO = new TableDAO();
            InventoryDAO inventoryDAO = new InventoryDAO();
            StaffDAO staffDAO = new StaffDAO();
            ReservationDAO reservationDAO = new ReservationDAO();
            SpecialOfferDAO specialOfferDAO = new SpecialOfferDAO();
            SalesReport salesReport = new SalesReport();

            // Basic interaction
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Restaurant Management System");
            System.out.println("Please login:");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            Authentication auth = new Authentication();
            if (auth.login(username, password)) {
                System.out.println("Login successful!");

                // Sample actions
                System.out.println("1. View Menu");
                System.out.println("2. View Orders");
                System.out.println("3. View Tables");
                // Add more actions as needed...

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        menuDAO.getAllMenuItems().forEach(item -> {
                            System.out.println(item.getItemName() + " - $" + item.getPrice());
                        });
                        break;
                    case 2:
                        orderDAO.getAllOrders().forEach(order -> {
                            System.out.println("com.restaurant.management.models.Order ID: " + order.getOrderId() + " Total: $" + order.getTotalPrice());
                        });
                        break;
                    case 3:
                        tableDAO.getAllTables().forEach(table -> {
                            System.out.println("com.restaurant.management.models.Table ID: " + table.getTableId() + " Status: " + table.getStatus());
                        });
                        break;
                    // Add more cases for other actions...
                }
            } else {
                System.out.println("Login failed!");
            }

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
