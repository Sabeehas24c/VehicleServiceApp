import java.sql.*;

public class DBUtil {
    private static final String DB_FILE = "data/vehicle_service.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    static {
        try {
            Class.forName("org.sqlite.JDBC");

            // ensure data directory exists (relative to project root)
            java.io.File folder = new java.io.File("data");
            if (!folder.exists()) folder.mkdir();

            // Create tables if not present
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");

                st.execute(
                    "CREATE TABLE IF NOT EXISTS clients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "name TEXT, phone TEXT, email TEXT" +
                    ");"
                );

                st.execute(
                    "CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "client_username TEXT NOT NULL, " +
                    "vehicle_model TEXT NOT NULL, " +
                    "vehicle_year INTEGER NOT NULL, " +
                    "last_service_date TEXT NOT NULL, " +    // yyyy-MM-dd
                    "service_interval_months INTEGER NOT NULL, " +
                    "completed INTEGER DEFAULT 0, " +
                    "booked_on TEXT DEFAULT (date('now')), " +
                    "cost INTEGER DEFAULT 0, " +
                    "appointment_date TEXT, " +
                    "FOREIGN KEY(client_username) REFERENCES clients(username) ON DELETE CASCADE" +
                    ");"
                );
            }

            System.out.println("✅ SQLite DB initialized (file: " + DB_FILE + ")");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize DB", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }
}
