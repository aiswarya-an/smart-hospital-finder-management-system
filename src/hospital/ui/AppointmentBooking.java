package hospital.ui;

import javax.swing.*;
import hospital.dao.AppointmentDAO;
import hospital.dao.DoctorDAO;
import hospital.models.Appointment;
import hospital.models.Doctor;
import hospital.models.Hospital;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dark-themed Appointment Booking UI connected to DAO.
 * Works seamlessly with the popup in HospitalSearch.
 */
public class AppointmentBooking extends JFrame {

    private final int patientUserId;
    private final Doctor doctor;
    private final Hospital hospital;
    private final AppointmentDAO appointmentDAO;

    public AppointmentBooking(Doctor doctor, Hospital hospital) {
        this.patientUserId = 1; // TODO: Replace with actual logged-in patient ID (from session)
        this.doctor = doctor;
        this.hospital = hospital;
        this.appointmentDAO = new AppointmentDAO();

        setTitle("Book Appointment | " + doctor.getName());
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // ====== DARK THEME COLORS ======
        Color bg = new Color(25, 25, 30);
        Color card = new Color(40, 40, 48);
        Color accent = new Color(66, 133, 244);
        Color success = new Color(34, 139, 84);
        Color text = new Color(230, 230, 230);
        Color faded = new Color(170, 170, 180);

        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(bg);
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        setContentPane(root);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("📅 Book Appointment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(accent);
        header.add(title, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        styleButton(backBtn, accent, text);
        backBtn.addActionListener(e -> {
            dispose();
            // this.setVisible(true);
        });
        
        header.add(backBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ===== Form Card =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(card);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Doctor Name
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(label("Doctor:", text), gbc);
        gbc.gridx = 1;
        formPanel.add(label(doctor.getName(), accent), gbc);
        row++;

        // Hospital Name
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(label("Hospital:", text), gbc);
        gbc.gridx = 1;
        formPanel.add(label(hospital.getName(), faded), gbc);
        row++;

        // Appointment Date
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(label("Appointment Date (YYYY-MM-DD):", text), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField(15);
        dateField.setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        styleField(dateField, bg, text);
        formPanel.add(dateField, gbc);
        row++;

        // Time Slot
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(label("Time Slot:", text), gbc);
        gbc.gridx = 1;
        JComboBox<String> timeSlots = new JComboBox<>(new String[]{"09:00", "10:00", "11:00", "13:00", "14:00"});
        styleCombo(timeSlots, bg, text);
        formPanel.add(timeSlots, gbc);
        row++;

        root.add(formPanel, BorderLayout.CENTER);
        // ===== Confirm Button =====
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton confirmBtn = new JButton("Confirm Appointment");
        styleButton(confirmBtn, success, Color.WHITE);
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        confirmBtn.addActionListener(e -> attemptBooking(dateField.getText(), (String) timeSlots.getSelectedItem()));
        bottom.add(confirmBtn);

        root.add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void attemptBooking(String dateStr, String timeStr) {
        Date sqlDate;
        Time sqlTime;

        try {
            LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime localTime = LocalTime.parse(timeStr);
            if (localDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "❌ Date cannot be in the past.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            sqlDate = Date.valueOf(localDate);
            sqlTime = Time.valueOf(localTime);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Invalid date/time format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Appointment appt = new Appointment();
        appt.setPatientUserId(patientUserId);
        appt.setDoctorUserId(doctor.getDoctorId());
        appt.setDate(sqlDate);
        appt.setTime(sqlTime);
        appt.setStatus("Scheduled");

        boolean success = appointmentDAO.bookAppointment(appt);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "<html><b>✅ Appointment Confirmed!</b><br>Doctor: " + doctor.getName() +
                            "<br>Date: " + dateStr +
                            "<br>Time: " + timeStr + "</html>",
                    "Booking Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "❌ Failed to book appointment. Please check DAO or connection.",
                    "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== Helper methods for styling =====
    private JLabel label(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        l.setForeground(color);
        return l;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
    }

    private void styleField(JTextField field, Color bg, Color fg) {
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(fg);
        field.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1));
    }

    private void styleCombo(JComboBox<String> combo, Color bg, Color fg) {
        combo.setBackground(bg);
        combo.setForeground(fg);
        combo.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1));
    }

    public static void main(String[] args) {
        // Mock demo run
        Doctor mockDoctor = new Doctor();
        mockDoctor.setDoctorId(2);
        mockDoctor.setName("Dr. Emily Smith");

        Hospital mockHospital = new Hospital();
        mockHospital.setId(1);
        mockHospital.setName("CityCare Hospital");

        SwingUtilities.invokeLater(() -> new AppointmentBooking(mockDoctor, mockHospital));
    }
}
