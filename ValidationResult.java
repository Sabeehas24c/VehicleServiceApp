// ValidationResult.java
import java.util.Date;

public class ValidationResult {
    // public fields used elsewhere in your code
    public boolean valid;
    public Date lastServiceDate;
    public int vehicleYear;
    public int intervalMonths;
    public String message;

    public ValidationResult(boolean v, String m) {
        this.valid = v;
        this.message = m;
    }

    // Backwards-compatible accessors used by the rest of your code
    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
