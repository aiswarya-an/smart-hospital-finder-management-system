package hospital.ui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.Border;

import hospital.dao.AdminDAO;
import hospital.dao.HospitalAdminDAO;
import hospital.dao.DoctorDAO;
import hospital.dao.PatientDAO;
import hospital.dao.UserDAO;
import hospital.models.Admin;
import hospital.models.HospitalAdmin;
import hospital.models.Doctor;
import hospital.models.Patient;
import hospital.models.User;

public class Login {
    private JFrame frame;
    private JPanel rootPanel;
    private CardLayout cardLayout;

    private AdminDAO adminDAO = new AdminDAO();
    private HospitalAdminDAO hospitalAdminDAO = new HospitalAdminDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private UserDAO userDAO = new UserDAO();

    public Login() {
        frame = new JFrame("Smart Hospital Finder and Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        JPanel loginPage = createLoginPage();

        rootPanel.add(loginPage, "Login");

        frame.setContentPane(rootPanel);
        frame.setVisible(true);
    }

 private JPanel createLoginPage() {
    BackgroundPanel page = new BackgroundPanel("src/icons/front.jpg");
    page.setLayout(new BorderLayout());

    // Header
    JPanel header = new JPanel();
    header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
    header.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    header.setOpaque(false);

    JLabel logo = loadImageLabel("/icons/logooo.png", 120, 120);
    JLabel title = new JLabel("Smart Hospital Finder and Management System");
    title.setFont(new Font("Arial", Font.BOLD, 32));
    title.setForeground(Color.WHITE);


    JButton adminLoginBtn = new JButton("Admin Login");
    adminLoginBtn.addActionListener(e -> showAdminLogin());

    JButton homeBtn = new JButton("Home");
    homeBtn.addActionListener(e -> {
        frame.dispose(); 
        SwingUtilities.invokeLater(() -> new SmoothScrollPanels()); 
    });


    header.add(logo);
    header.add(Box.createHorizontalStrut(20));
    header.add(title);
    header.add(Box.createHorizontalStrut(400));
    header.add(homeBtn);
    header.add(Box.createHorizontalGlue());
    header.add(adminLoginBtn);

    // Login Form
    JPanel formBox = new JPanel();
    formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
    formBox.setOpaque(false);

    
    JLabel loginTitle = new JLabel("Login");
    loginTitle.setFont(new Font("Arial", Font.BOLD, 20));
    loginTitle.setForeground(Color.WHITE);
    loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField username = new JTextField("Username");
    username.setMaximumSize(new Dimension(250, 30));
    username.setAlignmentX(Component.CENTER_ALIGNMENT);
    username.setToolTipText("Enter Username");
    username.setForeground(Color.WHITE);
    username.setCaretColor(Color.WHITE);
    username.setOpaque(false);
    username.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (username.getText().equals("Username")) {
                username.setText("");
                username.setForeground(Color.WHITE);
            }
        }
    
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (username.getText().isEmpty()) {
                username.setText("Username");
                username.setForeground(Color.GRAY);
            }
        }
    });



    JPasswordField password = new JPasswordField("Password");
    password.setEchoChar((char) 0);
    password.setMaximumSize(new Dimension(250, 30));
    password.setAlignmentX(Component.CENTER_ALIGNMENT);
    password.setToolTipText("Enter Password");
    password.setForeground(Color.WHITE);
    password.setCaretColor(Color.WHITE);
    password.setOpaque(false);

    password.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (String.valueOf(password.getPassword()).equals("Password")) {
                password.setText("");
                password.setEchoChar('•'); // Show dots when typing
                password.setForeground(Color.WHITE);
            }
        }
    
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (String.valueOf(password.getPassword()).isEmpty()) {
                password.setText("Password");
                password.setEchoChar((char) 0); // Hide dots again
                password.setForeground(Color.GRAY);
            }
        }
    });

    JButton loginBtn = new JButton("Login");
    loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    loginBtn.addActionListener(_ -> {
        String uname = username.getText().trim();
        String pass = new String(password.getPassword());

        if (uname.isEmpty() || pass.isEmpty() || uname.equals("Username") || pass.equals("Password")) {
            JOptionPane.showMessageDialog(page, "❌ Please enter Username and Password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User u = userDAO.validateLogin(uname, pass);

            if (u != null) {
                Window window = SwingUtilities.getWindowAncestor(page);
                if (window != null) window.dispose();

                

                switch (u.getRole()) {
                    case "DOCTOR":
                        JOptionPane.showMessageDialog(page, "✅ Login Successful as Doctor", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        DoctorDAO doctorDAO = new DoctorDAO();
                        Doctor doctor = doctorDAO.getDoctorByUserId(u.getUserId());
                        new DoctorDashboard(doctor);
                        break;
                    case "PATIENT":
                        JOptionPane.showMessageDialog(page, "✅ Login Successful as Patient", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();    
                        PatientDAO patientDAO = new PatientDAO();
                        Patient patient = patientDAO.getPatientByUserId(u.getUserId());
                        new PatientDashboard(patient);
                        break;
                    default:
                        JOptionPane.showMessageDialog(page, "❌ Invalid Role or Database error!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(page, "❌ Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(page, "⚠️ Database connection error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    

    JLabel signup = new JLabel("Don’t have an account? Sign Up");
    signup.setAlignmentX(Component.CENTER_ALIGNMENT);
    signup.setForeground(Color.WHITE);
    signup.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            frame.dispose(); // close login
            SwingUtilities.invokeLater(() -> new Register());
        }
    });

    formBox.add(loginTitle);
    formBox.add(Box.createVerticalStrut(10));
    formBox.add(username);
    formBox.add(Box.createVerticalStrut(10));
    formBox.add(password);
    formBox.add(Box.createVerticalStrut(10));
    formBox.add(loginBtn);
    formBox.add(Box.createVerticalStrut(10));
    // formBox.add(forgotpassword);
    formBox.add(signup);

    // Wrapper with black transparency and rounded corners
    JPanel formWrapper = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 180)); // semi-transparent black
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25); // rounded corners
            g2.dispose();
        }
    };
    formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
    formWrapper.setOpaque(false);
    formWrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    formWrapper.setMaximumSize(new Dimension(340, 360));
    formWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
    formWrapper.add(formBox);

    JPanel centerBox = new JPanel();
    centerBox.setLayout(new BoxLayout(centerBox, BoxLayout.Y_AXIS));
    centerBox.setOpaque(false);
    centerBox.add(Box.createVerticalGlue());
    centerBox.add(formWrapper);
    centerBox.add(Box.createVerticalGlue());

    page.add(header, BorderLayout.NORTH);
    page.add(centerBox, BorderLayout.CENTER);

    return page;
}
        

    private void showAdminLogin() {
        // Create dialog instead of JFrame
        JDialog loginDialog = new JDialog(frame, "Admin Login", false); // false = non-modal
        loginDialog.setUndecorated(true);
        loginDialog.setSize(300, 200);
        loginDialog.setBackground(new Color(0, 0, 0, 0));
        
        // Make sure dialog is always on top of the frame
        loginDialog.setAlwaysOnTop(true);

        // Position at top-right relative to main frame
        Runnable updatePosition = () -> {
            Rectangle bounds = frame.getBounds();
            int x = bounds.x + bounds.width - loginDialog.getWidth() - 20;
            int y = bounds.y + 30;
            loginDialog.setLocation(x, y);
        };
        updatePosition.run(); // initial position
    
        // Keep dialog aligned if main frame moves/resizes
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent e) { updatePosition.run(); }
            public void componentResized(java.awt.event.ComponentEvent e) { updatePosition.run(); }
        });

    
        // Panel with semi-transparent background and rounded corners
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
    
        // Close button
        JButton closeBtn = new JButton("X");
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorder(null);
        closeBtn.setForeground(Color.LIGHT_GRAY);
        closeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        closeBtn.addActionListener(_ -> loginDialog.dispose());
    
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(closeBtn, BorderLayout.EAST);
        panel.add(topPanel);
    
        // Title
        JLabel title = new JLabel("Admin Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
    
        // Username field
        JTextField username = new JTextField("Username");
        username.setMaximumSize(new Dimension(250, 28));
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setToolTipText("Enter Username");
        username.setForeground(Color.WHITE);
        username.setCaretColor(Color.WHITE);
        username.setOpaque(false);
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (username.getText().equals("Username")) {
                    username.setText("");
                    username.setForeground(Color.WHITE);
                }
            }
        
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (username.getText().isEmpty()) {
                    username.setText("Username");
                    username.setForeground(Color.GRAY);
                }
            }
        });
    
        // Password field
        JPasswordField password = new JPasswordField("Password");
        password.setEchoChar((char) 0);
        password.setMaximumSize(new Dimension(250, 30));
        password.setAlignmentX(Component.CENTER_ALIGNMENT);
        password.setToolTipText("Enter Password");
        password.setForeground(Color.WHITE);
        password.setCaretColor(Color.WHITE);
        password.setOpaque(false);
    
        password.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (String.valueOf(password.getPassword()).equals("Password")) {
                    password.setText("");
                    password.setEchoChar('•'); // Show dots when typing
                    password.setForeground(Color.WHITE);
                }
            }
        
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (String.valueOf(password.getPassword()).isEmpty()) {
                    password.setText("Password");
                    password.setEchoChar((char) 0); // Hide dots again
                    password.setForeground(Color.GRAY);
                }
            }
        });
    
    
        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(_ -> {
            String user = username.getText();
            String pass = new String(password.getPassword());
        
            UserDAO userDAO = new UserDAO();
            User u = userDAO.validateLogin(user, pass);
            if (u != null) {
                switch (u.getRole()) {
                    case "ADMIN":
                        JOptionPane.showMessageDialog(loginDialog, "✅ Login Successful as Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loginDialog.dispose();
                        new AdminDashboard();
                        break;
                    case "HOSPITAL_ADMIN":
                        JOptionPane.showMessageDialog(loginDialog, "✅ Login Successful as Hospital Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loginDialog.dispose();
                        HospitalAdminDAO adminDAO = new HospitalAdminDAO();
                        HospitalAdmin admin = adminDAO.getByUserId(u.getUserId());

                        // Pass admin object and username to dashboard
                        new HospitalAdminDashboard(admin, user);
                        break;
                    default: // Doctor/Patient
                        JOptionPane.showMessageDialog(loginDialog, "❌ Only Admins can log in here!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(loginDialog, "❌ Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(username);
        panel.add(Box.createVerticalStrut(10));
        panel.add(password);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loginBtn);
    
        loginDialog.setContentPane(panel);
        loginDialog.setVisible(true);
    }
    

    private JLabel loadImageLabel(String path, int width, int height) {
        URL url = getClass().getResource(path);
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(img));
        } else {
            System.err.println("Image not found: " + path);
            return new JLabel("Logo");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
