package hospital.ui;

import javax.swing.*;

import hospital.dao.AppointmentDAO;
import hospital.dao.DoctorDAO;
import hospital.dao.HospitalAdminDAO;
import hospital.dao.PatientDAO;
import hospital.models.Hospital;
import hospital.models.HospitalAdmin;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class HospitalAdminDashboard extends JFrame {
    private JPanel mainArea;
    private JLabel adminNameLabel;
    private HospitalAdmin loggedAdmin;

    public HospitalAdminDashboard(HospitalAdmin admin, String username) {
        setTitle("Hospital Admin — Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root content
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        // Sidebar
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(300, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(35, 45, 60));


        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(new Color(35, 45, 60));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(Box.createVerticalStrut(20)); 


        JLabel avatar = new JLabel(makeAvatarIcon(90, 90));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(avatar);

        profilePanel.add(Box.createVerticalStrut(10));

        // Admin name
        HospitalAdminDAO hospitalAdminDAO = new HospitalAdminDAO();
        this.loggedAdmin = hospitalAdminDAO.getByUserId(admin.getUserId()); 
        adminNameLabel = new JLabel();
        // JLabel adminNameLabel = new JLabel("<html><div style='padding-left:80px;'><b>Admin</b><br/><small>System Administrator</small></html>");
        adminNameLabel.setText("<html><div style='padding-left:80px;'><b>" 
        + loggedAdmin.getName() + "</b><br/><small>Hospital Admin</small></html>");
    
        adminNameLabel.setForeground(Color.WHITE);
        adminNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        adminNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(adminNameLabel);

        profilePanel.add(Box.createVerticalStrut(6));  // controls space to JSeparator


        side.add(profilePanel);
        side.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        sep.setForeground(new Color(80, 90, 110));
        side.add(sep);
        side.add(Box.createVerticalStrut(10));

        // Sidebar Buttons
        // side.add(navButton("🏠  Dashboard (Home)", e -> showDashboard()));
        // side.add(Box.createVerticalStrut(20));
        side.add(navButton("👨‍⚕️  Doctors Management", e -> openDoctors()));
        side.add(Box.createVerticalStrut(20));
        side.add(navButton("🧍‍♂️  Patients Management", e -> openPatients()));
        side.add(Box.createVerticalStrut(20));
        side.add(navButton("📅  Appointments", e -> openAppointments()));
        side.add(Box.createVerticalStrut(20));
        side.add(navButton("📊  Reports", e -> JOptionPane.showMessageDialog(this, "Reports coming soon!")));
        side.add(Box.createVerticalStrut(40));
        side.add(navButton("🚪  Logout", e -> {
            dispose();
            new Login();
        }));

        root.add(side, BorderLayout.WEST);

        // Main area
        mainArea = new JPanel(new BorderLayout());
        mainArea.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainArea.setBackground(new Color(250, 252, 255));
        root.add(mainArea, BorderLayout.CENTER);

        buildDashboardContents();

        setVisible(true);
    }

    private void buildDashboardContents() {
        mainArea.removeAll();

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel welcome = new JLabel(
        "<html><span style='font-size:22px; color:#333;'><b>Welcome, Admin</b></span>" +
        "<br/><span style='font-size:16px; color:#555;'>Manage doctors, patients, and appointments</span></html>"
            );
        top.add(welcome, BorderLayout.WEST);

        // Stat Cards Section
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        top.add(cards, BorderLayout.SOUTH);

        mainArea.add(top, BorderLayout.NORTH);

        // Info Section
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);

        JLabel info = new JLabel("<html><br/><span style='font-size:14px; color:#555;'>Use the left menu to navigate and manage hospital data.</span></html>");
        center.add(info, BorderLayout.CENTER);

        mainArea.add(center, BorderLayout.CENTER);

        mainArea.revalidate();
        mainArea.repaint();
    }

    // private JPanel statCard(String title, int value, Color color) {
    //     JPanel card = new JPanel(new BorderLayout());
    //     card.setPreferredSize(new Dimension(180, 80));
    //     card.setBackground(color);
    //     card.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));
    //     card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    //     card.setOpaque(true);

    //     JLabel lblTitle = new JLabel(title);
    //     lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
    //     lblTitle.setForeground(Color.WHITE);

    //     JLabel lblValue = new JLabel(String.valueOf(value));
    //     lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
    //     lblValue.setForeground(Color.WHITE);

    //     card.add(lblTitle, BorderLayout.NORTH);
    //     card.add(lblValue, BorderLayout.CENTER);

    //     // Hover animation
    //     card.addMouseListener(new MouseAdapter() {
    //         public void mouseEntered(MouseEvent e) { card.setBackground(color.darker()); }
    //         public void mouseExited(MouseEvent e) { card.setBackground(color); }
    //     });

    //     return card;
    // }

    private JButton navButton(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(new Color(35, 45, 60));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(al);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(55, 70, 90)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(35, 45, 60)); }
        });
        return btn;
    }

    private Icon makeAvatarIcon(int w, int h) {
        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(90, 150, 250));
        g.fillOval(0, 0, w, h);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, w / 3));
        String s = "A";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (w - fm.stringWidth(s)) / 2, (h + fm.getAscent() / 2) / 2 + 6);
        g.dispose();
        return new ImageIcon(buf);
    }

    // Dummy pages
    private void openDoctors() { new DoctorManagement(this); }
    private void openPatients() { new PatientRecords(this); }
    private void openAppointments() { new AppointmentManagement(this); }
    private void showDashboard() { buildDashboardContents(); }

    // public static void main(String[] args) {
    //     String dummyAdmin = new String();
    //     dummyAdmin.setName("Admin");  // assuming you have setName() in your model
    //     SwingUtilities.invokeLater(() -> new HospitalAdminDashboard(dummyAdmin));
    // }
    
 }
