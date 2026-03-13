package hospital.ui;

import javax.swing.*;
import hospital.models.Patient;
import hospital.dao.PatientDAO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PatientDashboard extends JFrame {
    private JPanel mainArea;
    private Patient currentPatient;
    private PatientDAO patientDAO;

    public PatientDashboard(Patient patient) {
        if (patient == null) {
            JOptionPane.showMessageDialog(null, "Error: Patient profile not loaded.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Patient object cannot be null.");
        }
        this.currentPatient = patient;
        this.patientDAO = new PatientDAO();

        setTitle("Patient Dashboard - " + currentPatient.getName());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        // ===== SIDEBAR =====
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(280, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(35, 45, 60));

        // ===== PROFILE PANEL (FIXED CENTERING) =====
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setOpaque(false);
        profilePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // allow BoxLayout to respect size
        profilePanel.setBorder(BorderFactory.createEmptyBorder(18, 12, 18, 12));

        // inner panel to center avatar + name using GridBagLayout
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(6, 6, 6, 6);

        String initial = currentPatient.getName() != null && !currentPatient.getName().isEmpty()
                ? currentPatient.getName().substring(0, 1).toUpperCase()
                : "?";
        JLabel avatar = new JLabel(makeAvatarIcon(90, 90, initial));
        inner.add(avatar, gbc);

        gbc.gridy++;
        // Name & email as centered multi-line label
        String name = currentPatient.getName() == null ? "Unknown" : currentPatient.getName();
        String email = currentPatient.getEmail() == null ? "—" : currentPatient.getEmail();
        JLabel patientName = new JLabel("<html><div style='text-align:center;'>" +
                "<b style='font-size:13px; color:#FFFFFF;'>" + escapeHtml(name) + "</b><br/>" +
                "<span style='font-size:12px; color:#cbd5e1;'>" + escapeHtml(email) + "</span>" +
                "</div></html>");
        patientName.setHorizontalAlignment(SwingConstants.CENTER);
        // ensure label doesn't expand full width awkwardly
        patientName.setMaximumSize(new Dimension(240, 60));
        inner.add(patientName, gbc);

        profilePanel.add(inner, BorderLayout.CENTER);
        side.add(profilePanel);

        // Separator
        side.add(Box.createVerticalStrut(6));
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        sep.setForeground(new Color(80, 90, 110));
        side.add(sep);
        side.add(Box.createVerticalStrut(10));

        // Navigation buttons
        side.add(navButton("🏥 Search Hospitals", e -> {
            dispose();
            new HospitalSearch(PatientDashboard.this);
        }));
        side.add(Box.createVerticalStrut(15));
        side.add(navButton("📋 My Appointments", e -> {
            dispose();
            new AppointmentHistory(currentPatient, this);
        }));
        side.add(Box.createVerticalStrut(15));
        side.add(navButton("✏️ Edit Profile", e -> {
            dispose();
            new ProfileEdit(currentPatient);
        }));
        side.add(Box.createVerticalGlue()); // push logout to bottom
        side.add(navButton("🚪 Logout", e -> {
            dispose();
            new Login();
        }));
        side.add(Box.createVerticalStrut(18));

        root.add(side, BorderLayout.WEST);

        // ===== MAIN AREA =====
        mainArea = new JPanel(new BorderLayout()) {
            // Uncomment this block below to add a background image later.
            /*
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = new ImageIcon("src/icons/patient_bg.jpg").getImage();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
            */
        };
        mainArea.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        mainArea.setBackground(new Color(250, 252, 255));
        root.add(mainArea, BorderLayout.CENTER);

        showDashboard();
        setVisible(true);
    }

    private void showDashboard() {
        mainArea.removeAll();

        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentPatient.getName() + " 👋");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(40, 40, 60));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subText = new JLabel("<html><div style='width:680px; color:#555;'>"
                + "We’re glad to have you back! From here, you can easily explore nearby hospitals, "
                + "manage your appointments, and stay updated with your healthcare journey."
                + "</div></html>");
        subText.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subText.setAlignmentX(Component.LEFT_ALIGNMENT);
        subText.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        contentPanel.add(welcomeLabel);
        contentPanel.add(subText);
        contentPanel.add(Box.createVerticalStrut(30));

        // Placeholder for additional cards or widgets
        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        placeholder.setPreferredSize(new Dimension(760, 300));
        contentPanel.add(placeholder);

        mainArea.add(contentPanel, BorderLayout.NORTH);
        mainArea.revalidate();
        mainArea.repaint();
    }

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

    private Icon makeAvatarIcon(int w, int h, String initial) {
        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(90, 150, 250));
        g.fillOval(0, 0, w, h);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, w / 3));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(initial, (w - fm.stringWidth(initial)) / 2, (h + fm.getAscent() / 2) / 2 + 6);
        g.dispose();
        return new ImageIcon(buf);
    }

    // small helper to escape minimal HTML special chars to avoid rendering issues
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int patientUserId = 8;
            PatientDAO dao = new PatientDAO();
            Patient patient = dao.getPatientByUserId(patientUserId);

            if (patient != null) {
                new PatientDashboard(patient);
            } else {
                Patient mockPatient = new Patient();
                mockPatient.setUserId(patientUserId);
                mockPatient.setName("Alice");
                mockPatient.setEmail("alice@example.com");
                new PatientDashboard(mockPatient);
            }
        });
    }
}
