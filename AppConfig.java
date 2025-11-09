import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class AppConfig {
    public static final int DEFAULT_INTERVAL_MONTHS = 6;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    // Professional Color scheme
    public static final Color PRIMARY_COLOR = new Color(44, 62, 80);     // Dark Blue Gray
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Professional Blue
    public static final Color ACCENT_COLOR = new Color(46, 204, 113);    // Professional Green
    public static final Color WARNING_COLOR = new Color(231, 76, 60);    // Professional Red
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);    // Dark Green
    public static final Color INFO_COLOR = new Color(155, 89, 182);      // Professional Purple
    public static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Light Gray
    public static final Color CARD_COLOR = Color.WHITE;
    public static final Color HEADER_COLOR = new Color(52, 73, 94);      // Dark Blue Gray

    public static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception ignored) {}
    }

    public static JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(160, 36));
        return b;
    }
    
    // Professional button styling
    public static JButton makeStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Subtle hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(brighterColor(bgColor, 15));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private static Color brighterColor(Color color, int amount) {
        int r = Math.min(255, color.getRed() + amount);
        int g = Math.min(255, color.getGreen() + amount);
        int b = Math.min(255, color.getBlue() + amount);
        return new Color(r, g, b);
    }
    
    // Professional table styling
    public static void styleTable(JTable table) {
        try {
            table.setRowHeight(32);
            table.setGridColor(new Color(230, 230, 230));
            table.setShowGrid(true);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            // Professional header styling
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setReorderingAllowed(false);
            
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    // Professional colors for headers
                    setBackground(HEADER_COLOR);
                    setForeground(Color.WHITE);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE),
                        BorderFactory.createEmptyBorder(8, 5, 8, 5)
                    ));
                    
                    return this;
                }
            });
            
            // Clean professional table rows
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, 
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (isSelected) {
                        c.setBackground(new Color(220, 230, 241)); // Light blue selection
                        c.setForeground(Color.BLACK);
                    } else {
                        // Clean alternating rows
                        if (row % 2 == 0) {
                            c.setBackground(Color.WHITE);
                        } else {
                            c.setBackground(new Color(248, 248, 248)); // Very light gray
                        }
                        c.setForeground(Color.BLACK);
                    }
                    
                    // Professional alignment
                    if (column == 0 || column == 3 || column == 7 || column == 8 || column == 9) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    return c;
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Paste this into AppConfig (replace the existing showDatePickerDialog)
public static void showDatePickerDialog(java.awt.Component parent, javax.swing.JTextField target, java.util.Date initialDate) {
    JDialog d = new JDialog(javax.swing.SwingUtilities.getWindowAncestor(parent), "Select Date", Dialog.ModalityType.APPLICATION_MODAL);
    d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    d.setResizable(false);

    JPanel root = new JPanel(new BorderLayout(10, 10));
    root.setBorder(new EmptyBorder(12, 12, 12, 12));
    root.setBackground(BACKGROUND_COLOR);

    Date init = (initialDate == null) ? new Date() : initialDate;
    SpinnerDateModel model = new SpinnerDateModel(init, null, null, Calendar.DAY_OF_MONTH);
    JSpinner spinner = new JSpinner(model);
    spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
    spinner.setPreferredSize(new Dimension(260, 28));

    JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
    center.setBackground(CARD_COLOR);
    center.add(spinner);
    root.add(center, BorderLayout.CENTER);

    JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
    btns.setBackground(CARD_COLOR);

    // Create plain JButtons and style them to be solid filled
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Cancel");

    // Common styling
    for (JButton b : new JButton[] { ok, cancel }) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(110, 36));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createLineBorder(new Color(0,0,0,40), 1));
    }

    // Colors
    ok.setBackground(SUCCESS_COLOR);     // green
    cancel.setBackground(WARNING_COLOR); // red/orange

    // subtle hover effect (brighten a bit)
    ok.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) { ok.setBackground(brighterColor(SUCCESS_COLOR, 18)); }
        public void mouseExited(java.awt.event.MouseEvent evt)  { ok.setBackground(SUCCESS_COLOR); }
    });
    cancel.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) { cancel.setBackground(brighterColor(WARNING_COLOR, 18)); }
        public void mouseExited(java.awt.event.MouseEvent evt)  { cancel.setBackground(WARNING_COLOR); }
    });

    btns.add(cancel);
    btns.add(ok);
    root.add(btns, BorderLayout.SOUTH);

    ok.addActionListener(ev -> {
        java.util.Date sel = model.getDate();
        target.setText(AppConfig.sdf.format(sel));
        d.dispose();
    });
    cancel.addActionListener(ev -> d.dispose());

    d.setContentPane(root);
    d.pack();
    d.setLocationRelativeTo(parent);
    d.setVisible(true);
}


}