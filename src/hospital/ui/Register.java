package hospital.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.JTextComponent;
import java.util.Calendar;

import hospital.dao.UserDAO;
import hospital.dao.PatientDAO;
import hospital.models.User;
import hospital.models.Patient;

public class Register {
    private JFrame frame;
    private JPanel formPanel;

    public Register() {
        frame = new JFrame("Patient Registration - Smart Hospital System");
        frame.setSize(520, 680);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(25, 35, 25, 35));
        formPanel.setBackground(new Color(34, 45, 65));

        addTitle();

        JTextField fullNameField = createTextField("Full Name");
        JTextField usernameField = createTextField("Username");
        JTextField emailField = createTextField("Email");
        JPasswordField passwordField = createPasswordField("Password");
        JPasswordField confirmPasswordField = createPasswordField("Confirm Password");

        addDOBPicker();

        // Gender
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(genderLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JPanel genderPanel = new JPanel();
        genderPanel.setOpaque(false);
        JRadioButton male = new JRadioButton("Male");
        JRadioButton female = new JRadioButton("Female");
        male.setForeground(Color.WHITE);
        female.setForeground(Color.WHITE);
        male.setBackground(new Color(34, 45, 65));
        female.setBackground(new Color(34, 45, 65));
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        genderPanel.add(male);
        genderPanel.add(female);
        formPanel.add(genderPanel);
        formPanel.add(Box.createVerticalStrut(10));

        JTextField contactField = createTextField("Contact Number");
        JTextArea addressArea = createTextArea("Address");

        formPanel.add(addressArea);
        formPanel.add(Box.createVerticalStrut(15));

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton registerBtn = createButton("Register", new Color(0, 153, 0), new Color(0, 102, 0));
        JButton resetBtn = createButton("Reset", new Color(204, 0, 0), new Color(153, 0, 0));
        JButton backBtn = createButton("Back to Login", new Color(0, 102, 204), new Color(0, 51, 153));
        buttonPanel.add(registerBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel);

        // Button Actions
        registerBtn.addActionListener(e -> {
            String fullName = fullNameField.getText();
            String username = usernameField.getText();
            String email = emailField.getText();
            String pass = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());
            String dob = getSelectedDate();
            String gender = male.isSelected() ? "Male" : female.isSelected() ? "Female" : "";
            String contact = contactField.getText();
            String address = addressArea.getText();

            // Check for empty fields
            if (fullName.isEmpty() || fullName.equals("Full Name") ||
                username.isEmpty() || username.equals("Username") ||
                email.isEmpty() || email.equals("Email") ||
                pass.isEmpty() || pass.equals("Password") ||
                confirmPass.isEmpty() || confirmPass.equals("Confirm Password") ||
                gender.isEmpty() || contact.isEmpty() || contact.equals("Contact Number") ||
                address.isEmpty() || address.equals("Address") ||
                dob.equals("Select Date")) {
                JOptionPane.showMessageDialog(frame, "⚠ Please fill all required fields!");
                return;
            }

            // Password match check
            if (!pass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(frame, "❌ Passwords do not match!");
                return;
            }

            // Contact number check
            if (!contact.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(frame, "⚠ Contact number must be 10 digits!");
                return;
            }

            // Email validation using regex
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(frame, "⚠ Enter a valid email address!");
                return;
            }

            try {
                // Step 1: Create User object
                User user = new User();
                user.setUsername(username);
                user.setPassword(pass);
                user.setRole("PATIENT");
                user.setContact(contact);
            
                // Step 2: Save user in DB
                UserDAO userDAO = new UserDAO();
                int userId = userDAO.registerUser(user);
            
                if (userId > 0) {
                    // Step 3: Create Patient profile
                    Patient patient = new Patient();
                    patient.setUserId(userId);
                    patient.setName(fullName);
                    patient.setAge(2025 - Integer.parseInt(yearBox.getSelectedItem().toString())); // quick age calc
                    patient.setGender(gender);
                    patient.setContact(contact);
                    patient.setEmail(email);
                    patient.setAddress(address);
                    patient.setDob(java.sql.Date.valueOf(
                        String.format("%s-%02d-%02d",
                            yearBox.getSelectedItem(),
                            monthBox.getSelectedIndex(),
                            dayBox.getSelectedIndex())
                    ));
            
                    // Step 4: Save patient in DB
                    PatientDAO patientDAO = new PatientDAO();
                    boolean added = patientDAO.addPatient(patient);
            
                    if (added) {
                        JOptionPane.showMessageDialog(frame, "✅ Registration Successful!");
                        frame.dispose();
                        new Login();
                    } else {
                        JOptionPane.showMessageDialog(frame, "❌ Failed to register patient details!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ Failed to create user account!");
                }
            
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "⚠ Error: " + ex.getMessage());
            }
            

            // JOptionPane.showMessageDialog(frame, "✅ Registration Successful!");
            // frame.dispose();
            // new Login();
        });

        resetBtn.addActionListener(e -> {
            fullNameField.setText("Full Name");
            fullNameField.setForeground(Color.WHITE);
            usernameField.setText("Username");
            usernameField.setForeground(Color.WHITE);
            emailField.setText("Email");
            emailField.setForeground(Color.WHITE);
            passwordField.setText("Password");
            passwordField.setForeground(Color.WHITE);
            passwordField.setEchoChar((char)0);
            confirmPasswordField.setText("Confirm Password");
            confirmPasswordField.setForeground(Color.WHITE);
            confirmPasswordField.setEchoChar((char)0);
            resetDOB();
            genderGroup.clearSelection();
            contactField.setText("Contact Number");
            contactField.setForeground(Color.WHITE);
            addressArea.setText("Address");
            addressArea.setForeground(Color.WHITE);
        });

        backBtn.addActionListener(_ -> {
            frame.dispose();
            new Login();
        });

        frame.add(formPanel);
        frame.setVisible(true);
    }

    private void addTitle() {
        JLabel title = new JLabel("Patient Registration");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
    }

    // Modern placeholder for JTextField
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(400, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(50, 60, 80));
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setupPlaceholder(field, placeholder, false);
        formPanel.add(field);
        formPanel.add(Box.createVerticalStrut(10));
        return field;
    }

    // Modern placeholder for JPasswordField
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(400, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(50, 60, 80));
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setupPlaceholder(field, placeholder, true);
        formPanel.add(field);
        formPanel.add(Box.createVerticalStrut(10));
        return field;
    }

    // Modern placeholder for JTextArea
    private JTextArea createTextArea(String placeholder) {
        JTextArea area = new JTextArea();
        area.setMaximumSize(new Dimension(400, 60));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setForeground(Color.WHITE);
        area.setBackground(new Color(50, 60, 80));
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setupPlaceholder(area, placeholder, false);
        return area;
    }

    // Universal placeholder setup
    private void setupPlaceholder(JTextComponent comp, String placeholder, boolean isPassword) {
        comp.setText(placeholder);
        comp.setForeground(Color.WHITE);
        if (isPassword) ((JPasswordField) comp).setEchoChar((char)0); // show placeholder

        comp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (comp.getText().equals(placeholder)) {
                    comp.setForeground(Color.GRAY);
                    if (isPassword) ((JPasswordField) comp).setEchoChar((char)0);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (comp.getText().isEmpty()) {
                    comp.setText(placeholder);
                    comp.setForeground(Color.WHITE);
                    if (isPassword) ((JPasswordField) comp).setEchoChar((char)0);
                }
            }
        });

        comp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (comp.getForeground() == Color.GRAY) {
                    comp.setText("");
                    comp.setForeground(Color.WHITE);
                    if (isPassword) ((JPasswordField) comp).setEchoChar('*');
                }
            }
        });
    }

    private JButton createButton(String text, Color normal, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(normal);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new LineBorder(Color.BLACK, 1, true));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(130, 35));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(normal); }
        });
        return btn;
    }

    private JComboBox<String> dayBox, monthBox, yearBox;

    private void addDOBPicker() {
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setForeground(Color.WHITE);
        dobLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(dobLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JPanel dobPanel = new JPanel();
        dobPanel.setOpaque(false);

        String[] days = new String[32];
        days[0] = "Day";
        for (int i = 1; i <= 31; i++) days[i] = String.valueOf(i);

        String[] months = {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[101];
        years[0] = "Year";
        for (int i = 1; i < years.length; i++) years[i] = String.valueOf(currentYear - i + 1);

        dayBox = new JComboBox<>(days);
        monthBox = new JComboBox<>(months);
        yearBox = new JComboBox<>(years);

        styleCombo(dayBox);
        styleCombo(monthBox);
        styleCombo(yearBox);

        dobPanel.add(dayBox);
        dobPanel.add(monthBox);
        dobPanel.add(yearBox);
        formPanel.add(dobPanel);
        formPanel.add(Box.createVerticalStrut(15));
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(new Color(50, 60, 80));
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBorder(new LineBorder(new Color(100, 120, 140)));
    }

    private String getSelectedDate() {
        if (dayBox.getSelectedIndex() == 0 || monthBox.getSelectedIndex() == 0 || yearBox.getSelectedIndex() == 0)
            return "Select Date";
        return dayBox.getSelectedItem() + " " + monthBox.getSelectedItem() + " " + yearBox.getSelectedItem();
    }

    private void resetDOB() {
        dayBox.setSelectedIndex(0);
        monthBox.setSelectedIndex(0);
        yearBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Register::new);
    }
}
