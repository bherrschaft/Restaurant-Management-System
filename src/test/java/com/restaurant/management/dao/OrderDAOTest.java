package com.restaurant.management.dao;

import com.restaurant.management.database.DatabaseInitializer;
import com.restaurant.management.models.Order;
import com.restaurant.management.models.OrderItem;
import com.restaurant.management.models.Table;
import com.restaurant.management.models.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOTest {
    private TableDAO tableDAO;
    private OrderDAO orderDAO;
    private MenuDAO menuDAO;  // Add this

    @BeforeEach
    public void setUp() throws SQLException {
        tableDAO = new TableDAO();
        orderDAO = new OrderDAO();
        menuDAO = new MenuDAO();  // Add this

        DatabaseInitializer.initialize();  // Ensure the database is initialized

        // Add a valid table to the database
        Table table = new Table();
        table.setSize(4);
        table.setStatus("Available");
        tableDAO.addTable(table);

        // Add a valid menu item to the database
        MenuItem menuItem = new MenuItem();
        menuItem.setItemName("Cheeseburger");
        menuItem.setPrice(5.99);
        menuItem.setDescription("A delicious cheeseburger.");
        menuItem.setPreparationTime(10);
        menuDAO.addMenuItem(menuItem);  // Ensure this method exists and works correctly
    }

    // Positive Test Case for adding a valid order
    @Test
    public void testAddOrder_Success() throws SQLException {
        // Retrieve the first available table for the order
        List<Table> tables = tableDAO.getAllTables();
        Table table = tables.get(0);  // Assuming we have added at least one table

        Order order = new Order();
        order.setTableId(table.getTableId());  // Set valid table ID
        order.setTotalPrice(59.99);
        order.setStatus("Completed");
        order.setDate(new Date());

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId(1);  // Assuming item ID 1 corresponds to the added menu item
        item.setQuantity(2);
        items.add(item);
        order.setItems(items);

        orderDAO.addOrder(order);

        assertNotNull(order.getOrderId());
    }

    // Negative Test Case for adding an order with an invalid table ID
    @Test
    public void testAddOrder_InvalidTable() {
        Order order = new Order();
        order.setTableId(-1);  // Invalid table ID
        order.setTotalPrice(59.99);
        order.setStatus("Completed");
        order.setDate(new Date());

        assertThrows(SQLException.class, () -> orderDAO.addOrder(order));
    }

    // Edge Test Case for adding an order with no items
    @Test
    public void testAddOrder_InvalidQuantity() {
        List<Table> tables = null;
        try {
            tables = tableDAO.getAllTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Table table = tables.get(0);

        Order order = new Order();
        order.setTableId(table.getTableId());
        order.setTotalPrice(19.99);
        order.setStatus("Pending");
        order.setDate(new Date());

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId(1);  // Assuming item ID 1 exists
        item.setQuantity(0);  // Invalid quantity
        items.add(item);
        order.setItems(items);

        assertThrows(SQLException.class, () -> orderDAO.addOrder(order));
    }

    // Test for getting all orders
    @Test
    public void testGetAllOrders() throws SQLException {
        List<Order> orders = orderDAO.getAllOrders();
        assertNotNull(orders);
    }

    // Test for updating order status
    @Test
    public void testUpdateOrderStatus_Success() throws SQLException {
        List<Table> tables = tableDAO.getAllTables();
        Table table = tables.get(0);

        Order order = new Order();
        order.setTableId(table.getTableId());
        order.setTotalPrice(59.99);
        order.setStatus("Waiting");
        order.setDate(new Date());

        List<OrderItem> items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setItemId(1);
        item.setQuantity(2);
        items.add(item);
        order.setItems(items);

        orderDAO.addOrder(order);

        orderDAO.updateOrderStatus(order.getOrderId(), "Completed");
        assertEquals("Completed", orderDAO.getAllOrders().get(0).getStatus());
    }
}