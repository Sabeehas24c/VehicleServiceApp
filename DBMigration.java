import java.sql.*;

public class DBMigration {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:data/vehicle_service.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn == null) {
                System.out.println("Could not connect to database.");
                return;
            }

            // Check whether the column already exists
            boolean exists = false;
            try (PreparedStatement ps = conn.prepareStatement("PRAGMA table_info('bookings')");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if ("reminder_sent_date".equalsIgnoreCase(rs.getString("name"))) {
                        exists = true;
                        break;
                    }
                }
            }

            if (exists) {
                System.out.println("Column 'reminder_sent_date' already exists.");
            } else {
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("ALTER TABLE bookings ADD COLUMN reminder_sent_date TEXT;");
                    System.out.println("Column 'reminder_sent_date' added successfully!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while adding column: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
