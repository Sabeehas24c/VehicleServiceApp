// Validators.java
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
//import java.util.regex.*;

public class Validators {

    // ---- Booking / date / year validation (used by AddService & BookService) ----
    public static ValidationResult validateVehicleYearAndLastService(String yearStr, String lastServiceStr, String intervalStr) {
        yearStr = (yearStr == null) ? "" : yearStr.trim();
        lastServiceStr = (lastServiceStr == null) ? "" : lastServiceStr.trim();
        intervalStr = (intervalStr == null) ? "" : intervalStr.trim();

        if (yearStr.isEmpty() || lastServiceStr.isEmpty() || intervalStr.isEmpty()) {
            return new ValidationResult(false, "Fill vehicle year, last service date and interval.");
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Vehicle year must be a valid integer (e.g., 2015).");
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year > currentYear) {
            return new ValidationResult(false, "Vehicle year cannot be in the future.");
        }

        Date lastService;
        try {
            lastService = AppConfig.sdf.parse(lastServiceStr);
        } catch (ParseException pe) {
            return new ValidationResult(false, "Last service date invalid. Use format yyyy-MM-dd.");
        }

        Date today = new Date();
        if (lastService.after(today)) {
            return new ValidationResult(false, "Last service date cannot be in the future.");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(lastService);
        int lastServiceYear = cal.get(Calendar.YEAR);
        if (lastServiceYear < year) {
            return new ValidationResult(false, "Invalid: last service year (" + lastServiceYear + ") is earlier than vehicle year (" + year + ").");
        }

        int im;
        try {
            im = Integer.parseInt(intervalStr);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Interval must be an integer (months).");
        }
        if (im <= 0) {
            return new ValidationResult(false, "Interval must be a positive integer (months).");
        }

        ValidationResult ok = new ValidationResult(true, "OK");
        ok.lastServiceDate = lastService;
        ok.vehicleYear = year;
        ok.intervalMonths = im;
        return ok;
    }

    // ---- Booking constraints checks (used to prevent double-booking etc.) ----
    public static ValidationResult checkBookingConstraints(String vehicleModelRaw, Date requestedLastServiceDate) {
        if (vehicleModelRaw == null) vehicleModelRaw = "";
        String vehicleModel = vehicleModelRaw.trim();
        if (vehicleModel.isEmpty()) return new ValidationResult(false, "Vehicle model cannot be empty.");

        // Check in-memory store first (DataStore.bookings) and then DB-stored bookings via BookingDAO
        for (Booking b : DataStore.bookings) {
            if (b.vehicleModel == null) continue;
            String existingModel = b.vehicleModel.trim();
            if (!existingModel.equalsIgnoreCase(vehicleModel)) continue;

            if (!b.completed) {
                return new ValidationResult(false, "Please mark existing booking as completed before creating a new one for this vehicle (" + vehicleModel + ").");
            }

            if (b.lastServiceDate != null) {
                if (AppConfig.sdf.format(b.lastServiceDate).equals(AppConfig.sdf.format(requestedLastServiceDate))) {
                    return new ValidationResult(false, "A booking already exists for this vehicle on the same service date (" + AppConfig.sdf.format(requestedLastServiceDate) + ").");
                }

                long diffMs = Math.abs(requestedLastServiceDate.getTime() - b.lastServiceDate.getTime());
                long days = diffMs / (1000L * 60L * 60L * 24L);
                if (days < 180L) {
                    return new ValidationResult(false, "Cannot create a new booking: the vehicle was serviced recently (" + AppConfig.sdf.format(b.lastServiceDate) + "). Please wait at least 6 months (~180 days) from last service.");
                }
            }
        }

        // Also check bookings from DB (safer)
        try {
            for (Booking b : BookingDAO.findAllBookings()) {
                if (b.vehicleModel == null) continue;
                String existingModel = b.vehicleModel.trim();
                if (!existingModel.equalsIgnoreCase(vehicleModel)) continue;

                if (!b.completed) {
                    return new ValidationResult(false, "Please mark existing booking as completed before creating a new one for this vehicle (" + vehicleModel + ").");
                }

                if (b.lastServiceDate != null) {
                    if (AppConfig.sdf.format(b.lastServiceDate).equals(AppConfig.sdf.format(requestedLastServiceDate))) {
                        return new ValidationResult(false, "A booking already exists for this vehicle on the same service date (" + AppConfig.sdf.format(requestedLastServiceDate) + ").");
                    }

                    long diffMs = Math.abs(requestedLastServiceDate.getTime() - b.lastServiceDate.getTime());
                    long days = diffMs / (1000L * 60L * 60L * 24L);
                    if (days < 180L) {
                        return new ValidationResult(false, "Cannot create a new booking: the vehicle was serviced recently (" + AppConfig.sdf.format(b.lastServiceDate) + "). Please wait at least 6 months (~180 days) from last service.");
                    }
                }
            }
        } catch (Exception e) {
            // ignore DB errors here — don't block booking if DB read fails; log to stderr
            System.err.println("Warning: booking constraint DB check failed: " + e.getMessage());
        }

        return new ValidationResult(true, "OK");
    }

    // ---- Simple reusable validators (email, phone, password, name) ----
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email cannot be empty.");
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            return new ValidationResult(false, "Invalid email format.");
        }
        return new ValidationResult(true, "Valid email.");
    }

    public static ValidationResult validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return new ValidationResult(false, "Phone number cannot be empty.");
        }
        String regex = "^[6-9][0-9]{9}$";
        if (!phone.matches(regex)) {
            return new ValidationResult(false, "Invalid phone number. Enter 10 digits starting with 6–9.");
        }
        return new ValidationResult(true, "Valid phone number.");
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty.");
        }
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        if (!password.matches(regex)) {
            return new ValidationResult(false,
                "Password must be at least 6 characters long, contain one uppercase, one lowercase, and one digit.");
        }
        return new ValidationResult(true, "Valid password.");
    }

    public static ValidationResult validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "Name cannot be empty.");
        }
        String regex = "^[A-Za-z ]+$";
        if (!name.matches(regex)) {
            return new ValidationResult(false, "Name can only contain letters and spaces.");
        }
        return new ValidationResult(true, "Valid name.");
    }
}
