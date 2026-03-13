package hospital.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hospital.dao.AppointmentDAO;
import hospital.models.Appointment;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AppointmentManagement extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JFrame parent;

    public AppointmentManagement(JFrame parentFrame) {
        this.parent = parentFrame;
        setTitle("Manage Appointments");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        parent.setVisible(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        setContentPane(mainPanel);

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton backButton = new JButton("⬅ Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(220, 53, 69));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(new EmptyBorder(8, 18, 8, 18));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            parent.setVisible(true);
        });

        JLabel title = new JLabel("Appointments", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setOpaque(true);
        title.setBackground(new Color(70, 130, 180));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(15, 0, 15, 0));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== Table Setup =====
        String[] cols = {"ID", "Patient Name", "Date", "Time", "Status", "Actions"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the Actions column
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(55);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(220, 230, 240));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setIntercellSpacing(new Dimension(0, 0));

        // // Add data
        // addRow("APT001", "John Mathew", "Dr. Nisha", "11 Oct 2025", "10:30 AM", "Pending");
        // addRow("APT002", "Meera S", "Dr. Raj", "12 Oct 2025", "2:00 PM", "Pending");
        // addRow("APT003", "Amit K", "Dr. Renu", "12 Oct 2025", "3:00 PM", "Approved");

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAllAppointments(); // or by doctor/admin
        for(Appointment a : appointments){
            tableModel.addRow(new Object[]{
                a.getAppointmentId(),
                //a.getPatientUserId(),
                //a.getDoctorUserId(),
                a.getPatientName(),
                a.getDate(),
                a.getTime(),
                a.getStatus(),
                ""
            });
        }

        // Renderers + Editors
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
table.getColumnModel().getColumn(5).setPreferredWidth(220); // Actions column is now index 5

        // Adjust column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);  // ID smaller
        columnModel.getColumn(5).setPreferredWidth(220); // Actions wider

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addRow(String id, String patient, String doctor, String date, String time, String status) {
        tableModel.addRow(new Object[]{id, patient, doctor, date, time, status, ""});
    }


    private JButton createStyledButton(String text, Color base, Color hover) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(base);
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(base);
            }
        });
        return btn;
    }

    // Renderer to display both buttons visibly
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton approveBtn, cancelBtn;
        
            public ButtonRenderer() {
                setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
                setOpaque(true);
        
                approveBtn = createStyledButton("Approve", new Color(40, 167, 69), new Color(0, 140, 60));
                cancelBtn = createStyledButton("Cancel", new Color(220, 53, 69), new Color(180, 20, 30));
        
                add(approveBtn);
                add(cancelBtn);
            }
        
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                setBackground(isSelected ? new Color(220, 235, 255) : Color.WHITE);
                return this;
            }
        
    }
        

    // Editor for interactive buttons
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton approveBtn, cancelBtn;
    
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            panel.setBackground(Color.WHITE);
    
            approveBtn = createStyledButton("Approve", new Color(40, 167, 69), new Color(0, 140, 60));
            cancelBtn = createStyledButton("Cancel", new Color(220, 53, 69), new Color(180, 20, 30));
    
            panel.add(approveBtn);
            panel.add(cancelBtn);
    
            approveBtn.addActionListener(e -> handleAction("Confirmed"));
            cancelBtn.addActionListener(e -> handleAction("Cancelled"));
        }
    
        private void handleAction(String status) {
            int viewRow = table.getSelectedRow();
            if(viewRow >= 0){
                int modelRow = table.convertRowIndexToModel(viewRow);
                int appointmentId = (Integer) table.getValueAt(modelRow, 0);
                AppointmentDAO dao = new AppointmentDAO();
                boolean updated = dao.updateAppointmentStatus(appointmentId, status);
                if(updated){
                    table.setValueAt(status, modelRow, 4); // Status column index
                    String patient = (String) table.getValueAt(modelRow, 1);
                    JOptionPane.showMessageDialog(null, "Appointment " + status.toLowerCase() + " for " + patient);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update appointment.");
                }
            }
            fireEditingStopped();
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            return panel;
        }
    
        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(()-> new AppointmentManagement(new JFrame()));
        
    }
    
}
