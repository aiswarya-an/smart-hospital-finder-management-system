package hospital.ui;

import javax.swing.*;
import hospital.dao.PatientDAO;
import hospital.models.Patient;
import java.awt.*;
import java.awt.event.*;

public class ProfileEdit extends JFrame {

    private Patient currentPatient;
    private PatientDAO patientDAO;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;

    public ProfileEdit(Patient patient) {
        if (patient == null) {
            JOptionPane.showMessageDialog(null, "Error: Patient profile not loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        this.currentPatient = patient;
        this.patientDAO = new PatientDAO();

        setTitle("Edit Profile: " + currentPatient.getName());
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ===== Root Panel =====
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("✏️ Edit Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(40, 40, 60));
        header.add(title, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(66, 133, 244));
        backBtn.setForeground(Color.WHITE);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        backBtn.addActionListener(e -> {
            dispose();
            new PatientDashboard(currentPatient);
        });
        header.add(backBtn, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // ===== Card-style Form Panel =====
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(70, 70, 70));
        card.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        nameField = new JTextField(20);
        nameField.setText(currentPatient.getName());
        nameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        card.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(70, 70, 70));
        card.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        emailField = new JTextField(20);
        emailField.setText(currentPatient.getEmail());
        emailField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        card.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        phoneLabel.setForeground(new Color(70, 70, 70));
        card.add(phoneLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        phoneField = new JTextField(20);
        phoneField.setText(currentPatient.getContact());
        phoneField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        card.add(phoneField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        addressLabel.setForeground(new Color(70, 70, 70));
        card.add(addressLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        addressArea = new JTextArea(4, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setText(currentPatient.getAddress());
        addressArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        JScrollPane scroll = new JScrollPane(addressArea);
        card.add(scroll, gbc);

        root.add(card, BorderLayout.CENTER);

        // ===== Save Button =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(15, 157, 88));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) { saveBtn.setBackground(new Color(10, 120, 70)); }
            public void mouseExited(MouseEvent e) { saveBtn.setBackground(new Color(15, 157, 88)); }
        });
        saveBtn.addActionListener(e -> saveProfileChanges());

        bottomPanel.add(saveBtn);
        root.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveProfileChanges() {
        if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentPatient.setName(nameField.getText().trim());
        currentPatient.setEmail(emailField.getText().trim());
        currentPatient.setContact(phoneField.getText().trim());
        currentPatient.setAddress(addressArea.getText().trim());

        boolean success = patientDAO.updatePatientProfile(currentPatient);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Profile Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new PatientDashboard(currentPatient);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to update profile in database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
