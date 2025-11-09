import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * ReminderSender — Sends email reminders X days before the next service date.
 * Call with daysBefore = 15 for your requirement.
 */
public class ReminderSender {

    public static class Result {
        public int sent = 0;
        public int skippedNoEmail = 0;
        public List<String> errors = new ArrayList<>();
    }

    public static Result sendRemindersDaysBefore(int daysBefore) {
        Result res = new Result();

        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;
        String username = System.getenv("EMAIL_USERNAME");
        String appPassword = System.getenv("EMAIL_APP_PASSWORD");

        if (username == null || appPassword == null) {
            res.errors.add("EMAIL_USERNAME or EMAIL_APP_PASSWORD not set in environment variables.");
            return res;
        }

        try {
            List<Booking> allBookings = BookingDAO.findAllBookings();
            java.util.Date today = truncateDate(new java.util.Date());

            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, daysBefore);
            java.util.Date targetDate = truncateDate(cal.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Booking booking : allBookings) {
                java.util.Date nextServiceDate = truncateDate(booking.nextServiceDate());

                // Only process bookings due exactly 'daysBefore' days from today
                if (!nextServiceDate.equals(targetDate)) continue;

                // Skip if already reminded
                if (hasReminderSent(booking.id)) continue;

                // Find client email — requires ClientDAO.findByUsername to exist
                Client client = ClientDAO.findByUsername(booking.clientUsername);
                if (client == null || client.email == null || client.email.trim().isEmpty()) {
                    res.skippedNoEmail++;
                    continue;
                }

                String to = client.email.trim();
                String subject = "Vehicle service due in " + daysBefore + " days";
                String body = "Hello " + (client.name == null ? client.username : client.name) + ",\n\n" +
                        "This is a reminder that your vehicle (" + booking.vehicleModel + ") " +
                        "is due for service on " + sdf.format(nextServiceDate) + ".\n\n" +
                        "Please log in to schedule or contact us for details.\n\n" +
                        "Best regards,\nVehicle Service Team";

                try {
                    EmailUtil.sendEmail(smtpHost, smtpPort, username, appPassword,
                            username, to, subject, body);

                    // BookingDAO.markReminderSent must exist
                    BookingDAO.markReminderSent(booking.id, new java.util.Date());
                    res.sent++;
                } catch (Exception e) {
                    res.errors.add("Booking " + booking.id + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            res.errors.add("Unexpected error: " + e.getMessage());
        }

        return res;
    }

    private static boolean hasReminderSent(int bookingId) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT reminder_sent_date FROM bookings WHERE id = ?")) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String sent = rs.getString("reminder_sent_date");
                    return sent != null && !sent.trim().isEmpty();
                }
            }
        } catch (Exception e) {
            System.err.println("Reminder check error: " + e.getMessage());
        }
        return false;
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
