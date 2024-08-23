package com.restaurant.management.dao;

import com.restaurant.management.database.DatabaseInitializer;
import com.restaurant.management.models.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TableDAOTest {

    private TableDAO tableDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        tableDAO = new TableDAO();
        DatabaseInitializer.initialize();  // Initialize the database
    }

    // Positive Test Case: Adding a Valid Table
    @Test
    public void testAddTable_Success() throws SQLException {
        Table table = new Table();
        table.setSize(4);
        table.setStatus("Available");

        tableDAO.addTable(table);
        assertNotNull(table.getTableId());  // Check that the table ID is assigned
    }

    // Negative Test Case: Adding a Table with Invalid Size
    @Test
    public void testAddTable_InvalidSize() {
        Table table = new Table();
        table.setSize(-1);  // Invalid size
        table.setStatus("Available");

        assertThrows(SQLException.class, () -> tableDAO.addTable(table));
    }

    // Edge Test Case: Adding a Table with Zero Size
    @Test
    public void testAddTable_ZeroSize() {
        Table table = new Table();
        table.setSize(0);  // Edge case: zero size
        table.setStatus("Available");

        assertThrows(SQLException.class, () -> tableDAO.addTable(table));
    }

    // Positive Test Case: Updating Table Status
    @Test
    public void testUpdateTableStatus_Success() throws SQLException {
        // Add a valid table first
        Table table = new Table();
        table.setSize(4);
        table.setStatus("Available");
        tableDAO.addTable(table);

        // Now update the status
        tableDAO.updateTableStatus(table.getTableId(), "Reserved");
        List<Table> tables = tableDAO.getAllTables();
        assertEquals("Reserved", tables.get(0).getStatus());
    }

    // Negative Test Case: Updating Non-Existent Table
    @Test
    public void testUpdateTableStatus_NonExistentTable() {
        assertThrows(SQLException.class, () -> tableDAO.updateTableStatus(-1, "Reserved"));  // Non-existent table ID
    }

    // Edge Test Case: Adding a Table with Maximum Size
    @Test
    public void testAddTable_MaxSize() throws SQLException {
        Table table = new Table();
        table.setSize(Integer.MAX_VALUE);  // Edge case: maximum size
        table.setStatus("Available");

        tableDAO.addTable(table);
        assertNotNull(table.getTableId());  // Check that the table ID is assigned
    }

}