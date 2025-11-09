import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class VehicleServiceApp {

static class EntryFrame extends JFrame {
    EntryFrame() {
        setTitle("Vehicle Service Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(100, 80));
        headerPanel.setBackground(AppConfig.HEADER_COLOR);
        JLabel title = new JLabel("Vehicle Service Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(20, 10, 10, 10));
        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center card
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel card = new JPanel(new GridLayout(0, 1, 15, 15));
        card.setPreferredSize(new Dimension(500, 280));
        card.setBackground(AppConfig.CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel cardTitle = new JLabel("Welcome", SwingConstants.CENTER);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        cardTitle.setForeground(AppConfig.HEADER_COLOR);
        card.add(cardTitle);

        JLabel subtitle = new JLabel("<html><center>Vehicle Service Management System<br>Schedule and track vehicle maintenance</center></html>",
            SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        card.add(subtitle);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        JButton btnAdmin = createSolidButton("Manager Login", AppConfig.PRIMARY_COLOR);
        JButton btnClient = createSolidButton("Client Login", AppConfig.SECONDARY_COLOR);

        btnPanel.add(btnAdmin);
        btnPanel.add(btnClient);
        card.add(btnPanel);

        JPanel hintPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hintPanel.setOpaque(false);
        JLabel hint = new JLabel("<html><font color='#666666'>New user? </font><font color='" +
            String.format("#%02x%02x%02x", AppConfig.PRIMARY_COLOR.getRed(),
                AppConfig.PRIMARY_COLOR.getGreen(), AppConfig.PRIMARY_COLOR.getBlue()) +
            "'><u>Register here</u></font></html>");
        hint.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // --- Fixed mouse listener: find parent window at runtime instead of using EntryFrame.this ---
        hint.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Optional debug
                // System.out.println("Register link clicked");

                // Find the parent window (safe, no use of "this" in ambiguous context)
                java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(hint);
                java.awt.Frame parentFrame = (parentWindow instanceof java.awt.Frame) ? (java.awt.Frame) parentWindow : null;

                javax.swing.SwingUtilities.invokeLater(() -> {
                    try {
                        RegisterFrame rf = new RegisterFrame(parentFrame);
                        rf.setLocationRelativeTo(parentFrame);
                        rf.setVisible(true);
                        if (parentWindow != null) parentWindow.setVisible(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        RegisterFrame rf = new RegisterFrame(null);
                        rf.setVisible(true);
                        if (parentWindow != null) parentWindow.setVisible(false);
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hint.setForeground(AppConfig.SECONDARY_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hint.setForeground(new Color(100, 100, 100));
            }
        });
        // -------------------------------------------------------------------------------

        hintPanel.add(hint);
        card.add(hintPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(card, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(AppConfig.BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel footer = new JLabel("Professional Vehicle Maintenance System", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footer.setForeground(new Color(120, 120, 120));
        footerPanel.add(footer);
        add(footerPanel, BorderLayout.SOUTH);

        // Button actions
        btnAdmin.addActionListener(ae -> {
            new ManagerLoginFrame(this).setVisible(true);
            setVisible(false);
        });
        btnClient.addActionListener(ae -> {
            new ClientLoginFrame(this).setVisible(true);
            setVisible(false);
        });
    }
}
// Inside VehicleServiceApp.java

static class RegisterFrame extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton registerBtn = new JButton("Register");
    private final JButton backBtn = new JButton("Back");
    private final java.awt.Frame parentFrame;

    RegisterFrame() { this(null); }

    RegisterFrame(java.awt.Frame parent) {
        super("Client Registration");
        this.parentFrame = parent;
        initComponents();
        pack();
        setResizable(false);
        setLocationRelativeTo(parentFrame);
    }

    private void initComponents() {
        // Root Panel
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(AppConfig.BACKGROUND_COLOR);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppConfig.HEADER_COLOR);
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("Register New Client", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Form Panel
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(AppConfig.BACKGROUND_COLOR);
        root.add(center, BorderLayout.CENTER);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 8, 10);
        gbc.anchor = GridBagConstraints.LINE_END;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Dimension fieldSize = new Dimension(340, 28);

        // Labels and Fields
        addFormRow(card, gbc, 0, "Full Name:", nameField, labelFont, fieldSize);
        addFormRow(card, gbc, 1, "Phone:", phoneField, labelFont, fieldSize);
        addFormRow(card, gbc, 2, "Email:", emailField, labelFont, fieldSize);
        addFormRow(card, gbc, 3, "Username:", usernameField, labelFont, fieldSize);
        addFormRow(card, gbc, 4, "Password:", passwordField, labelFont, fieldSize);

        // Add form to center
        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = 0; cc.gridy = 0;
        cc.weightx = 1.0; cc.weighty = 1.0;
        cc.anchor = GridBagConstraints.CENTER;
        center.add(card, cc);

        // Footer Buttons (SOLID STYLE)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setBackground(AppConfig.BACKGROUND_COLOR);

        styleButton(backBtn, new Color(90, 90, 90), new Color(70, 70, 70));
        styleButton(registerBtn, new Color(40, 167, 69), new Color(30, 140, 55));

        footer.add(backBtn);
        footer.add(registerBtn);
        root.add(footer, BorderLayout.SOUTH);

        // Button Actions
        backBtn.addActionListener(e -> {
            dispose();
            if (parentFrame != null) parentFrame.setVisible(true);
            else SwingUtilities.invokeLater(() -> new VehicleServiceApp.EntryFrame().setVisible(true));
        });

        registerBtn.addActionListener(e -> registerClient());
    }

    // Helper to add form rows
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field, Font font, Dimension fieldSize) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(fieldSize);
        panel.add(field, gbc);
    }

    // Helper to style solid buttons
    private void styleButton(JButton button, Color baseColor, Color hoverColor) {
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    private void registerClient() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim().toLowerCase();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ValidationResult r;

        r = Validators.validatePhone(phone);
        if (!r.isValid()) {
            JOptionPane.showMessageDialog(this, r.getMessage(), "Invalid Phone", JOptionPane.ERROR_MESSAGE);
            return;
        }

        r = Validators.validateEmail(email);
        if (!r.isValid()) {
            JOptionPane.showMessageDialog(this, r.getMessage(), "Invalid Email", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (ClientDAO.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists. Choose another.", "Duplicate", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client c = new Client(username, password, name, phone, email);
            ClientDAO.insertClient(c);

            JOptionPane.showMessageDialog(this, "Registered successfully! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if (parentFrame != null) parentFrame.setVisible(true);
            else SwingUtilities.invokeLater(() -> new VehicleServiceApp.EntryFrame().setVisible(true));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}






    static class ManagerLoginFrame extends JFrame {
        ManagerLoginFrame(JFrame parent) {
            setTitle("Manager Login");
            setSize(400, 200);
            setLocationRelativeTo(null);
            getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
            setLayout(new BorderLayout(8, 8));
            
            JPanel center = new JPanel(new GridLayout(2, 2, 8, 10));
            center.setBorder(new EmptyBorder(20, 20, 20, 20));
            center.setBackground(AppConfig.CARD_COLOR);
            
            JTextField tfUser = new JTextField();
            JPasswordField pf = new JPasswordField();
            center.add(new JLabel("Username:"));
            center.add(tfUser);
            center.add(new JLabel("Password:"));
            center.add(pf);
            add(center, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottom.setBackground(AppConfig.BACKGROUND_COLOR);
            JButton btnLogin = createSolidButton("Login", AppConfig.PRIMARY_COLOR);
            btnLogin.setPreferredSize(new Dimension(100, 36));
            JButton btnBack = createSolidButton("Back", new Color(120, 120, 120));
            btnBack.setPreferredSize(new Dimension(100, 36));
            bottom.add(btnBack);
            bottom.add(btnLogin);
            add(bottom, BorderLayout.SOUTH);

            btnLogin.addActionListener(e -> {
                String u = tfUser.getText().trim();
                String p = new String(pf.getPassword()).trim();
                if (u.equals(DataStore.MANAGER_USER) && p.equals(DataStore.MANAGER_PASS)) {
                    new ManagerDashboard(this).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid manager credentials.", "Auth failed", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnBack.addActionListener(e -> {
                parent.setVisible(true);
                dispose();
            });
        }
    }

    static class ClientLoginFrame extends JFrame {
        ClientLoginFrame(JFrame parent) {
            setTitle("Client Login");
            setSize(400, 200);
            setLocationRelativeTo(null);
            getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
            setLayout(new BorderLayout(8, 8));
            
            JPanel center = new JPanel(new GridLayout(2, 2, 8, 10));
            center.setBorder(new EmptyBorder(20, 20, 20, 20));
            center.setBackground(AppConfig.CARD_COLOR);
            
            JTextField tfUser = new JTextField();
            JPasswordField pf = new JPasswordField();
            center.add(new JLabel("Username:"));
            center.add(tfUser);
            center.add(new JLabel("Password:"));
            center.add(pf);
            add(center, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottom.setBackground(AppConfig.BACKGROUND_COLOR);
            JButton btnLogin = createSolidButton("Login", AppConfig.SECONDARY_COLOR);
            btnLogin.setPreferredSize(new Dimension(100, 36));
            JButton btnBack = createSolidButton("Back", new Color(120, 120, 120));
            btnBack.setPreferredSize(new Dimension(100, 36));
            bottom.add(btnBack);
            bottom.add(btnLogin);
            add(bottom, BorderLayout.SOUTH);

            btnLogin.addActionListener(e -> {
                String u = tfUser.getText().trim();
                String p = new String(pf.getPassword()).trim();
                try {
                    Client c = ClientDAO.findByCredentials(u, p);
                    if (c != null) {
                        ClientDashboard cd = new ClientDashboard(this, c);
                        cd.setVisible(true);
                        String reminder = buildOverdueReminderForClient(c.username);
                        if (!reminder.isEmpty()) {
                            JOptionPane.showMessageDialog(cd, reminder, "Overdue Reminders", JOptionPane.WARNING_MESSAGE);
                        }
                        dispose();
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Invalid client credentials or no such user.", "Auth failed", JOptionPane.ERROR_MESSAGE);
            });

            btnBack.addActionListener(e -> {
                if (parent != null) parent.setVisible(true);
                else new EntryFrame().setVisible(true);
                dispose();
            });
        }

        String buildOverdueReminderForClient(String username) {
            StringBuilder sb = new StringBuilder();
            try {
                java.util.List<Booking> bookings = BookingDAO.findBookingsForClient(username);
                Date now = new Date();
                for (Booking b : bookings) {
                    if (!b.completed && b.nextServiceDate().before(now)) {
                        sb.append("Booking ID ").append(b.id)
                          .append(" (").append(b.vehicleModel).append(") was due on ").append(AppConfig.sdf.format(b.nextServiceDate()))
                          .append("\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

  // Inside VehicleServiceApp.java

static class ManagerDashboard extends JFrame {
    JTable table;
    DefaultTableModel model;

    ManagerDashboard(JFrame parent) {
        setTitle("Manager Dashboard - Vehicle Service Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Left toolbar
        JPanel leftToolbar = new JPanel();
        leftToolbar.setLayout(new BoxLayout(leftToolbar, BoxLayout.Y_AXIS));
        leftToolbar.setBorder(new EmptyBorder(15, 15, 15, 15));
        leftToolbar.setBackground(AppConfig.CARD_COLOR);

        // Professional buttons with solid fill
        JButton btnViewAll = createSolidButton("View All Bookings", AppConfig.PRIMARY_COLOR);
        JButton btnAddService = createSolidButton("Add New Booking", AppConfig.SUCCESS_COLOR);
        JButton btnOverdue = createSolidButton("Check Overdue", AppConfig.WARNING_COLOR);
        JButton btnMarkComplete = createSolidButton("Mark Completed", new Color(46, 125, 50));
        JButton btnCancelAppointment = createSolidButton("Cancel Booking", new Color(192, 57, 43));
        JButton btnViewClients = createSolidButton("View Clients", AppConfig.INFO_COLOR);
        JButton btnLogout = createSolidButton("Logout", new Color(120, 120, 120));

        Dimension btnSize = new Dimension(160, 40);
        for (JButton b : new JButton[]{btnViewAll, btnAddService, btnOverdue, btnMarkComplete, btnCancelAppointment, btnViewClients, btnLogout}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnSize.height));
            b.setPreferredSize(btnSize);
            leftToolbar.add(b);
            leftToolbar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // --- NEW: Send 15-Day Reminders button (placed with the other toolbar buttons)
        JButton btnSendReminders = createSolidButton("Send 15-Day Reminders", new Color(46, 139, 87));
        btnSendReminders.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSendReminders.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnSize.height));
        btnSendReminders.setPreferredSize(btnSize);
        leftToolbar.add(btnSendReminders);
        leftToolbar.add(Box.createRigidArea(new Dimension(0, 10)));

        add(leftToolbar, BorderLayout.WEST);

        // Main table and center area
        String[] cols = {"ID", "Client", "Vehicle", "Year", "Last Service", "Next Service Date", "Appointment Date", "Interval (months)", "Completed", "Cost"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        AppConfig.styleTable(table);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);
        table.getColumnModel().getColumn(9).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom / actions panel (re-uses some of your existing behavior)
        JPanel bottomActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomActions.setBackground(AppConfig.BACKGROUND_COLOR);
        add(bottomActions, BorderLayout.SOUTH);

        // Wire up existing buttons
        btnViewAll.addActionListener(e -> refreshTable());
        btnAddService.addActionListener(e -> new AddServiceFrame(this).setVisible(true));
        btnOverdue.addActionListener(e -> showOverdue());
        btnMarkComplete.addActionListener(e -> markSelectedCompleted());
        btnCancelAppointment.addActionListener(e -> cancelAppointmentManager());
        btnViewClients.addActionListener(e -> new ClientsFrame(this).setVisible(true));
        btnLogout.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                new EntryFrame().setVisible(true);
                dispose();
            }
        });

        // --- NEW: Handler for Send 15-Day Reminders ---
btnSendReminders.addActionListener(ev -> {
    int confirm = JOptionPane.showConfirmDialog(this, "Send 15-day reminders now?", "Confirm", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    SwingWorker<ReminderSender.Result, Void> worker = new SwingWorker<>() {
        private ReminderSender.Result result;
        @Override
        protected ReminderSender.Result doInBackground() {
            result = ReminderSender.sendRemindersDaysBefore(15);
            return result;
        }

        @Override
        protected void done() {
            try {
                ReminderSender.Result res = result; // already set in doInBackground
                String msg = "Reminders sent: " + res.sent + "\nSkipped (no email): " + res.skippedNoEmail;
                if (!res.errors.isEmpty()) {
                    msg += "\n\nErrors:\n" + String.join("\n", res.errors);
                }
                JOptionPane.showMessageDialog(ManagerDashboard.this, msg, "Reminder Status", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ManagerDashboard.this, "Error while sending reminders: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    worker.execute();
});


        // initial load
        refreshTable();
    }

    void refreshTable() {
        model.setRowCount(0);
        try {
            java.util.List<Booking> list = BookingDAO.findAllBookings();
            for (Booking b : list) {
                model.addRow(new Object[]{
                    b.id,
                    b.clientUsername,
                    b.vehicleModel,
                    b.year,
                    AppConfig.sdf.format(b.lastServiceDate),
                    AppConfig.sdf.format(b.nextServiceDate()),
                    (b.appointmentDate != null ? AppConfig.sdf.format(b.appointmentDate) : ""),
                    b.serviceIntervalMonths,
                    b.completed ? "Yes" : "No",
                    b.cost == 0 ? "-" : "$" + b.cost
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void showOverdue() {
        model.setRowCount(0);
        try {
            java.util.List<Booking> list = BookingDAO.findOverdueBookings();
            for (Booking b : list) {
                model.addRow(new Object[]{
                    b.id,
                    b.clientUsername,
                    b.vehicleModel,
                    b.year,
                    AppConfig.sdf.format(b.lastServiceDate),
                    AppConfig.sdf.format(b.nextServiceDate()),
                    (b.appointmentDate != null ? AppConfig.sdf.format(b.appointmentDate) : ""),
                    b.serviceIntervalMonths,
                    b.completed ? "Yes" : "No",
                    b.cost == 0 ? "-" : "$" + b.cost
                });
            }
            if (model.getRowCount() == 0) JOptionPane.showMessageDialog(this, "No overdue services found.", "Overdue", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void markSelectedCompleted() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a booking first.", "No selection", JOptionPane.WARNING_MESSAGE); return; }
        int id = (int) model.getValueAt(row, 0);

        Booking target = null;
        try {
            java.util.List<Booking> list = BookingDAO.findAllBookings();
            for (Booking b : list) {
                if (b.id == id) { target = b; break; }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (target == null) {
            JOptionPane.showMessageDialog(this, "Selected booking not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (target.completed) {
            JOptionPane.showMessageDialog(this, "Booking already completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Date requiredDate = (target.appointmentDate != null) ? target.appointmentDate : target.nextServiceDate();
        Date today = truncateToDate(new Date());
        Date req = truncateToDate(requiredDate);

        if (today.before(req)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot mark completed yet.\nYou can mark this booking completed on or after: " + AppConfig.sdf.format(req),
                    "Too early", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String costStr = JOptionPane.showInputDialog(this, "Enter service cost (integer):", "0");
        if (costStr == null) return;
        int cost = 0;
        try { cost = Integer.parseInt(costStr.trim()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid cost, using 0."); }

        try {
            BookingDAO.markCompleted(id, cost);
            JOptionPane.showMessageDialog(this, "Booking marked completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelAppointmentManager() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a booking first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) model.getValueAt(row, 0);

        Booking target = null;
        try {
            java.util.List<Booking> list = BookingDAO.findAllBookings();
            for (Booking b : list) {
                if (b.id == id) { target = b; break; }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (target == null) {
            JOptionPane.showMessageDialog(this, "Selected booking not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (target.completed) {
            JOptionPane.showMessageDialog(this, "Cannot delete a completed booking.", "Not allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int r = JOptionPane.showConfirmDialog(this, "Delete booking (ID " + id + ")? This will remove the booking permanently.", "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        try {
            BookingDAO.deleteBooking(target.id);
            JOptionPane.showMessageDialog(this, "Booking deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date truncateToDate(Date d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}



    static class ClientsFrame extends JFrame {
        DefaultTableModel model;
        JTable table;

        ClientsFrame(JFrame parent) {
            setTitle("Registered Clients");
            setSize(700, 450);
            setLocationRelativeTo(null);
            getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
            setLayout(new BorderLayout(8,8));

            String[] cols = {"Username", "Name", "Phone", "Email"};
            model = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            table = new JTable(model);
            AppConfig.styleTable(table);
            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottom.setBackground(AppConfig.BACKGROUND_COLOR);
            JButton btnRefresh = createSolidButton("Refresh", AppConfig.PRIMARY_COLOR);
            btnRefresh.setPreferredSize(new Dimension(120, 38));
            JButton btnClose = createSolidButton("Close", new Color(120, 120, 120));
            btnClose.setPreferredSize(new Dimension(120, 38));
            bottom.add(btnRefresh);
            bottom.add(btnClose);
            add(bottom, BorderLayout.SOUTH);

            btnRefresh.addActionListener(e -> refresh());
            btnClose.addActionListener(e -> dispose());

            table.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.rowAtPoint(e.getPoint());
                        if (row == -1) return;
                        String username = (String) model.getValueAt(row, 0);
                        String name = (String) model.getValueAt(row, 1);
                        String phone = (String) model.getValueAt(row, 2);
                        String email = (String) model.getValueAt(row, 3);

                        StringBuilder sb = new StringBuilder();
                        sb.append("Username: ").append(username).append("\n");
                        sb.append("Name: ").append(name).append("\n");
                        sb.append("Phone: ").append(phone == null ? "" : phone).append("\n");
                        sb.append("Email: ").append(email == null ? "" : email).append("\n");

                        JOptionPane.showMessageDialog(ClientsFrame.this, sb.toString(), "Client Details", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            refresh();
        }

        void refresh() {
            model.setRowCount(0);
            try {
                java.util.List<Client> clients = ClientDAO.findAllClients();
                for (Client c : clients) {
                    model.addRow(new Object[]{ c.username, c.name, c.phone == null ? "" : c.phone, c.email == null ? "" : c.email });
                }
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No clients found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class AddServiceFrame extends JFrame {
        AddServiceFrame(ManagerDashboard parent) {
            setTitle("Add New Service Booking");
            setSize(520, 460);
            setLocationRelativeTo(null);
            getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
            setLayout(new BorderLayout(8, 8));
            
            JPanel form = new JPanel(new GridLayout(6, 2, 8, 10));
            form.setBorder(new EmptyBorder(20, 20, 20, 20));
            form.setBackground(AppConfig.CARD_COLOR);

            JTextField tfClient = new JTextField();
            JTextField tfVehicle = new JTextField();
            JTextField tfYear = new JTextField();
            JTextField tfLastService = new JTextField();
            JTextField tfInterval = new JTextField(String.valueOf(AppConfig.DEFAULT_INTERVAL_MONTHS));
            JTextField tfAppointment = new JTextField();

            tfLastService.setEditable(true);
            tfLastService.setToolTipText("Click to pick a date or type yyyy-MM-dd");
            tfLastService.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AppConfig.showDatePickerDialog(AddServiceFrame.this, tfLastService, null);
                }
            });

            tfAppointment.setEditable(true);
            tfAppointment.setToolTipText("Optional: click to pick a date or type yyyy-MM-dd");
            tfAppointment.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AppConfig.showDatePickerDialog(AddServiceFrame.this, tfAppointment, null);
                }
            });

            form.add(new JLabel("Client username:"));
            form.add(tfClient);
            form.add(new JLabel("Vehicle model:"));
            form.add(tfVehicle);
            form.add(new JLabel("Vehicle Year:"));
            form.add(tfYear);
            form.add(new JLabel("Last service date:"));
            form.add(tfLastService);
            form.add(new JLabel("Interval (months):"));
            form.add(tfInterval);
            form.add(new JLabel("Appointment Date:"));
            form.add(tfAppointment);

            add(form, BorderLayout.CENTER);

            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            south.setBackground(AppConfig.BACKGROUND_COLOR);
            JButton btnAdd = createSolidButton("Add Booking", AppConfig.SUCCESS_COLOR);
            JButton btnCancel = createSolidButton("Cancel", new Color(120, 120, 120));
            south.add(btnCancel);
            south.add(btnAdd);
            add(south, BorderLayout.SOUTH);

            btnAdd.addActionListener(ev -> {
                String clientInput = tfClient.getText().trim();
                String vehicle = tfVehicle.getText().trim();
                String yearStr = tfYear.getText().trim();
                String lastServiceStr = tfLastService.getText().trim();
                String intervalStr = tfInterval.getText().trim();
                String appointmentStr = tfAppointment.getText().trim();
                ValidationResult vr = Validators.validateVehicleYearAndLastService(yearStr, lastServiceStr, intervalStr);
                if (!vr.valid) {
                    JOptionPane.showMessageDialog(this, vr.message, "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String canonicalUsername = null;
                try {
                    if (!ClientDAO.usernameExists(clientInput.toLowerCase())) {
                        JOptionPane.showMessageDialog(this, "Client username not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        canonicalUsername = clientInput.toLowerCase();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ValidationResult constraint = Validators.checkBookingConstraints(vehicle, vr.lastServiceDate);
                if (!constraint.valid) {
                    JOptionPane.showMessageDialog(this, constraint.message, "Booking blocked", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date appointmentDate = null;
                if (!appointmentStr.isEmpty()) {
                    try {
                        appointmentDate = AppConfig.sdf.parse(appointmentStr);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Invalid appointment date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Booking temp = new Booking(canonicalUsername, vehicle, vr.vehicleYear, vr.lastServiceDate, vr.intervalMonths);
                    Date nextService = temp.nextServiceDate();
                    if (appointmentDate.before(nextService)) {
                        JOptionPane.showMessageDialog(this,
                                "Invalid Appointment Date: must be on or after the next service date (" + AppConfig.sdf.format(nextService) + ").",
                                "Invalid Appointment Date", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Booking b = new Booking(canonicalUsername, vehicle, vr.vehicleYear, vr.lastServiceDate, vr.intervalMonths);
                b.appointmentDate = appointmentDate;
                try {
                    BookingDAO.insertBooking(b);
                    JOptionPane.showMessageDialog(this, "Booking added. ID: " + b.id, "Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.refreshTable();
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnCancel.addActionListener(e -> dispose());
        }
    }

    static class ClientDashboard extends JFrame {
        Client client;
        DefaultTableModel model;
        JTable table;

        ClientDashboard(JFrame parent, Client client) {
            this.client = client;
            setTitle("Client Dashboard - " + client.name);
            setSize(1000, 600);
            setLocationRelativeTo(null);
            getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
            setLayout(new BorderLayout(8, 8));

            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            top.setBorder(new EmptyBorder(10, 10, 10, 10));
            top.setBackground(AppConfig.CARD_COLOR);
            
            // Professional buttons for client dashboard with solid fill
            JButton btnViewHistory = createSolidButton("View History", AppConfig.PRIMARY_COLOR);
            JButton btnBookService = createSolidButton("Book Service", AppConfig.SUCCESS_COLOR);
            JButton btnEditAppointment = createSolidButton("Edit Appointment", AppConfig.INFO_COLOR);
            JButton btnCancelAppointment = createSolidButton("Cancel Booking", AppConfig.WARNING_COLOR);
            JButton btnLogout = createSolidButton("Logout", new Color(120, 120, 120));
            
            top.add(btnViewHistory);
            top.add(btnBookService);
            top.add(btnEditAppointment);
            top.add(btnCancelAppointment);
            top.add(btnLogout);
            add(top, BorderLayout.NORTH);

            String[] cols = {"ID", "Vehicle", "Year", "Last Service", "Next Service Date", "Appointment Date", "Interval (months)", "Completed", "Cost"};
            model = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            table = new JTable(model);
            AppConfig.styleTable(table);
            add(new JScrollPane(table), BorderLayout.CENTER);

            btnViewHistory.addActionListener(e -> {
                refresh();
                String reminder = buildOverdueReminderForClient(client.username);
                if (!reminder.isEmpty()) {
                    JOptionPane.showMessageDialog(this, reminder, "Overdue Reminders", JOptionPane.WARNING_MESSAGE);
                }
            });

            btnBookService.addActionListener(e -> new BookServiceFrame(this, client).setVisible(true));
            btnEditAppointment.addActionListener(e -> editSelectedAppointment());
            btnCancelAppointment.addActionListener(e -> cancelSelectedAppointment());
            btnLogout.addActionListener(e -> {
                int r = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    new EntryFrame().setVisible(true);
                    dispose();
                }
            });

            refresh();
        }

        void refresh() {
            model.setRowCount(0);
            try {
                java.util.List<Booking> list = BookingDAO.findBookingsForClient(client.username);
                for (Booking b : list) {
                    model.addRow(new Object[]{
                        b.id,
                        b.vehicleModel,
                        b.year,
                        AppConfig.sdf.format(b.lastServiceDate),
                        AppConfig.sdf.format(b.nextServiceDate()),
                        (b.appointmentDate != null ? AppConfig.sdf.format(b.appointmentDate) : ""),
                        b.serviceIntervalMonths,
                        b.completed ? "Yes" : "No",
                        b.cost == 0 ? "-" : "$" + b.cost
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String buildOverdueReminderForClient(String username) {
            StringBuilder sb = new StringBuilder();
            Date now = new Date();
            for (Booking b : DataStore.bookings) {
                if (b.clientUsername != null && b.clientUsername.trim().equalsIgnoreCase(username.trim()) && !b.completed && b.nextServiceDate().before(now)) {
                    sb.append("Booking ID ").append(b.id)
                            .append(" (").append(b.vehicleModel).append(") was due on ").append(AppConfig.sdf.format(b.nextServiceDate()))
                            .append("\n");
                }
            }
            return sb.toString();
        }

        void editSelectedAppointment() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a booking first.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) model.getValueAt(row, 0);

            Booking target = null;
            try {
                java.util.List<Booking> bookings = BookingDAO.findBookingsForClient(client.username);
                for (Booking b : bookings) {
                    if (b.id == id) { target = b; break; }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (target == null) {
                JOptionPane.showMessageDialog(this, "Selected booking not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (target.completed) {
                JOptionPane.showMessageDialog(this, "Cannot edit appointment for a completed booking.", "Not allowed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String current = (target.appointmentDate != null) ? AppConfig.sdf.format(target.appointmentDate) : "";
            String prompt = "Current appointment: " + (current.isEmpty() ? "(none)" : current) + "\nSelect new appointment date (yyyy-MM-dd):";
            JTextField tf = new JTextField(current);
            tf.setToolTipText("Click to pick a date or type yyyy-MM-dd");
            final Booking selectedBooking = target;
            tf.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AppConfig.showDatePickerDialog(ClientDashboard.this, tf, selectedBooking.appointmentDate);
                }
            });

            int res = JOptionPane.showConfirmDialog(this, tf, prompt, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;

            String newApptStr = tf.getText().trim();
            if (newApptStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Appointment date cannot be empty.", "Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date newAppt;
            try {
                newAppt = AppConfig.sdf.parse(newApptStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date nextService = target.nextServiceDate();
            if (newAppt.before(nextService)) {
                JOptionPane.showMessageDialog(this, "Invalid Appointment Date: must be on or after the next service date (" + AppConfig.sdf.format(nextService) + ").", "Invalid", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                BookingDAO.updateAppointmentDate(target.id, newAppt);
                target.appointmentDate = newAppt;
                JOptionPane.showMessageDialog(this, "Appointment updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        void cancelSelectedAppointment() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a booking first.", "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) model.getValueAt(row, 0);

            Booking target = null;
            try {
                java.util.List<Booking> bookings = BookingDAO.findBookingsForClient(client.username);
                for (Booking b : bookings) {
                    if (b.id == id) { target = b; break; }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (target == null) {
                JOptionPane.showMessageDialog(this, "Selected booking not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (target.completed) {
                JOptionPane.showMessageDialog(this, "Cannot delete a completed booking.", "Not allowed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int r = JOptionPane.showConfirmDialog(this, "Delete booking (ID " + id + ")? This will remove the booking permanently.", "Confirm delete", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) return;

            try {
                BookingDAO.deleteBooking(target.id);
                JOptionPane.showMessageDialog(this, "Booking deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class BookServiceFrame extends JFrame {
        BookServiceFrame(ClientDashboard parent, Client client) {
            setTitle("Book Service - " + client.username);
            setSize(520, 460);
            setLocationRelativeTo(null);
            getContentPane().setBackground(AppConfig.BACKGROUND_COLOR);
            setLayout(new BorderLayout(8, 8));
            
            JPanel form = new JPanel(new GridLayout(5, 2, 8, 10));
            form.setBorder(new EmptyBorder(20, 20, 20, 20));
            form.setBackground(AppConfig.CARD_COLOR);

            JTextField tfVehicle = new JTextField();
            JTextField tfYear = new JTextField();
            JTextField tfLastService = new JTextField();
            JTextField tfInterval = new JTextField(String.valueOf(AppConfig.DEFAULT_INTERVAL_MONTHS));
            JTextField tfAppointment = new JTextField();

            tfLastService.setEditable(true);
            tfLastService.setToolTipText("Click to pick a date or type yyyy-MM-dd");
            tfLastService.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AppConfig.showDatePickerDialog(BookServiceFrame.this, tfLastService, null);
                }
            });

            tfAppointment.setEditable(true);
            tfAppointment.setToolTipText("Click to pick a date or type yyyy-MM-dd");
            tfAppointment.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AppConfig.showDatePickerDialog(BookServiceFrame.this, tfAppointment, null);
                }
            });

            form.add(new JLabel("Vehicle model:"));
            form.add(tfVehicle);
            form.add(new JLabel("Vehicle Year:"));
            form.add(tfYear);
            form.add(new JLabel("Last service date:"));
            form.add(tfLastService);
            form.add(new JLabel("Interval (months):"));
            form.add(tfInterval);
            form.add(new JLabel("Appointment Date:"));
            form.add(tfAppointment);

            add(form, BorderLayout.CENTER);

            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            south.setBackground(AppConfig.BACKGROUND_COLOR);
            JButton btnBook = createSolidButton("Book Service", AppConfig.SUCCESS_COLOR);
            JButton btnCancel = createSolidButton("Cancel", new Color(120, 120, 120));
            south.add(btnCancel);
            south.add(btnBook);
            add(south, BorderLayout.SOUTH);

            btnBook.addActionListener(ev -> {
                String veh = tfVehicle.getText().trim();
                String yearStr = tfYear.getText().trim();
                String lastServiceStr = tfLastService.getText().trim();
                String intervalStr = tfInterval.getText().trim();
                String appointmentStr = tfAppointment.getText().trim();

                ValidationResult vr = Validators.validateVehicleYearAndLastService(yearStr, lastServiceStr, intervalStr);
                if (!vr.valid) {
                    JOptionPane.showMessageDialog(this, vr.message, "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (appointmentStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select appointment date.", "Missing field", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Date appointmentDate;
                try {
                    appointmentDate = AppConfig.sdf.parse(appointmentStr);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid appointment date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ValidationResult constraint = Validators.checkBookingConstraints(veh, vr.lastServiceDate);
                if (!constraint.valid) {
                    JOptionPane.showMessageDialog(this, constraint.message, "Booking blocked", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Booking temp = new Booking(client.username, veh, vr.vehicleYear, vr.lastServiceDate, vr.intervalMonths);
                Date nextService = temp.nextServiceDate();
                if (appointmentDate.before(nextService)) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid Appointment Date: must be on or after the next service date (" + AppConfig.sdf.format(nextService) + ").",
                            "Invalid Appointment Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Booking newBooking = new Booking(client.username, veh, vr.vehicleYear, vr.lastServiceDate, vr.intervalMonths);
                newBooking.appointmentDate = appointmentDate;
                try {
                    BookingDAO.insertBooking(newBooking);
                    JOptionPane.showMessageDialog(this, "Service booked. Booking ID: " + newBooking.id, "Success", JOptionPane.INFORMATION_MESSAGE);
                    parent.refresh();
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnCancel.addActionListener(e -> dispose());
        }
    }

    // Solid fill button creation method
    private static JButton createSolidButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fill solid background
                g2.setColor(backgroundColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(Cursor.getDefaultCursor());
            }
        });
        
        return button;
    }

    public static void main(String[] args) {
    // Apply platform L&F early
    AppConfig.applySystemLookAndFeel();

    // Ensure strict date parsing (your code expects yyyy-MM-dd)
    AppConfig.sdf.setLenient(false);

    // Optional: seed a demo client (remove if you don't want a default client)
    try {
        if (!ClientDAO.usernameExists("john")) {
            ClientDAO.insertClient(new Client("john", "pass123", "John Doe", "9999999999", "john@example.com"));
        }
    } catch (Exception ex) {
        // ignore DB seed failure (app can still run)
        ex.printStackTrace();
    }

    // Launch the main entry frame on the EDT (correct Swing startup pattern)
    javax.swing.SwingUtilities.invokeLater(() -> {
        new VehicleServiceApp.EntryFrame().setVisible(true);
    });
}

}
