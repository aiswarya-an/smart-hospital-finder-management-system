package hospital.ui;

import hospital.dao.AppointmentDAO;
import hospital.models.Appointment;
import hospital.models.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AppointmentHistory extends JFrame {
    private final Patient currentPatient;
    private final AppointmentDAO appointmentDAO;
    private JPanel cardsPanel;
    private JComboBox<String> filterCombo;
    private final PatientDashboard parent;

    public AppointmentHistory(Patient patient, PatientDashboard parentFrame) {
        this.currentPatient = patient;
        this.parent = parentFrame;
        this.appointmentDAO = new AppointmentDAO();

        setTitle("My Appointments - " + patient.getName());
        setSize(960, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Root panel with border =====
        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(new Color(245, 247, 250));
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 120, 120), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setContentPane(root);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("📋 My Appointments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.add(title, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        styleButton(backBtn, new Color(66, 133, 244), Color.WHITE);
        backBtn.addActionListener(e -> {
            dispose();
            if (parent != null) parent.setVisible(true);
        });
        header.add(backBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ===== Filter Panel =====
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Show:"));
        filterCombo = new JComboBox<>(new String[]{"All", "Upcoming", "Past"});
        filterPanel.add(filterCombo);
        JButton applyBtn = new JButton("Apply");
        styleButton(applyBtn, new Color(15, 157, 88), Color.WHITE);
        filterPanel.add(applyBtn);
        root.add(filterPanel, BorderLayout.CENTER);

        // ===== Cards Panel =====
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(root.getBackground());

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scrollPane, BorderLayout.CENTER);

        // ===== Load Data =====
        loadAppointments("All");

        // Filter action
        applyBtn.addActionListener(e -> loadAppointments(filterCombo.getSelectedItem().toString()));

        setVisible(true);
    }

    private void loadAppointments(String filter) {
        cardsPanel.removeAll();
        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatientId(currentPatient.getUserId(), filter);

        if (appointments == null || appointments.isEmpty()) {
            JLabel empty = new JLabel("No appointments found for the selected filter.");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            empty.setForeground(Color.GRAY);
            empty.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            cardsPanel.add(empty);
        } else {
            for (Appointment a : appointments) {
                cardsPanel.add(createAppointmentCard(a));
                cardsPanel.add(Box.createVerticalStrut(12));
            }
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createAppointmentCard(Appointment a) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setBackground(Color.WHITE);
    
        // ===== Info panel at top =====
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5)); // 2 rows, 2 columns, with gaps
        infoPanel.setOpaque(false);
    
        infoPanel.add(new JLabel("<html><b>" + a.getHospitalName() + "</b></html>"));
        infoPanel.add(new JLabel("Status: " + a.getStatus()));
    
        infoPanel.add(new JLabel("Doctor: " + a.getDoctorName()));
        infoPanel.add(new JLabel("Date: " + a.getDate() + "  " + a.getTime().toString().substring(0,5)));
    
        card.add(infoPanel, BorderLayout.NORTH);
    
        // ===== Buttons panel =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
    
        JButton viewBtn = new JButton("View");
        styleButton(viewBtn, new Color(66, 133, 244), Color.WHITE);
        viewBtn.addActionListener(e -> {
            // Default centered popup
            JOptionPane.showMessageDialog(card,
                    "<html>Appointment ID: " + a.getAppointmentId() +
                            "<br>Hospital: " + a.getHospitalName() +
                            "<br>Doctor: " + a.getDoctorName() +
                            "<br>Date: " + a.getDate() +
                            "<br>Time: " + a.getTime().toString().substring(0,5) +
                            "<br>Status: " + a.getStatus() + "</html>",
                    "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
        });
        btnPanel.add(viewBtn);
    
        if(a.getStatus().equalsIgnoreCase("Scheduled") || a.getStatus().equalsIgnoreCase("Confirmed") || a.getStatus().equalsIgnoreCase("Upcoming")) {
            JButton cancelBtn = new JButton("Cancel");
            styleButton(cancelBtn, new Color(220, 50, 50), Color.WHITE);
            cancelBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(card,
                        "Are you sure you want to cancel appointment ID " + a.getAppointmentId() + "?",
                        "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) {
                    boolean success = appointmentDAO.updateAppointmentStatus(a.getAppointmentId(), "Cancelled");
                    if(success) loadAppointments(filterCombo.getSelectedItem().toString());
                }
            });
            btnPanel.add(cancelBtn);
        }
    
        card.add(btnPanel, BorderLayout.CENTER);
    
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(245,245,250)); }
            public void mouseExited(MouseEvent e) { card.setBackground(Color.WHITE); }
        });
    
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
    
        return card;
    }
    

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }
}
