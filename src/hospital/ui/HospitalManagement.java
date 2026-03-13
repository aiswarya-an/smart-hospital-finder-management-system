package hospital.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import hospital.dao.HospitalAdminDAO;
import hospital.dao.HospitalDAO;
import hospital.models.Hospital;
import hospital.models.HospitalAdmin;

public class HospitalManagement {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

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
    private final Color TEXTFIELD_BG = new Color(58, 70, 80);

    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public HospitalManagement(JFrame dashboardFrame) {
        // --- Frame Setup ---
        frame = new JFrame("Hospital Management - Smart Hospital System");
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

        // --- Top Gradient Panel ---
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

        JButton backBtn = new JButton("← Back");
        backBtn.setFont(LABEL_FONT);
        backBtn.setBackground(new Color(22, 31, 38));
        backBtn.setForeground(FOREGROUND_LIGHT);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            dashboardFrame.setVisible(true); // Show dashboard again
            frame.dispose();
        });

        JLabel titleLabel = new JLabel("Hospital Management", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(FOREGROUND_LIGHT);

        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        // --- Center Table Panel ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.setBackground(DARK_BACKGROUND_PRIMARY);

        table = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Hospital ID", "Hospital Name", "Location", "Contact", "Specializations", "Actions"});
        table.setModel(tableModel);

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
                if (row % 2 == 0) c.setBackground(rowBg1); else c.setBackground(rowBg2);
                if (isSelected) c.setBackground(SELECTION_COLOR);
                c.setForeground(FOREGROUND_LIGHT);
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(darkCellRenderer);

        // // Sample data
        // tableModel.addRow(new Object[]{"H001", "City General Hospital", "New York, NY", "Dr. A. Smith", "Edit | Delete"});
        // tableModel.addRow(new Object[]{"H002", "Central Trauma Clinic", "Los Angeles, CA", "Dr. B. Jones", "Edit | Delete"});
        // tableModel.addRow(new Object[]{"H003", "St. Jude's Medical", "Chicago, IL", "Dr. C. Lee", "Edit | Delete"});

        loadHospitalData(); // Load real data from DB
        table.addMouseListener(new TableActionMouseListener());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        centerPanel.add(scroll, BorderLayout.CENTER);
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel (Buttons) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        bottomPanel.setBackground(DARK_BACKGROUND_PRIMARY);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));

        //ActionListener placeholderAction = e -> JOptionPane.showMessageDialog(frame, "Use the 'Edit | Delete' links in the table.");
        JButton addBtn = createStyledButton("➕ Add Hospital", SUCCESS_COLOR, e -> showAddHospitalDialog());
        JButton pendingBtn = createStyledButton("👤 Pending Admins", ACCENT_COLOR, e -> showPendingAdminsDialog());
        bottomPanel.add(pendingBtn);

        // JButton editBtn = createStyledButton("✎ Edit Hospital", WARNING_COLOR, placeholderAction);
        // JButton deleteBtn = createStyledButton("🗑️ Delete Hospital", DANGER_COLOR, placeholderAction);

        bottomPanel.add(addBtn);
        // bottomPanel.add(editBtn);
        // bottomPanel.add(deleteBtn);

        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadHospitalData() {
        tableModel.setRowCount(0); // clear existing
        HospitalDAO hospitalDAO = new HospitalDAO();
        List<Hospital> hospitals = hospitalDAO.getAllHospitals();
        for (Hospital h : hospitals) {
            tableModel.addRow(new Object[]{
                "H" + String.format("%03d", h.getId()),
                h.getName(),
                h.getAddress(),
                h.getContact(),
                h.getSpecialization(),
                "Edit | Delete"
            });
        }
    }

    // --- Table Click Handling ---
    private class TableActionMouseListener extends java.awt.event.MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());
            if (row >= 0 && col == 5) {
                int modelRow = table.convertRowIndexToModel(row);
                String actionCell = (String) tableModel.getValueAt(modelRow, col);
                int clickX = e.getX() - table.getCellRect(row, col, true).x;
                int cellWidth = table.getColumnModel().getColumn(col).getWidth();
                if (actionCell.contains("Edit") && clickX < cellWidth / 2) showEditHospitalDialog(modelRow);
                else if (actionCell.contains("Delete") && clickX >= cellWidth / 2) showDeleteConfirmationDialog(modelRow);
            }
        }
    }

    // --- Add Dialog ---
    private void showAddHospitalDialog() {
        JDialog dialog = new JDialog(frame, "Add New Hospital", true);
        dialog.setSize(450, 450);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);
        dialog.getContentPane().setBackground(DARK_BACKGROUND_PRIMARY);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        inputPanel.setBackground(DARK_BACKGROUND_PRIMARY);

        //JTextField idField = createTextField("H004");
        JTextField nameField = createTextField("e.g., Regional Care Center");
        JTextField locationField = createTextField("e.g., Boston, MA");
        JTextField contactField = createTextField("e.g., 9345671200");
        JTextField specializationsField = createTextField("e.g., cardiology,neurology..");

        // inputPanel.add(createLabel("Hospital ID:"));
        // inputPanel.add(idField);
        inputPanel.add(createLabel("Hospital Name:"));
        inputPanel.add(nameField);
        inputPanel.add(createLabel("Location:"));
        inputPanel.add(locationField);
        inputPanel.add(createLabel("Contact:"));
        inputPanel.add(contactField);
        inputPanel.add(createLabel("Specializations:"));
        inputPanel.add(specializationsField);
        dialog.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(DARK_BACKGROUND_PRIMARY);

        JButton saveBtn = createStyledButton("Save & Add", SUCCESS_COLOR, e -> {
            //String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String contact = contactField.getText().trim();
            String specializations = specializationsField.getText().trim();
            if ( name.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //tableModel.addRow(new Object[]{id, name, location, contact.isEmpty() ? "N/A" : doctor, "Edit | Delete"});
            tableModel.addRow(new Object[]{name, location, contact.isEmpty() ? "N/A" : "-",  "Edit | Delete"});
            Hospital hospital = new Hospital(0, name, location, contact, specializations); // ID auto-increment in DB
            HospitalDAO hospitalDAO = new HospitalDAO();
            if (hospitalDAO.addHospital(hospital)) {
                JOptionPane.showMessageDialog(dialog, "Hospital added successfully!");
                loadHospitalData(); // refresh table
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error adding hospital!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            dialog.dispose();
        });

        JButton cancelBtn = createStyledButton("Cancel", DARK_BACKGROUND_SECONDARY.darker(), e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showPendingAdminsDialog() {
        JDialog pendingFrame = new JDialog(frame, "Pending Hospital Admins", true);
        pendingFrame.setSize(900, 450);
        pendingFrame.setLayout(new BorderLayout());
        pendingFrame.setLocationRelativeTo(frame);
        pendingFrame.getContentPane().setBackground(DARK_BACKGROUND_PRIMARY);
    
        // --- Top Panel with Back Button ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JButton backBtn = createStyledButton("← Back", new Color(22, 31, 38), e -> pendingFrame.dispose());
        JLabel titleLabel = new JLabel("Pending Hospital Admins", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(FOREGROUND_LIGHT);
        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        pendingFrame.add(topPanel, BorderLayout.NORTH);
    
        // --- Table Panel ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.setOpaque(false);
    
        JTable pendingTable = createStyledTable();
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"User ID", "HospitalAdmin Name", "Hospital","Location", "Contact Doctor","",""});
        pendingTable.setModel(model);
    
        // // Load pending admins from DAO
        // // List<HospitalAdmin> pendingAdmins = HospitalAdminDAO.getPendingAdmins();
        // // for (HospitalAdmin admin : pendingAdmins) {
        //     HospitalAdminDAO hospitaladminDAO = new HospitalAdminDAO();
        //     Hospital h = hospitaladminDAO.getPendingAdmins();
        //     model.addRow(new Object[]{
        //         "U" + String.format("%03d", admin.getId()),
        //         h.getName(),
        //         h.getAddress(),
        //         admin.getName(),
        //         "Approve | Reject"
        //     });
        // //}

        HospitalAdminDAO hospitaladminDAO = new HospitalAdminDAO();// DAO to get hospital info
        HospitalDAO hospitalDAO = new HospitalDAO();
        List<HospitalAdmin> pendingAdmins = hospitaladminDAO.getPendingAdmins();
        
        for (HospitalAdmin admin : pendingAdmins) {
            Hospital h = hospitalDAO.getHospital(admin.getHospitalId());
            model.addRow(new Object[]{
                "U" + String.format("%03d", admin.getId()),
                h.getName(),
                h.getAddress(),
                admin.getName(),
                "Approve | Reject"
            });
        }

    
        JScrollPane scroll = new JScrollPane(pendingTable);
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        centerPanel.add(scroll, BorderLayout.CENTER);
        pendingFrame.add(centerPanel, BorderLayout.CENTER);
    
        // --- Approve / Reject Actions ---
        pendingTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = pendingTable.rowAtPoint(e.getPoint());
                int col = pendingTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) {
                    int clickX = e.getX() - pendingTable.getCellRect(row, col, true).x;
                    int cellWidth = pendingTable.getColumnModel().getColumn(col).getWidth();
                    HospitalAdmin admin = pendingAdmins.get(row);
                    HospitalDAO hospitalDAO = new HospitalDAO();
                    Hospital h = hospitalDAO.getHospital(admin.getHospitalId());
    
                    if (clickX < cellWidth / 2) { // Approve
                        if (HospitalAdminDAO.approveAdmin(admin.getId())) {
                            // Optionally, add hospital if needed
                            // tableModel = main hospital table model
                            tableModel.addRow(new Object[]{
                                "H" + String.format("%03d", h.getId()),
                                h.getName(),
                                h.getAddress(),
                                admin.getName(),
                                "Edit | Delete"
                            });
                            JOptionPane.showMessageDialog(pendingFrame, admin.getName() + " approved!");
                            model.removeRow(row);
                            pendingAdmins.remove(row); // keep list in sync
                        } else {
                            JOptionPane.showMessageDialog(pendingFrame, "Error approving admin.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else { // Reject
                        if (HospitalAdminDAO.deleteAdmin(admin.getId())) {
                            JOptionPane.showMessageDialog(pendingFrame, admin.getName() + " rejected!");
                            model.removeRow(row);
                            pendingAdmins.remove(row);
                        } else {
                            JOptionPane.showMessageDialog(pendingFrame, "Error rejecting admin.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    
        pendingFrame.setVisible(true);
    }
    
    
    // --- Helper function to create a styled table ---
    private JTable createStyledTable() {
        JTable table = new JTable();
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
    
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                c.setForeground(FOREGROUND_LIGHT);
                if(row % 2 == 0) c.setBackground(new Color(40,50,60,200));
                else c.setBackground(new Color(50,60,70,200));
                if(isSelected) c.setBackground(SELECTION_COLOR);
                return c;
            }
        };
        for(int i=0;i<table.getColumnCount();i++) table.getColumnModel().getColumn(i).setCellRenderer(renderer);
    
        return table;
    }
    
    
    

    private void showEditHospitalDialog(int row) {
        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String loc = (String) tableModel.getValueAt(row, 2);
        String cont = (String) tableModel.getValueAt(row, 3);
        String spl = (String) tableModel.getValueAt(row, 4);
        JDialog dialog = new JDialog(frame, "Edit Hospital", true);
        dialog.setSize(450, 600);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(frame);
        dialog.getContentPane().setBackground(DARK_BACKGROUND_PRIMARY);

        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBackground(DARK_BACKGROUND_PRIMARY);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JTextField nameField = createTextField(name);
        JTextField locField = createTextField(loc);
        JTextField contField = createTextField(cont);
        JTextField splField = createTextField(spl);

        panel.add(createLabel("Hospital ID:"));
        panel.add(new JLabel(id));
        panel.add(createLabel("Hospital Name:"));
        panel.add(nameField);
        panel.add(createLabel("Location:"));
        panel.add(locField);
        panel.add(createLabel("Contact:"));
        panel.add(contField);
        panel.add(createLabel("Specializations:"));
        panel.add(splField);


        dialog.add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(DARK_BACKGROUND_PRIMARY);

        // JButton save = createStyledButton("Update", WARNING_COLOR, e -> {
        //     tableModel.setValueAt(nameField.getText(), row, 1);
        //     tableModel.setValueAt(locField.getText(), row, 2);
        //     tableModel.setValueAt(contField.getText(), row, 3);
        //     tableModel.setValueAt(splField.getText(), row, 4);
        //     dialog.dispose();
        // });

        JButton save = createStyledButton("Update", WARNING_COLOR, e -> {
            String newName = nameField.getText();
            String newLoc = locField.getText();
            String newCont = contField.getText();
            String newSpl = splField.getText();
            
            int hospitalId = Integer.parseInt(id.substring(1));
            Hospital h = new Hospital(hospitalId, newName, newLoc, newCont, newSpl);
            HospitalDAO hospitalDAO = new HospitalDAO();
            if (hospitalDAO.updateHospital(h)) {
                JOptionPane.showMessageDialog(frame, "Hospital updated successfully!");
                loadHospitalData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating hospital.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancel = createStyledButton("Cancel", DARK_BACKGROUND_SECONDARY.darker(), e -> dialog.dispose());
        btnPanel.add(cancel);
        btnPanel.add(save);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // private void showDeleteConfirmationDialog(int row) {
    //     String id = (String) tableModel.getValueAt(row, 0);
    //     String name = (String) tableModel.getValueAt(row, 1);
    //     int confirm = JOptionPane.showConfirmDialog(frame,
    //             "Delete " + name + " (" + id + ")?", "Confirm Deletion",
    //             JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    //     if (confirm == JOptionPane.YES_OPTION) {
    //         int hospitalId = Integer.parseInt(id.substring(1));
    //         if(HospitalDAO.deleteHospital(hospitalId)) {
    //             tableModel.removeRow(row);
    //             JOptionPane.showMessageDialog(frame, "Hospital deleted successfully!");
    //         } else {
    //             JOptionPane.showMessageDialog(frame, "Error deleting hospital.");
    //         }
    //     }
    // }

    private void showDeleteConfirmationDialog(int row) {
        String id = (String) tableModel.getValueAt(row, 0);
        int hospitalId = Integer.parseInt(id.substring(1));
    
        int confirm = JOptionPane.showConfirmDialog(frame,
            "Delete hospital " + id + "?", "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            HospitalDAO hospitalDAO = new HospitalDAO();
            if (hospitalDAO.deleteHospital(hospitalId)) {
                tableModel.removeRow(row);
                JOptionPane.showMessageDialog(frame, "Hospital deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Error deleting hospital!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(FOREGROUND_LIGHT);
        return l;
    }

    private JTextField createTextField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(TEXTFIELD_BG);
        f.setForeground(FOREGROUND_LIGHT);
        f.setCaretColor(ACCENT_COLOR);
        f.setFont(LABEL_FONT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BACKGROUND_SECONDARY.darker(), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return f;
    }

    private JButton createStyledButton(String text, Color bgColor, ActionListener action) {
        JButton btn = new JButton(text);
        btn.addActionListener(action);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setUI(new BasicButtonUI());
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame dummy = new JFrame("Dashboard");
            dummy.setSize(400, 300);
            dummy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummy.setVisible(true);
            new HospitalManagement(dummy);
        });
    }
}
