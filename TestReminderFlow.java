// TestReminderFlow.java
import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.*;

public class TestReminderFlow {
    public static void main(String[] args) throws Exception {
        // 1) configure test client
        String testUsername = "test_reminder_user";
        String testPass = "testpass";
        String testName = "Test Reminder";
        String testPhone = "0000000000";
        String testEmail = System.getenv("EMAIL_USERNAME"); // send to yourself (must be set)
        if (testEmail == null || testEmail.trim().isEmpty()) {
            System.err.println("ERROR: EMAIL_USERNAME env var is not set. Set it before running the test.");
            return;
        }

        // 2) ensure client exists
        try {
            if (!ClientDAO.usernameExists(testUsername)) {
                Client c = new Client(testUsername, testPass, testName, testPhone, testEmail);
                ClientDAO.insertClient(c);
                System.out.println("Inserted test client: " + testUsername);
            } else {
                System.out.println("Test client already exists: " + testUsername);
            }
        } catch (Exception e) {
            System.err.println("Error ensuring test client: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3) prepare booking so nextServiceDate == today + 15 days
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DAY_OF_MONTH, 15);
        java.util.Date targetDate = truncateDate(cal.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetStr = sdf.format(targetDate);

        // We'll use intervalMonths = 0 and lastServiceDate = targetDate
        Booking b = new Booking(testUsername, "Test Vehicle 123", 2018, targetDate, 0);
        b.appointmentDate = null;
        b.cost = 0;
        b.completed = false;

        try {
            BookingDAO.insertBooking(b);
            System.out.println("Inserted test booking id=" + b.id + " with nextServiceDate = " + targetStr);
        } catch (Exception e) {
            System.err.println("Error inserting booking: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 4) Run the reminder sender for 15 days
        System.out.println("Running ReminderSender.sendRemindersDaysBefore(15) ...");
        ReminderSender.Result res = ReminderSender.sendRemindersDaysBefore(15);
        System.out.println("Result: sent=" + res.sent + ", skipped=" + res.skippedNoEmail);
        if (!res.errors.isEmpty()) {
            System.out.println("Errors:");
            for (String err : res.errors) System.out.println("  " + err);
        }

        // 5) Show DB reminder_sent_date for this booking
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id, reminder_sent_date FROM bookings WHERE client_username = ? ORDER BY id DESC LIMIT 1")) {
            ps.setString(1, testUsername);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("DB entry for booking id=" + rs.getInt("id")
                            + " reminder_sent_date=" + rs.getString("reminder_sent_date"));
                } else {
                    System.out.println("No booking found for test user (unexpected).");
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading booking reminder_sent_date: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("TEST COMPLETE. (You may want to remove the test booking/client afterwards.)");
    }

    private static java.util.Date truncateDate(java.util.Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
