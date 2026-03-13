package hospital.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import hospital.dao.HospitalDAO;
import hospital.dao.DoctorDAO;
import hospital.models.Doctor;
import hospital.models.Hospital;

public class HospitalSearch extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private PatientDashboard parent;
    private HospitalDAO hospitalDAO;
    private DoctorDAO doctorDAO;
    private List<Hospital> hospitals;
    private JTextField locationField;
    private JTextField specialtyField;

    public HospitalSearch(PatientDashboard parentDashboard) {
        this.parent = parentDashboard;
        this.hospitalDAO = new HospitalDAO();
        this.doctorDAO = new DoctorDAO();

        setTitle("Search Hospitals");
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("🏥 Search Hospitals");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(40, 40, 60));
        header.add(title, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        styleButton(backBtn, new Color(66, 133, 244), Color.WHITE);
        backBtn.addActionListener(e -> {
            dispose();
            parent.setVisible(true);
        });
        header.add(backBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ===== Filter Panel =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        filterPanel.setOpaque(false);

        locationField = new JTextField(15);
        locationField.setToolTipText("Enter location");
        locationField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true));

        specialtyField = new JTextField(15);
        specialtyField.setToolTipText("Enter specialization");
        specialtyField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true));

        JButton searchBtn = new JButton("Search");
        styleButton(searchBtn, new Color(15, 157, 88), Color.WHITE);

        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationField);
        filterPanel.add(new JLabel("Specialization:"));
        filterPanel.add(specialtyField);
        filterPanel.add(searchBtn);
        root.add(filterPanel, BorderLayout.CENTER);

        // ===== Table Panel (Card style) =====
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columns = {"Hospital Name", "Address", "Contact", "Specializations", "Action"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) { return column == 4; }

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : new Color(240, 240, 245));
                } else {
                    c.setBackground(new Color(200, 220, 255));
                }
                return c;
            }
        };
        table.setRowHeight(38);
        table.setFont(new Font("SansSerif", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(66, 133, 244));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableCard.add(scroll, BorderLayout.CENTER);

        root.add(tableCard, BorderLayout.SOUTH);

        // ===== Load Data =====
        loadHospitalData(null, null);

        // ===== Button Renderer & Editor =====
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        // ===== Search button action =====
        searchBtn.addActionListener(e -> {
            String location = locationField.getText().trim();
            String specialty = specialtyField.getText().trim();
            loadHospitalData(location.isEmpty() ? null : location, specialty.isEmpty() ? null : specialty);
        });

        setVisible(true);
    }

    private void loadHospitalData(String location, String specialization) {
        hospitals = (location == null && specialization == null)
                ? hospitalDAO.getAllHospitals()
                : hospitalDAO.getHospitalsByFilter(location, specialization);

        model.setRowCount(0);

        if (hospitals != null && !hospitals.isEmpty()) {
            for (Hospital h : hospitals) {
                model.addRow(new Object[]{
                        h.getName(),
                        h.getAddress(),
                        h.getContact(),
                        h.getSpecialization(),
                        "View Doctors"
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hospitals found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===== Renderer =====
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFocusPainted(false);
            setBackground(new Color(66, 133, 244));
            setForeground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // ===== Editor =====
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(66, 133, 244));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int row = table.getSelectedRow();
                if (row >= 0 && row < hospitals.size()) {
                    Hospital selectedHospital = hospitals.get(row);
                    List<Doctor> doctors = doctorDAO.getDoctorsByHospital(selectedHospital.getId());

                    if (doctors == null || doctors.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "No doctors registered in this hospital.",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JPopupMenu doctorMenu = new JPopupMenu();
                        for (Doctor d : doctors) {
                            JMenuItem item = new JMenuItem(d.getName() + " (" + d.getSpecialization() + ")");
                            item.addActionListener(ev -> {
                                new AppointmentBooking(d, selectedHospital);
                                HospitalSearch.this.dispose();
                            });
                            doctorMenu.add(item);
                        }
                        doctorMenu.show(button, button.getWidth() / 2, button.getHeight());
                    }
                }
            }
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    // ===== Button styling =====
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }
}
