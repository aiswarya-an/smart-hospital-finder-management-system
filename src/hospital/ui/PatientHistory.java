package hospital.ui;

import javax.swing.*;
import javax.swing.table.*;

import hospital.dao.AppointmentDAO;
import hospital.models.Doctor;
import hospital.models.Patient;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PatientHistory extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private Doctor doctorId;


    public PatientHistory(Doctor currentDoctor) {
        this.doctorId = currentDoctor;
        setTitle("Patient History - Dr. Nisha");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.setBackground(new Color(34, 34, 40));
        setContentPane(root);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 34, 40));

        JLabel title = new JLabel("👨‍⚕️  Patient History - Dr. Nisha");
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
        String[] columns = {"Patient ID", "Name", "Last Visit", "Total Visits"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setBackground(new Color(50, 50, 70));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(70, 70, 100));
        table.getTableHeader().setBackground(new Color(30, 30, 50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loadPatientHistory();
        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        root.add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadPatientHistory() {
    model.setRowCount(0);
    List<Patient> patients = AppointmentDAO.getPatientHistoryByDoctor(doctorId);
    for (Patient p : patients) {
        model.addRow(new Object[]{
                p.getUserId(),
                p.getName(),
                p.getLastAppointmentDate(),
                p.getTotalVisits(),
        });
    }
    System.out.println("Doctor ID: " + doctorId.getDoctorId());
    System.out.println("Patients fetched: " + patients.size());

}

}
