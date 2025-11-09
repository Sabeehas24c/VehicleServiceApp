import java.util.Calendar;
import java.util.Date;

public class Booking {
    private static int COUNTER = 1; //Encapsulation - private

    public int id;
    public String clientUsername;
    public String vehicleModel;
    public int year;
    public Date lastServiceDate;
    public int serviceIntervalMonths;
    public boolean completed;
    public Date bookedOn;
    public int cost;
    public Date appointmentDate;

    public Booking(String clientUsername, String vehicleModel, int year, Date lastServiceDate, int serviceIntervalMonths) {
        this.id = COUNTER++;
        this.clientUsername = clientUsername;
        this.vehicleModel = vehicleModel;
        this.year = year;
        this.lastServiceDate = lastServiceDate;
        this.serviceIntervalMonths = serviceIntervalMonths;
        this.completed = false;
        this.bookedOn = new Date();
        this.cost = 0;
        this.appointmentDate = null;
    }

    public Date nextServiceDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(lastServiceDate);
        c.add(Calendar.MONTH, serviceIntervalMonths);
        return c.getTime();
    }

    public boolean isOverdue() {
        if (completed) return false;
        return nextServiceDate().before(new Date());
    }
}
