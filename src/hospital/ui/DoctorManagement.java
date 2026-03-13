package hospital.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import hospital.dao.DoctorDAO;
import hospital.dao.UserDAO;
import hospital.models.Doctor;
import hospital.models.User;

public class DoctorManagement extends JFrame {

    private JPanel centerPanel;
    private DoctorDAO doctorDao = new DoctorDAO();
    private UserDAO userDao = new UserDAO();
    private JFrame parent;

    public DoctorManagement(JFrame parentFrame) {
        this.parent = parentFrame;          // store parent reference
        if(parent != null) parent.setVisible(false);  // hide parent

        setTitle("Manage Doctors");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Outer panel with padding/border
        // Outer dark panel with padding
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(34, 34, 50)); // dark background
        outer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // padding from edges

        // Inner panel with double-line border
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(44, 44, 60)); // slightly lighter dark
        root.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(20, 20, 30), 2),   // inner dark line
            BorderFactory.createLineBorder(new Color(70, 70, 90), 4)    // outer dark line
        ));

        outer.add(root, BorderLayout.CENTER);
        setContentPane(outer);


                // Top panel with heading and Add Doctor button
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new EmptyBorder(12, 12, 12, 12));
        top.setBackground(new Color(44, 44, 60));

        // Heading
        JLabel heading = new JLabel("Manage Doctors");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(Color.WHITE);
        heading.setBorder(new EmptyBorder(0, 10, 0, 0));
        top.add(heading, BorderLayout.WEST);

        // Add Doctor button
        JButton addBtn = new JButton("Add Doctor");
        addBtn.addActionListener(e -> openDoctorDialog(null, true));
        top.add(addBtn, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);


        // Center panel for cards
        centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 12));
        centerPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        root.add(scroll, BorderLayout.CENTER);

        refreshDoctors();

        // Restore parent frame when closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(parent != null) parent.setVisible(true);
            }
        });

        setVisible(true);
    }

    private void refreshDoctors() {
        centerPanel.removeAll();
        List<Doctor> doctors = doctorDao.getAllDoctors();
        for (Doctor d : doctors) {
            centerPanel.add(doctorCard(d));
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPanel doctorCard(Doctor d) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(30,30,50), 2),       // inner dark line
        BorderFactory.createLineBorder(new Color(70,70,90), 3)        // outer dark line
        ));
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));


        JLabel photo = new JLabel("👨‍⚕️", SwingConstants.CENTER);
        photo.setFont(new Font("SansSerif", Font.PLAIN, 40));
        photo.setBorder(new EmptyBorder(8, 8, 8, 8));
        card.add(photo, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setOpaque(false);
        info.add(new JLabel(d.getName(), SwingConstants.CENTER));
        info.add(new JLabel(d.getSpecialization(), SwingConstants.CENTER));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        btnRow.setOpaque(false);
        JButton view = new JButton("View");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");

        view.addActionListener(e -> openDoctorDialog(d, true));
        edit.addActionListener(e -> openDoctorDialog(d, false));
        del.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Delete " + d.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                doctorDao.deleteDoctor(d.getDoctorId());
                refreshDoctors();
            }
        });

        btnRow.add(view); btnRow.add(edit); btnRow.add(del);
        info.add(btnRow);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                card.setBackground(new Color(230, 240, 255)); // light hover
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50,100,200), 2),
                    BorderFactory.createLineBorder(new Color(90,90,120), 3)
                ));
            }
            public void mouseExited(MouseEvent e){
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(30,30,50), 2),
                    BorderFactory.createLineBorder(new Color(70,70,90), 3)
                ));
            }
        });
        
        
        

        return card;
    }

    private void openDoctorDialog(Doctor doctor, boolean isAddMode) {
        boolean readOnly = !isAddMode && doctor != null && !isAddMode;
        JDialog d = new JDialog(this, doctor == null ? "Add Doctor" : (readOnly ? "View Doctor" : "Edit Doctor"), true);
        d.setSize(500, 600);
        d.setResizable(false);
        d.setLocationRelativeTo(this);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(40,20,40,20));
        panel.setBackground(new Color(30,30,70));
    
        // Fields
        JTextField nameField = createDarkField("Full Name");
        JTextField contactField = createDarkField("Contact Number");
        JTextField emailField = createDarkField("Email");
        JTextField specializationField = createDarkField("Specialization");
        JTextField experienceField = createDarkField("Experience");
        JTextField usernameField = createDarkField("Username");
        JPasswordField passwordField = createDarkPassword("Password");
    
        panel.add(nameField); panel.add(contactField); panel.add(emailField);
        panel.add(specializationField); panel.add(experienceField);
    
        if(doctor == null){
            panel.add(usernameField); panel.add(passwordField);
        }
    
        // Pre-fill if editing
        if(doctor != null){
            nameField.setText(doctor.getName());
            contactField.setText(doctor.getContact());
            emailField.setText(doctor.getEmail());
            specializationField.setText(doctor.getSpecialization());
            experienceField.setText(String.valueOf(doctor.getExperience()));
            if(readOnly){
                nameField.setEditable(false); contactField.setEditable(false);
                emailField.setEditable(false); specializationField.setEditable(false);
                experienceField.setEditable(false); usernameField.setEditable(false);
                passwordField.setEditable(false);
            }
        }
    
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);
        JButton closeBtn = createDarkButton("Close", new Color(200,100,10), new Color(100,0,0));
        closeBtn.addActionListener(e -> d.dispose());
        btnPanel.add(closeBtn);
    
        if(!readOnly){
            JButton submitBtn = createDarkButton(doctor == null ? "Add Doctor" : "Update Doctor", new Color(0,153,0), new Color(0,102,0));
            submitBtn.addActionListener(e -> {
                // Validation & save logic (same as before)
                if(nameField.getText().trim().isEmpty() || (doctor==null && (usernameField.getText().trim().isEmpty() || passwordField.getPassword().length==0))){
                    JOptionPane.showMessageDialog(d,"Name, Username & Password cannot be empty");
                    return;
                }
    
                int experience = 0;
                try { experience = Integer.parseInt(experienceField.getText().trim().replaceAll("[^0-9]","")); }
                catch(Exception ex){ JOptionPane.showMessageDialog(d, "Experience must be a number"); return; }
    
                if(doctor==null){
                    if(userDao.getUserByUsername(usernameField.getText().trim()) != null){
                        JOptionPane.showMessageDialog(d, "Username already exists!"); 
                        return;
                    }
                    User user = new User();
                    user.setUsername(usernameField.getText().trim());
                    user.setPassword(new String(passwordField.getPassword()));
                    user.setRole("doctor");
                    int userId = userDao.registerUser(user);
    
                    Doctor dc = new Doctor();
                    dc.setUserId(userId);
                    dc.setName(nameField.getText().trim());
                    dc.setContact(contactField.getText().trim());
                    dc.setEmail(emailField.getText().trim());
                    dc.setSpecialization(specializationField.getText().trim());
                    dc.setExperience(experience);
                    doctorDao.addDoctor(dc);
                } else {
                    doctor.setName(nameField.getText().trim());
                    doctor.setContact(contactField.getText().trim());
                    doctor.setEmail(emailField.getText().trim());
                    doctor.setSpecialization(specializationField.getText().trim());
                    doctor.setExperience(experience);
                    doctorDao.updateDoctor(doctor);
                }
    
                refreshDoctors();
                JOptionPane.showMessageDialog(d, "Added succesfully"); 
                d.dispose();
            });
            btnPanel.add(submitBtn);
        }
    
        panel.add(btnPanel);
        d.add(panel);
        d.setVisible(true);
    }

    private JTextField createDarkField(String placeholder){
    JTextField field = new JTextField();
    field.setMaximumSize(new Dimension(400,30));
    field.setFont(new Font("Arial", Font.PLAIN, 14));
    field.setForeground(Color.WHITE);
    field.setBackground(new Color(50,60,80));
    field.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    setupPlaceholder(field, placeholder, false);
    return field;
}

private JPasswordField createDarkPassword(String placeholder){
    JPasswordField field = new JPasswordField();
    field.setMaximumSize(new Dimension(400,30));
    field.setFont(new Font("Arial", Font.PLAIN, 14));
    field.setForeground(Color.WHITE);
    field.setBackground(new Color(50,60,80));
    field.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    setupPlaceholder(field, placeholder, true);
    return field;
}

private JButton createDarkButton(String text, Color normal, Color hover){
    JButton btn = new JButton(text);
    btn.setFont(new Font("Arial", Font.BOLD, 14));
    btn.setForeground(Color.WHITE);
    btn.setBackground(normal);
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
    btn.setOpaque(true);
    btn.setPreferredSize(new Dimension(130,35));
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e){ btn.setBackground(hover);}
        public void mouseExited(java.awt.event.MouseEvent e){ btn.setBackground(normal);}
    });
    return btn;
}

// Reuse this from Register class
private void setupPlaceholder(JTextComponent comp, String placeholder, boolean isPassword){
    comp.setText(placeholder);
    comp.setForeground(Color.WHITE);
    if(isPassword) ((JPasswordField)comp).setEchoChar((char)0);

    comp.addFocusListener(new FocusAdapter(){
        @Override
        public void focusGained(FocusEvent e){
            if(comp.getText().equals(placeholder)){
                comp.setForeground(Color.GRAY);
                if(isPassword) ((JPasswordField)comp).setEchoChar((char)0);
            }
        }
        @Override
        public void focusLost(FocusEvent e){
            if(comp.getText().isEmpty()){
                comp.setText(placeholder);
                comp.setForeground(Color.WHITE);
                if(isPassword) ((JPasswordField)comp).setEchoChar((char)0);
            }
        }
    });

    comp.addKeyListener(new KeyAdapter(){
        @Override
        public void keyTyped(KeyEvent e){
            if(comp.getForeground()==Color.GRAY){
                comp.setText("");
                comp.setForeground(Color.WHITE);
                if(isPassword) ((JPasswordField)comp).setEchoChar('*');
            }
        }
    });
}

    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new DoctorManagement(null));
    }
}
