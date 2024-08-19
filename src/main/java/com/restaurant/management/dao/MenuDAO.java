import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM Menu";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MenuItem item = new MenuItem();
                item.setItemId(rs.getInt("item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setPreparationTime(rs.getInt("preparation_time"));
                item.setPrice(rs.getDouble("price"));
                item.setIngredients(rs.getString("ingredients"));
                menuItems.add(item);
            }
        }
        return menuItems;
    }

    public void addMenuItem(MenuItem item) throws SQLException {
        String query = "INSERT INTO Menu (item_name, description, preparation_time, price, ingredients) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getDescription());
            pstmt.setInt(3, item.getPreparationTime());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getIngredients());
            pstmt.executeUpdate();
        }
    }

    public void updateMenuItem(MenuItem item) throws SQLException {
        String query = "UPDATE Menu SET item_name = ?, description = ?, preparation_time = ?, price = ?, ingredients = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getDescription());
            pstmt.setInt(3, item.getPreparationTime());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, item.getIngredients());
            pstmt.setInt(6, item.getItemId());
            pstmt.executeUpdate();
        }
    }

    public void deleteMenuItem(int itemId) throws SQLException {
        String query = "DELETE FROM Menu WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        }
    }
}
