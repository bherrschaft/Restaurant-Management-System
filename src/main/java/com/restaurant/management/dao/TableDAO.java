import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {

    public List<Table> getAllTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        String query = "SELECT * FROM Tables";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Table table = new Table();
                table.setTableId(rs.getInt("table_id"));
                table.setSize(rs.getInt("size"));
                table.setStatus(rs.getString("status"));
                tables.add(table);
            }
        }
        return tables;
    }

    public void updateTableStatus(int tableId, String status) throws SQLException {
        String query = "UPDATE Tables SET status = ? WHERE table_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, tableId);
            pstmt.executeUpdate();
        }
    }
}
