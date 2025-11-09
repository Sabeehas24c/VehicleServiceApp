import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {
    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:data/vehicle_service.db";

        System.out.println("Checking SQLite connection...");
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            if (conn != null) {
                System.out.println("Connection successful!");
                System.out.println("Database file: " + dbUrl);
                System.out.println("AutoCommit: " + conn.getAutoCommit());
            } else {
                System.out.println("Connection failed (connection is null).");
            }
        } catch (SQLException e) {
            System.out.println("Database connection error:");
            e.printStackTrace();
        }
    }
}
