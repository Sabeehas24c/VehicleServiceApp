import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public static boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM clients WHERE username = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void insertClient(Client client) throws SQLException {
        String sql = "INSERT INTO clients (username, password, name, phone, email) VALUES (?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, client.username);
            ps.setString(2, client.password);
            ps.setString(3, client.name);
            ps.setString(4, client.phone);
            ps.setString(5, client.email);
            ps.executeUpdate();
        }
    }

    public static Client findByCredentials(String username, String password) throws SQLException {
        String sql = "SELECT username, password, name, phone, email FROM clients WHERE username = ? AND password = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                    );
                }
                return null;
            }
        }
    }

    // NEW: find by username (used by ReminderSender)
    public static Client findByUsername(String username) throws SQLException {
        String sql = "SELECT username, password, name, phone, email FROM clients WHERE username = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                    );
                }
                return null;
            }
        }
    }

    // Return all clients
    public static List<Client> findAllClients() throws SQLException {
        String sql = "SELECT username, password, name, phone, email FROM clients ORDER BY username";
        List<Client> out = new ArrayList<>();
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Client(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        }
        return out;
    }

    // Update client info
    public static void updateClient(Client client) throws SQLException {
        String sql = "UPDATE clients SET password = ?, name = ?, phone = ?, email = ? WHERE username = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, client.password);
            ps.setString(2, client.name);
            ps.setString(3, client.phone);
            ps.setString(4, client.email);
            ps.setString(5, client.username);
            ps.executeUpdate();
        }
    }

    // Delete client
    public static void deleteClient(String username) throws SQLException {
        String sql = "DELETE FROM clients WHERE username = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        }
    }
}
