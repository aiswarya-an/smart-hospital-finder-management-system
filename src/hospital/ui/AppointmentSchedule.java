package hospital.ui;

import javax.swing.*;
import javax.swing.table.*;

import hospital.models.Doctor;

import java.awt.*;

public class AppointmentSchedule extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private Doctor doctor;

    public AppointmentSchedule(Doctor doctor) {

        this.doctor = doctor;
    
        setTitle("Appointment Schedule - " + doctor.getName());
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(34, 34, 40));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 34, 40));

        JLabel title = new JLabel("📅  Appointment Schedule - " + doctor.getName());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(new Color(50, 50, 70));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());
        header.add(backBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ===== Table =====
        String[] columns = {"ID", "Patient Name", "Date", "Time", "Status", "Actions"};
        // Object[][] data = {
        //         {"A001", "John Doe", "11/10/2025", "10:00 AM", "Cancelled", ""},
        //         {"A002", "Alice Smith", "11/10/2025", "10:30 AM", "Completed", ""},
        //         {"A003", "Bob Johnson", "11/10/2025", "11:00 AM", "Approved", ""},
        // };

        Object[][] data = fetchAppointmentsFromDAO();
        

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Actions column editable
            }
        };

        table = new JTable(model) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 4) { // Status column
                    return new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                                                                       boolean isSelected, boolean hasFocus,
                                                                       int row, int column) {
                            JLabel lbl = new JLabel(value.toString(), SwingConstants.CENTER);
                            lbl.setOpaque(true);
                            switch (value.toString()) {
                                //case "Pending" -> lbl.setBackground(new Color(255, 165, 0));
                                case "Approved" -> lbl.setBackground(new Color(30, 144, 255));
                                case "Completed" -> lbl.setBackground(new Color(50, 205, 50));
                                case "Cancelled" -> lbl.setBackground(new Color(220, 20, 60));
                                default -> lbl.setBackground(Color.GRAY);
                            }
                            lbl.setForeground(Color.WHITE);
                            lbl.setBorder(BorderFactory.createEmptyBorder(3,5,3,5));
                            return lbl;
                        }
                    };
                }
                return super.getCellRenderer(row, column);
            }
        };

        table.setRowHeight(35);
        table.setBackground(new Color(50, 50, 70));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(70, 70, 100));
        table.getTableHeader().setBackground(new Color(30, 30, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);   // ID
        columnModel.getColumn(1).setPreferredWidth(150);  // Patient Name
        columnModel.getColumn(2).setPreferredWidth(90);   // Date
        columnModel.getColumn(3).setPreferredWidth(80);   // Time
        columnModel.getColumn(4).setPreferredWidth(100);  // Status
        columnModel.getColumn(5).setPreferredWidth(300);  // Actions

        // Actions buttons
        TableColumn actionsColumn = table.getColumn("Actions");
        actionsColumn.setCellRenderer(new ActionRenderer());
        actionsColumn.setCellEditor(new ActionEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        root.add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    private Object[][] fetchAppointmentsFromDAO() {
        hospital.dao.AppointmentDAO dao = new hospital.dao.AppointmentDAO();
        java.util.List<hospital.models.Appointment> list = dao.getAppointmentsByDoctorId(doctor.getUserId());
    
        Object[][] data = new Object[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            var a = list.get(i);
            data[i][0] = a.getAppointmentId();
            data[i][1] = a.getPatientName();
            data[i][2] = a.getDate().toString();
            data[i][3] = a.getTime().toString();
            data[i][4] = a.getStatus();
            data[i][5] = "";
        }
        return data;
    }
    
    // Renderer for Actions column
    class ActionRenderer extends JPanel implements TableCellRenderer {
        public ActionRenderer() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            removeAll();
            add(createButton("View Details", row));
            add(createButton("Mark Completed", row));
            add(createButton("Cancel", row));
            return this;
        }
    }

    // Editor for Actions column
    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();
            panel.add(createButton("View Details", row));
            panel.add(createButton("Mark Completed", row));
            panel.add(createButton("Cancel", row));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private JButton createButton(String text, int row) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);

        switch (text) {
            case "View Details" -> btn.setBackground(new Color(30, 30, 60));
            case "Mark Completed" -> btn.setBackground(new Color(50, 50, 90));
            case "Cancel" -> btn.setBackground(new Color(90, 30, 30));
        }

        btn.setMargin(new Insets(2,5,2,5)); // small padding for better spacing

        btn.addActionListener(e -> {
            switch (text) {
                case "View Details" -> JOptionPane.showMessageDialog(this,
                        "Patient Details: " + table.getValueAt(row, 1));
                        case "Mark Completed" -> {
                            updateAppointmentStatusInDB(row, "Completed");
                        }
                        case "Cancel" -> {
                            updateAppointmentStatusInDB(row, "Cancelled");
                        }
                        }
        });

        return btn;
    }

    private void updateAppointmentStatusInDB(int row, String newStatus) {
        int appointmentId = Integer.parseInt(table.getValueAt(row, 0).toString());
        hospital.dao.AppointmentDAO dao = new hospital.dao.AppointmentDAO();
        boolean success = dao.updateAppointmentStatus(appointmentId, newStatus);
        if (success) {
            model.setValueAt(newStatus, row, 4);
            JOptionPane.showMessageDialog(this, "Appointment marked as " + newStatus);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update appointment.");
        }
    }
    

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(AppointmentSchedule::new);
    // }
}
