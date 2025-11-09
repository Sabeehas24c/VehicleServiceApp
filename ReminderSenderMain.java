// ReminderSenderMain.java
public class ReminderSenderMain {
    public static void main(String[] args) {
        System.out.println("ReminderSenderMain started: " + new java.util.Date());
        ReminderSender.Result r = ReminderSender.sendRemindersDaysBefore(15);
        System.out.println("Sent: " + r.sent + "  Skipped: " + r.skippedNoEmail);
        if (!r.errors.isEmpty()) {
            System.err.println("Errors:");
            for (String e : r.errors) System.err.println("  " + e);
        }
    }
}
