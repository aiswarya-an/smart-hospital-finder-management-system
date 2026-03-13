// package hospital.ui;

// import javax.swing.*;

// import hospital.dao.DoctorDAO;
// import hospital.models.Doctor;

// import java.awt.*;
// import java.awt.event.*;
// import java.awt.image.BufferedImage;

// public class DoctorDashboard extends JFrame {

//     private JPanel mainArea;
//     private Doctor currentDoctor; // New field to hold the doctor object
//     private DoctorDAO doctorDAO; // New field for DAO
//     // private String doctorName = "Dr. Nisha";
//     // private String specialization = "Cardiologist";

//     // Sample stats
//     // private int totalPatientsToday = 20;
//     // private int completedAppointments = 12;
//     // private int pendingAppointments = 8;

//     public DoctorDashboard(Doctor doctor) {
//         setTitle("Doctor Dashboard");
//         setSize(1000, 600);
//         setLocationRelativeTo(null);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);

//         JPanel root = new JPanel(new BorderLayout());
//         root.setBackground(new Color(245, 247, 250));
//         setContentPane(root);

//         this.currentDoctor = doctor; // Store the doctor object
//         this.doctorDAO = new DoctorDAO(); // Initialize the DAO

//         // ===== Sidebar =====
//         JPanel side = new JPanel();
//         side.setPreferredSize(new Dimension(250, 0));
//         side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
//         side.setBackground(new Color(20, 20, 30));

//         // Profile panel
//         JPanel profilePanel = new JPanel();
//         profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
//         profilePanel.setBackground(new Color(20, 20, 30));
//         profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//         profilePanel.add(Box.createVerticalStrut(20));

//         String initial = currentDoctor.getName().substring(0, 1).toUpperCase();
//         JLabel avatar = new JLabel(makeAvatarIcon(90, 90, initial)); // Use dynamic initial
//         avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
//         profilePanel.add(avatar);
//         profilePanel.add(Box.createVerticalStrut(10));

//         JLabel nameLabel = new JLabel("<html><div style='padding-left:60px;'><b>" + currentDoctor.getName() + "</b><br/><small>" +  currentDoctor.getSpecialization() + "</small></html>");
//         nameLabel.setForeground(Color.WHITE);
//         nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
//         nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//         profilePanel.add(nameLabel);

//         profilePanel.add(Box.createVerticalStrut(15));

//         side.add(profilePanel);

//         JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
//         sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
//         sep.setForeground(Color.GRAY);
//         side.add(sep);
//         side.add(Box.createVerticalStrut(15));

//         // Sidebar buttons
//         side.add(navButton("🏠  Dashboard", e -> showDashboard()));
//         side.add(Box.createVerticalStrut(10));
//         side.add(navButton("👤  Profile", e -> JOptionPane.showMessageDialog(this, "Profile Page Coming Soon!")));
//         side.add(Box.createVerticalStrut(10));
//         side.add(navButton("📅  Appointments", e -> showAppointments()));
//         side.add(Box.createVerticalStrut(10));
//         side.add(navButton("🧾  Patient History", e -> showPatientHistory()));
//         side.add(Box.createVerticalGlue());
//         side.add(navButton("🚪  Logout", e -> {
//             dispose();
//             JOptionPane.showMessageDialog(null, "Logged out successfully!");
//         }));
//         side.add(Box.createVerticalStrut(20));

//         root.add(side, BorderLayout.WEST);

//         // ===== Main Area =====
//         mainArea = new JPanel(new BorderLayout());
//         mainArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//         mainArea.setBackground(new Color(34, 34, 40));
//         root.add(mainArea, BorderLayout.CENTER);

//         showDashboard(); // default view

//         setVisible(true);
//     }

//     // ===== Sidebar button with hover effect =====
//     private JButton navButton(String text, ActionListener al) {
//         JButton btn = new JButton(text);
//         btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
//         btn.setAlignmentX(Component.LEFT_ALIGNMENT);
//         btn.setBackground(new Color(20, 20, 30));
//         btn.setForeground(Color.WHITE);
//         btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
//         btn.setFocusPainted(false);
//         btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 10));
//         btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         btn.setHorizontalAlignment(SwingConstants.LEFT);
//         btn.addActionListener(al);

//         // Hover effect
//         btn.addMouseListener(new MouseAdapter() {
//             public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50, 50, 70)); }
//             public void mouseExited(MouseEvent e) { btn.setBackground(new Color(20, 20, 30)); }
//         });
//         return btn;
//     }

//     // ===== Avatar icon =====
//     private Icon makeAvatarIcon(int w, int h, String initial) {
//         BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//         Graphics2D g = buf.createGraphics();
//         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//         g.setColor(new Color(90, 150, 250));
//         g.fillOval(0, 0, w, h);
//         g.setColor(Color.WHITE);
//         g.setFont(new Font("SansSerif", Font.BOLD, w / 2));
//         FontMetrics fm = g.getFontMetrics();
//         g.drawString(initial, (w - fm.stringWidth(initial)) / 2, (h + fm.getAscent() / 2) / 2 + 6);
//         g.dispose();
//         return new ImageIcon(buf);
//     }

//     // ===== Main area views =====
//     private void showDashboard() {
//         mainArea.removeAll();

//         // 1. Fetch dynamic stats (assuming 'doctorId' is available in currentDoctor)
//         int doctorUserId = currentDoctor.getUserId(); // Use the Doctor's User ID
//         String today = new java.sql.Date(System.currentTimeMillis()).toString();
        
//         // You'll need to implement these DAO calls. For now, use sample logic:
//         // Total is usually counted by date, regardless of status.
//         int totalToday = doctorDAO.getAppointmentCount(doctorUserId, today, "ALL"); 
//         int completed = doctorDAO.getAppointmentCount(doctorUserId, today, "COMPLETED");
//         int pending = doctorDAO.getAppointmentCount(doctorUserId, today, "PENDING");
        
//         //welcome
//         JLabel welcome = new JLabel("<html><span style='font-size:20px; color:#fff;'>Welcome, <b>" + currentDoctor.getName() + "</b></span><br/><span style='color:#aaa;'>Manage appointments and patient info</span></html>");
//         welcome.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
//         mainArea.add(welcome, BorderLayout.NORTH);

//         // Stat cards - use fetched variables
//         JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
//         cards.setOpaque(false);
//         cards.setBorder(BorderFactory.createEmptyBorder(60,0,60,0));

//         cards.add(statCard("Total Patients Today", totalToday, new Color(50, 50, 70)));
//         cards.add(statCard("Completed Appointments", completed, new Color(50, 50, 90)));
//         cards.add(statCard("Pending Appointments", pending, new Color(50, 50, 110)));

//         mainArea.add(cards, BorderLayout.CENTER);

//         // Quick links
//         JPanel quickLinks = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
//         quickLinks.setOpaque(false);

//         JButton viewAppointments = quickButton("View Today's Schedule");
//         JButton viewHistory = quickButton("View Patient History");
//         quickLinks.add(viewAppointments);
//         quickLinks.add(viewHistory);

//         viewAppointments.addActionListener(e -> showAppointments());
//         viewHistory.addActionListener(e -> showPatientHistory());

//         mainArea.add(quickLinks, BorderLayout.SOUTH);

//         mainArea.revalidate();
//         mainArea.repaint();
//     }

//     private JPanel statCard(String title, int value, Color color) {
//         JPanel card = new JPanel(new BorderLayout());
//         card.setPreferredSize(new Dimension(150, 80));
//         card.setBackground(color);
//         card.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
//         card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         card.setOpaque(true);

//         JLabel lblTitle = new JLabel(title);
//         lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
//         lblTitle.setForeground(Color.WHITE);

//         JLabel lblValue = new JLabel(String.valueOf(value));
//         lblValue.setFont(new Font("SansSerif", Font.BOLD, 20));
//         lblValue.setForeground(Color.WHITE);

//         card.add(lblTitle, BorderLayout.NORTH);
//         card.add(lblValue, BorderLayout.CENTER);

//         // Hover effect
//         card.addMouseListener(new MouseAdapter() {
//             public void mouseEntered(MouseEvent e) { card.setBackground(color.darker()); }
//             public void mouseExited(MouseEvent e) { card.setBackground(color); }
//         });

//         return card;
//     }

//     private JButton quickButton(String text) {
//         JButton btn = new JButton(text);
//         btn.setBackground(new Color(30, 30, 60));
//         btn.setForeground(Color.WHITE);
//         btn.setFont(new Font("SansSerif", Font.BOLD, 14));
//         btn.setFocusPainted(false);
//         btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         return btn;
//     }

//     private void showAppointments() {
//         // mainArea.removeAll();
//         // JLabel label = new JLabel("Appointments Page Coming Soon!");
//         // label.setForeground(Color.WHITE);
//         // label.setFont(new Font("SansSerif", Font.BOLD, 24));
//         // label.setHorizontalAlignment(SwingConstants.CENTER);
//         // mainArea.add(label, BorderLayout.CENTER);
//         // mainArea.revalidate();
//         // mainArea.repaint();

//         new AppointmentSchedule();
//     }

//     private void showPatientHistory() {
//         // mainArea.removeAll();
//         // JLabel label = new JLabel("Patient History Page Coming Soon!");
//         // label.setForeground(Color.WHITE);
//         // label.setFont(new Font("SansSerif", Font.BOLD, 24));
//         // label.setHorizontalAlignment(SwingConstants.CENTER);
//         // mainArea.add(label, BorderLayout.CENTER);
//         // mainArea.revalidate();
//         // mainArea.repaint();

//         new PatientHistory();
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             // 🚨 MOCK DOCTOR for testing the UI 🚨
//             Doctor mockDoctor = new Doctor(); 
//             mockDoctor.setUserId(1);
//             mockDoctor.setName("Dr. Emily Carter");
//             mockDoctor.setSpecialization("Pediatrician");
//             // Assume you have a Doctor model
//             new DoctorDashboard(mockDoctor); 
//         });
//     }
// }

package hospital.ui;

import javax.swing.*;
import hospital.models.Doctor;
import hospital.dao.DoctorDAO; // Import DAO
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate; // Use modern date for today's date

public class DoctorDashboard extends JFrame {

    private JPanel mainArea;
    private Doctor currentDoctor; // Holds the logged-in doctor's data

    // REMOVED: private String doctorName = "Dr. Nisha";
    // REMOVED: private String specialization = "Cardiologist";
    // REMOVED: Sample stats

    public DoctorDashboard(Doctor doctor) {
        
        // Essential initialization
        if (doctor == null) {
            JOptionPane.showMessageDialog(null, "Error: Doctor profile not loaded.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        this.currentDoctor = doctor;

        setTitle("Doctor Dashboard - " + doctor.getName());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        // ===== Sidebar =====
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(250, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(20, 20, 30));

        // Profile panel - Using dynamic doctor data
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(new Color(20, 20, 30));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(Box.createVerticalStrut(20));

        String initial = currentDoctor.getName().substring(0, 1).toUpperCase();
        JLabel avatar = new JLabel(makeAvatarIcon(90, 90, initial)); // Dynamic Initial
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(avatar);
        profilePanel.add(Box.createVerticalStrut(10));

        JLabel nameLabel = new JLabel(
            "<html><div style='padding-left:60px;'><b>" + 
            currentDoctor.getName() + 
            "</b><br/><small>" + 
            currentDoctor.getSpecialization() + 
            "</small></html>"
        ); // Dynamic Name/Specialization
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(nameLabel);

        profilePanel.add(Box.createVerticalStrut(15));

        side.add(profilePanel);

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(Color.GRAY);
        side.add(sep);
        side.add(Box.createVerticalStrut(15));

        // Sidebar buttons
        side.add(navButton("🏠  Dashboard", e -> showDashboard()));
        side.add(Box.createVerticalStrut(10));
        side.add(navButton("👤  Profile", e -> JOptionPane.showMessageDialog(this, "Profile Page Coming Soon!")));
        side.add(Box.createVerticalStrut(10));
        side.add(navButton("📅  Appointments", e -> showAppointments()));
        side.add(Box.createVerticalStrut(10));
        side.add(navButton("🧾  Patient History", e -> showPatientHistory()));
        side.add(Box.createVerticalGlue());
        side.add(navButton("🚪  Logout", e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
        }));
        side.add(Box.createVerticalStrut(20));

        root.add(side, BorderLayout.WEST);

        // ===== Main Area =====
        mainArea = new JPanel(new BorderLayout());
        mainArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainArea.setBackground(new Color(34, 34, 40));
        root.add(mainArea, BorderLayout.CENTER);

        showDashboard(); // default view

        setVisible(true);
    }
    
    // ... (navButton method remains unchanged) ...
    private JButton navButton(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(new Color(20, 20, 30));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(al);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50, 50, 70)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(20, 20, 30)); }
        });
        return btn;
    }

    // ... (makeAvatarIcon method remains unchanged) ...
    private Icon makeAvatarIcon(int w, int h, String initial) {
        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(90, 150, 250));
        g.fillOval(0, 0, w, h);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, w / 2));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(initial, (w - fm.stringWidth(initial)) / 2, (h + fm.getAscent() / 2) / 2 + 6);
        g.dispose();
        return new ImageIcon(buf);
    }

    // ===== Main area views (UPDATED) =====
    private void showDashboard() {
        mainArea.removeAll();

        // 1. Fetch Dynamic Stats
        int doctorUserId = currentDoctor.getUserId();
        // Use java.sql.Date format for the database query
        String todayDate = LocalDate.now().toString(); 
        
        // int totalPatientsToday = doctorDAO.getAppointmentCount(doctorUserId, todayDate, "ALL");
        // int completedAppointments = doctorDAO.getAppointmentCount(doctorUserId, todayDate, "Completed");
        // // Pending logic: Scheduled + Confirmed
        // int scheduledAppointments = doctorDAO.getAppointmentCount(doctorUserId, todayDate, "Scheduled");
        // int confirmedAppointments = doctorDAO.getAppointmentCount(doctorUserId, todayDate, "Confirmed");
        // int pendingAppointments = scheduledAppointments + confirmedAppointments;
        
        // Welcome
        JLabel welcome = new JLabel("<html><span style='font-size:20px; color:#fff;'>Welcome, <b>" + currentDoctor.getName() + "</b></span><br/><span style='color:#aaa;'>Manage appointments and patient info</span></html>");
        welcome.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
        mainArea.add(welcome, BorderLayout.NORTH);

        // Stat cards - use fetched variables
        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(60,0,60,0));

        // cards.add(statCard("Total Patients Today", totalPatientsToday, new Color(50, 50, 70)));
        // cards.add(statCard("Completed Appointments", completedAppointments, new Color(50, 50, 90)));
        // cards.add(statCard("Pending Appointments", pendingAppointments, new Color(50, 50, 110)));

        mainArea.add(cards, BorderLayout.CENTER);

        // Quick links
        JPanel quickLinks = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        quickLinks.setOpaque(false);

        JButton viewAppointments = quickButton("View Today's Schedule");
        JButton viewHistory = quickButton("View Patient History");
        quickLinks.add(viewAppointments);
        quickLinks.add(viewHistory);

        viewAppointments.addActionListener(e -> showAppointments());
        viewHistory.addActionListener(e -> showPatientHistory());

        mainArea.add(quickLinks, BorderLayout.SOUTH);

        mainArea.revalidate();
        mainArea.repaint();
    }

    // ... (statCard method remains unchanged) ...
    private JPanel statCard(String title, int value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(150, 80));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setOpaque(true);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel(String.valueOf(value));
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblValue.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { card.setBackground(color); }
        });

        return card;
    }

    // ... (quickButton method remains unchanged) ...
    private JButton quickButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(30, 30, 60));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // NOTE: showAppointments and showPatientHistory assume AppointmentSchedule and PatientHistory classes exist
    private void showAppointments() {
        // new AppointmentSchedule(); // Uncomment if class exists
        // JOptionPane.showMessageDialog(this, "Appointments Page (Requires AppointmentSchedule class)", "Info", JOptionPane.INFORMATION_MESSAGE);
        new AppointmentSchedule(currentDoctor);
    }

    private void showPatientHistory() {
        new hospital.ui.PatientHistory(currentDoctor);
        // Uncomment if class exists
        //JOptionPane.showMessageDialog(this, "Patient History Page (Requires PatientHistory class)", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 🚨 MOCK LOGIN FOR TESTING 🚨
            DoctorDAO dao = new DoctorDAO();
            // Get the doctor profile for 'doc1' (user_id 6 from your inserts)
            Doctor doc = dao.getDoctorByUserId(6); 
            
            if (doc != null) {
                new DoctorDashboard(doc);
            } else {
                // Fallback for UI testing if DB connection fails
                Doctor mockDoctor = new Doctor();
                mockDoctor.setUserId(6); 
                mockDoctor.setName("Dr. Priya Nair");
                mockDoctor.setSpecialization("Cardiology");
                new DoctorDashboard(mockDoctor);
            }
        });
    }
}
