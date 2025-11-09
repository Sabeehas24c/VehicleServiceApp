// CleanupTestData.java
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CleanupTestData {
    public static void main(String[] args) {
        String testUsername = "test_reminder_user";

        try (Connection conn = DBUtil.getConnection()) {
            // Delete bookings first (foreign key dependency)
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM bookings WHERE client_username = ?")) {
                ps.setString(1, testUsername);
                int count = ps.executeUpdate();
                System.out.println("Deleted " + count + " booking(s) for user " + testUsername);
            }

            // Delete client record
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM clients WHERE username = ?")) {
                ps.setString(1, testUsername);
                int count = ps.executeUpdate();
                System.out.println("Deleted " + count + " client record(s).");
            }

            System.out.println("✅ Cleanup complete!");
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
