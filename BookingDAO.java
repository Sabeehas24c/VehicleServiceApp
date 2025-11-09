import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDAO {

    public static void insertBooking(Booking b) throws SQLException {
        String sql = "INSERT INTO bookings (client_username, vehicle_model, vehicle_year, last_service_date, service_interval_months, completed, cost, appointment_date) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.clientUsername);
            ps.setString(2, b.vehicleModel);
            ps.setInt(3, b.year);
            ps.setString(4, AppConfig.sdf.format(b.lastServiceDate)); // yyyy-MM-dd
            ps.setInt(5, b.serviceIntervalMonths);
            ps.setInt(6, b.completed ? 1 : 0);
            ps.setInt(7, b.cost);
            if (b.appointmentDate != null) ps.setString(8, AppConfig.sdf.format(b.appointmentDate));
            else ps.setNull(8, Types.VARCHAR);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) b.id = rs.getInt(1);
            }
        }
    }

    public static List<Booking> findAllBookings() throws SQLException {
        String sql = "SELECT * FROM bookings ORDER BY id";
        List<Booking> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(mapRowToBooking(rs));
            }
        } catch (ParseException pe) {
            throw new SQLException("Date parse error", pe);
        }
        return out;
    }

    public static Booking findBookingById(int id) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToBooking(rs);
            }
        } catch (ParseException pe) {
            throw new SQLException("Date parse error", pe);
        }
        return null;
    }

    public static List<Booking> findBookingsForClient(String username) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE client_username = ? ORDER BY id";
        List<Booking> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRowToBooking(rs));
            }
        } catch (ParseException pe) {
            throw new SQLException("Date parse error", pe);
        }
        return out;
    }

    public static List<Booking> findOverdueBookings() throws SQLException {
        List<Booking> out = new ArrayList<>();
        List<Booking> all = findAllBookings();
        Date now = new Date();
        for (Booking b : all) {
            if (!b.completed && b.nextServiceDate().before(now)) out.add(b);
        }
        return out;
    }

    public static void updateAppointmentDate(int bookingId, java.util.Date appointmentDate) throws SQLException {
        String sql = "UPDATE bookings SET appointment_date = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (appointmentDate != null) ps.setString(1, AppConfig.sdf.format(appointmentDate));
            else ps.setNull(1, Types.VARCHAR);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    public static void markCompleted(int bookingId, int cost) throws SQLException {
        String sql = "UPDATE bookings SET completed = 1, cost = ?, last_service_date = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cost);
            ps.setString(2, AppConfig.sdf.format(new java.util.Date())); // set last_service_date to today
            ps.setInt(3, bookingId);
            ps.executeUpdate();
        }
    }

    public static void deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    // NEW: mark that a reminder was sent (stores date string in reminder_sent_date)
    public static void markReminderSent(int bookingId, java.util.Date sentDate) throws SQLException {
        String sql = "UPDATE bookings SET reminder_sent_date = ? WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, AppConfig.sdf.format(sentDate));
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    private static Booking mapRowToBooking(ResultSet rs) throws SQLException, ParseException {
        String clientUsername = rs.getString("client_username");
        String vehicleModel = rs.getString("vehicle_model");
        int vehicleYear = rs.getInt("vehicle_year");
        String lastServiceStr = rs.getString("last_service_date");
        Date lastService = AppConfig.sdf.parse(lastServiceStr);
        int interval = rs.getInt("service_interval_months");

        Booking b = new Booking(clientUsername, vehicleModel, vehicleYear, lastService, interval);
        b.id = rs.getInt("id");
        b.completed = rs.getInt("completed") != 0;
        String bookedOn = rs.getString("booked_on");
        if (bookedOn != null) {
            try { b.bookedOn = AppConfig.sdf.parse(bookedOn); } catch (Exception ex) { /* ignore */ }
        }
        b.cost = rs.getInt("cost");
        String appt = rs.getString("appointment_date");
        if (appt != null) b.appointmentDate = AppConfig.sdf.parse(appt);
        // reminder_sent_date is stored but Booking class may not have field; if it does, set it
        try {
            String rem = rs.getString("reminder_sent_date");
            if (rem != null) {
                // if Booking has a reminderSentDate field as String, set it via reflection or adjust Booking class accordingly
                // we ignore here if Booking doesn't expose it
            }
        } catch (Exception ignore) {}
        return b;
    }
}
