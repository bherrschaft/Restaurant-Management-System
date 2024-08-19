import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SalesReport {

    public void generateDailyReport() throws SQLException, IOException {
        String query = "SELECT * FROM Orders WHERE DATE('now') = DATE(order_date)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales_report.txt"))) {
                writer.write("-----------------------------\n");
                writer.write("Daily Sales Report\n");
                writer.write("Date: " + new Date().toString() + "\n");
                writer.write("-----------------------------\n");

                double totalRevenue = 0;
                while (rs.next()) {
                    totalRevenue += rs.getDouble("total_price");
                }

                writer.write("Total Revenue: $" + totalRevenue + "\n");

                // Add more details as needed...

                writer.flush();
            }
        }
    }
}
