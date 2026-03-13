package hospital.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.util.List;

import hospital.dao.UserDAO;
import hospital.models.UserWithHospital;

public class UserManagement {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private UserDAO userDAO = new UserDAO();

    // --- Theme Colors ---
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(26, 188, 156);
    private final Color DARK_BACKGROUND_PRIMARY = new Color(30, 41, 51);
    private final Color DARK_BACKGROUND_SECONDARY = new Color(42, 54, 66);
    private final Color FOREGROUND_LIGHT = new Color(236, 240, 241);
    private final Color TABLE_HEADER_BG = new Color(22, 31, 38);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color SELECTION_COLOR = new Color(58, 83, 107);
    private final Color PENDING_COLOR = new Color(243, 156, 18, 180); // Yellowish
    private final Color APPROVED_COLOR = new Color(39, 174, 96, 180); // Greenish
    private final Color REJECTED_COLOR = new Color(231, 76, 60, 180); // Redish
    
    private final Color TEXTFIELD_BG = new Color(58, 70, 80);

    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public UserManagement(JFrame dashboardFrame) {
        // --- Frame Setup ---
        frame = new JFrame("User Management - Smart Hospital System");
        frame.setSize(1000, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // --- Background Panel ---
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(DARK_BACKGROUND_PRIMARY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        frame.setContentPane(backgroundPanel);

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR.brighter(), w, h, ACCENT_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        topPanel.setPreferredSize(new Dimension(1000, 80));
        topPanel.setOpaque(false);

        JButton backBtn = createStyledButton("← Back", new Color(22, 31, 38), e -> {
            dashboardFrame.setVisible(true);
            frame.dispose();
        });
        backBtn.setFont(LABEL_FONT);

        JLabel titleLabel = new JLabel("User Management", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(FOREGROUND_LIGHT);

        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        // --- Center Table Panel ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.setOpaque(false);

        table = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Name", "Role", "Hospital"});
        table.setModel(tableModel);

        // // Sample data
        // tableModel.addRow(new Object[]{"U001", "Dr. Smith", "Doctor", "City General Hospital"});
        // tableModel.addRow(new Object[]{"U002", "John Doe", "Patient", "St. Jude Clinic"});
        // tableModel.addRow(new Object[]{"U003", "Nurse Alice", "Nurse", "City General Hospital"});
        // tableModel.addRow(new Object[]{"U004", "Admin Bob", "Administrator", "Regional Medical Center"});

        UserDAO userDAO = new UserDAO();
        List<UserWithHospital> users = userDAO.getAllUsersWithHospital();

        for (UserWithHospital u : users) {
            tableModel.addRow(new Object[]{
                u.getUserId(),
                u.getRealName(),  
                u.getRole(),
                u.getHospitalName()
            });
        }

        table.setBackground(new Color(DARK_BACKGROUND_SECONDARY.getRed(), DARK_BACKGROUND_SECONDARY.getGreen(), DARK_BACKGROUND_SECONDARY.getBlue(), 220));
        table.setForeground(FOREGROUND_LIGHT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setForeground(FOREGROUND_LIGHT);
        table.setRowHeight(35);
        table.setFont(LABEL_FONT);
        table.setGridColor(new Color(60, 70, 80));
        table.setSelectionBackground(SELECTION_COLOR);
        table.setSelectionForeground(FOREGROUND_LIGHT);

        DefaultTableCellRenderer darkCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                Color rowBg1 = new Color(40, 50, 60, 200);
                Color rowBg2 = new Color(50, 60, 70, 200);
                // Default alternating row color
                if (row % 2 == 0) c.setBackground(new Color(40,50,60,200));
                else c.setBackground(new Color(50,60,70,200));

                Object roleObj = tableModel.getValueAt(row, 2);
                // Object statusObj = tableModel.getValueAt(row, 4);

                // if (roleObj != null && roleObj.toString().equals("Administrator") && statusObj != null) {
                //     String status = statusObj.toString();
                //     if (status.equalsIgnoreCase("Pending")) c.setBackground(PENDING_COLOR);
                //     else if (status.equalsIgnoreCase("Approved")) c.setBackground(APPROVED_COLOR);
                //     else if (status.equalsIgnoreCase("Rejected")) c.setBackground(REJECTED_COLOR);
                // }

                if (isSelected) c.setBackground(SELECTION_COLOR);
                c.setForeground(FOREGROUND_LIGHT);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(darkCellRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        centerPanel.add(scroll, BorderLayout.CENTER);
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel (Buttons) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));

        JButton addBtn = createStyledButton("➕ Add User", SUCCESS_COLOR, e -> JOptionPane.showMessageDialog(frame, "Add User Form Here (Include Hospital selection)"));
        // JButton editBtn = createStyledButton("✎ Edit User", WARNING_COLOR, e -> JOptionPane.showMessageDialog(frame, "Edit User Form Here (Include Hospital selection)"));
        // JButton deleteBtn = createStyledButton("🗑️ Delete User", DANGER_COLOR, e -> JOptionPane.showMessageDialog(frame, "Delete User Confirmation Here"));
        // JButton editBtn = createStyledButton("✎ Edit Selected User", WARNING_COLOR, e -> {
        //     int selectedRow = table.getSelectedRow();
        //     if (selectedRow >= 0) {
        //         int userId = (int) table.getValueAt(selectedRow, 0);
        //         UserWithHospital u = userDAO.getUserById(userId);
        //         if (u != null) {
        //             String newName = JOptionPane.showInputDialog(frame, "Enter new name:", u.getRealName());
        //             if (newName != null && !newName.trim().isEmpty()) {
        //                 u.setRealName(newName);
        //                 if (userDAO.updateUsername(u)) {
        //                     JOptionPane.showMessageDialog(frame, "User updated successfully!");
        //                     refreshTable();
        //                 } else {
        //                     JOptionPane.showMessageDialog(frame, "Update failed!");
        //                 }
        //             }
        //         }
        //     } else {
        //         JOptionPane.showMessageDialog(frame, "Please select a user to edit.");
        //     }
        // });
        
        
    
    // --- Delete User ---
    JButton deleteBtn = createStyledButton("🗑️ Delete Selected User", DANGER_COLOR, e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure to delete user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (userDAO.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(frame, "User deleted successfully!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(frame, "Delete failed!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a user to delete.");
        }
    });
    
    
    
        bottomPanel.add(addBtn);
        //bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);

        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }


    // --- Styled Button Helper ---
    private JButton createStyledButton(String text, Color bgColor, ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setUI(new BasicButtonUI());
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Edit User ---
    JButton editBtn = createStyledButton("✎ Edit Selected User", WARNING_COLOR, e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) table.getValueAt(selectedRow, 0);
            UserWithHospital u = userDAO.getUserById(userId);
            if (u != null) {
                String newName = JOptionPane.showInputDialog(frame, "Enter new name:", u.getRealName());
                if (newName != null && !newName.trim().isEmpty()) {
                    u.setRealName(newName);
                    if (userDAO.updateUsername(u)) {
                        JOptionPane.showMessageDialog(frame, "User updated successfully!");
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Update failed!");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a user to edit.");
        }
    });
    
    

// --- Delete User ---
JButton deleteBtn = createStyledButton("🗑️ Delete Selected User", DANGER_COLOR, e -> {
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        int userId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure to delete user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDAO.deleteUser(userId)) {
                JOptionPane.showMessageDialog(frame, "User deleted successfully!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(frame, "Delete failed!");
            }
        }
    } else {
        JOptionPane.showMessageDialog(frame, "Please select a user to delete.");
    }
});



// --- Refresh Table Data ---
public void refreshTable() {
    tableModel.setRowCount(0);
    UserDAO userDAO = new UserDAO();
    List<UserWithHospital> users = userDAO.getAllUsersWithHospital();
    for (UserWithHospital u : users) {
        if (!"ADMIN".equalsIgnoreCase(u.getRole())) { 
        tableModel.addRow(new Object[]{
            u.getUserId(),
            u.getRealName(),
            u.getRole(),
            u.getHospitalName()
        });
    }
}


    }


    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color bg) {
            setText(text);
            setBackground(bg);
            setForeground(Color.WHITE);
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean clicked;
        private int row;
        private JTable table;
        private UserManagement parentUI;

        public ButtonEditor(JCheckBox checkBox, String label, Color bg, UserManagement ui) {
            super(checkBox);
            button = new JButton();
            button.setText(label);
            button.setBackground(bg);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            this.parentUI = ui;
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            clicked = true;
            return button;
        }

    //     @Override
    //     public Object getCellEditorValue() {
    //         if (clicked) {
    //             int userId = (int) table.getValueAt(row, 0);
    //             if (button.getText().equals("Edit")) {
    //                 parentUI.editUser(userId);
    //             } else if (button.getText().equals("Delete")) {
    //                 parentUI.deleteUser(userId);
    //             }
    //         }
    //         clicked = false;
    //         return button.getText();
    //     }

    //     @Override
    //     public boolean stopCellEditing() {
    //         clicked = false;
    //         return super.stopCellEditing();    
    //     }
    // }
    } 
    // Optional main for testing
    public static void main(String[] args) {
        JFrame dummyDashboard = new JFrame("Dashboard");
        dummyDashboard.setSize(400, 300);
        dummyDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dummyDashboard.setVisible(false);

        SwingUtilities.invokeLater(() -> new UserManagement(dummyDashboard));
    }
    
    }
