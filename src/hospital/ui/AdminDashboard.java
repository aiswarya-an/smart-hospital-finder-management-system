package hospital.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;

import hospital.dao.AppointmentDAO;
import hospital.dao.DoctorDAO;
import hospital.dao.HospitalDAO;
import hospital.dao.PatientDAO;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AdminDashboard {

    private JFrame frame;
    private JPanel sidebarPanel, mainPanel, calendarPanel, overlayPanel; // overlayPanel was null
    private JLabel lblHospitalsCount, lblDoctorsCount, lblPatientsCount, lblAppointmentsCount;
    private JLabel lblDateTime;
    private HospitalDAO hospitalDAO;
    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;
    private AppointmentDAO appointmentDAO;

    private final Set<LocalDate> registeredHospitalDates = new HashSet<>() {{
        add(LocalDate.now().minusDays(2));
        add(LocalDate.now());
        add(LocalDate.now().plusDays(3));
    }};

    private final Color DARK_BACKGROUND_PRIMARY = new Color(30, 41, 51, 180);
    private final Color DARK_BACKGROUND_SECONDARY = new Color(42, 54, 66, 180);
    private final Color FOREGROUND_LIGHT = new Color(236, 240, 241);

    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private final Font CARD_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    public AdminDashboard() {
        hospitalDAO = new HospitalDAO();
        doctorDAO = new DoctorDAO();
        patientDAO = new PatientDAO();
        appointmentDAO = new AppointmentDAO();

        frame = new JFrame("Admin Dashboard - Smart Hospital System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // FIX: The outer and root panels use BorderLayout.
        // We will set the layout on the *inner* panel (overlayPanel) to null for absolute positioning.
        // Remove frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        

        // Setup outer and root panels for complex border/background
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(34, 34, 50)); // dark background
        outer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // padding from edges

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(44, 44, 60)); // slightly lighter dark
        root.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(20, 20, 30), 2),   // inner dark line
            BorderFactory.createLineBorder(new Color(70, 70, 90), 4)    // outer dark line
        ));

        outer.add(root, BorderLayout.CENTER);
        frame.setContentPane(outer);
        
        // FIX: Initialize overlayPanel and add it to the root panel.
        // The overlayPanel will hold all absolutely positioned elements (sidebar, header, scrollPane).
        overlayPanel = new JPanel(null); 
        overlayPanel.setOpaque(false); // Let the root background show through
        root.add(overlayPanel, BorderLayout.CENTER);


        setupSidebar();
        setupTopHeader();
        
        // FIX: Adjust mainPanel initialization. We need to set a preferred size
        // for scrolling to work correctly.
        mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(30, 41, 51)); // dark primary
        // mainPanel.setBounds(220, 80, 980, 620); // Bounds are now managed by JScrollPane's viewport
        mainPanel.setPreferredSize(new Dimension(960, 1000)); // Set a large preferred size for scrolling

        // FIX: This section below will be removed as it's incorrect logic for nested JPanels/JScrollPanes.
        // The mainPanel is already in the scrollPane. The scrollPane is added to overlayPanel below.
        // overlayPanel.add(mainPanel); 
        
        // JScrollPane for scrolling main panel
        JScrollPane scrollPane = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                
        // FIX: Set the bounds for the JScrollPane relative to the overlayPanel (which covers 'root')
        // Width/Height calculated based on frame size minus sidebar and padding/header.
        int contentWidth = frame.getWidth() - 220 - 30; // 1200 - 220 (sidebar) - 30 (padding)
        int contentHeight = frame.getHeight() - 80 - 30; // 700 - 80 (header) - 30 (padding)
        
        scrollPane.setBounds(220, 80, contentWidth, contentHeight);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        // FIX: overlayPanel is now correctly initialized and can receive the component
        overlayPanel.add(scrollPane);

        // Drag/Touch scrolling effect (This logic remains fine)
        scrollPane.getViewport().addMouseMotionListener(new MouseMotionAdapter() {
            private Point lastPoint;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastPoint != null) {
                    JViewport viewport = (JViewport) e.getSource();
                    Point viewPos = viewport.getViewPosition();
                    int deltaY = lastPoint.y - e.getY();
                    viewPos.y += deltaY;
                    if (viewPos.y < 0) viewPos.y = 0;
                    if (viewPos.y + viewport.getHeight() > mainPanel.getHeight())
                        viewPos.y = mainPanel.getHeight() - viewport.getHeight();
                    viewport.setViewPosition(viewPos);
                }
                lastPoint = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastPoint = null;
            }
        });

        frame.setVisible(true);
    }

    private void setupSidebar() {
        sidebarPanel = new JPanel();
        // Position sidebar absolutely within the overlayPanel
        sidebarPanel.setBounds(0, 0, 220, frame.getHeight());
        sidebarPanel.setBackground(DARK_BACKGROUND_SECONDARY);
        sidebarPanel.setLayout(null);
        // FIX: overlayPanel is now initialized.
        overlayPanel.add(sidebarPanel);

        // GIF above admin name (Note: "resources/admin.gif" must be available)
        ImageIcon gifIcon = new ImageIcon("resources/admin.gif"); 
        Image img = gifIcon.getImage().getScaledInstance(140, 100, Image.SCALE_DEFAULT);
        gifIcon = new ImageIcon(img);
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(40, 40, 140, 100);
        sidebarPanel.add(gifLabel);

        JLabel adminName = new JLabel("Admin Name");
        adminName.setBounds(30, 150, 160, 25); 
        adminName.setForeground(FOREGROUND_LIGHT);
        adminName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        adminName.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarPanel.add(adminName);

        String[] options = {"Dashboard", "Hospitals", "Users", "Logout"};
        Color[][] colors = {
            {new Color(255, 85, 85, 180), new Color(255, 170, 85, 180)},  
            {new Color(85, 170, 255, 180), new Color(85, 255, 170, 180)}, 
            {new Color(170, 85, 255, 180), new Color(255, 85, 255, 180)}, 
            {new Color(255, 204, 102, 180), new Color(255, 102, 204, 180)}
        };

        int y = 200;
        for (int i = 0; i < options.length; i++) {
            String opt = options[i];
            JButton btn = createDarkButton(opt);
            btn.setBounds(20, y, 180, 45);
            btn.addActionListener(e -> {
                switch (opt) {
                    // case "Dashboard": break; // Implement dashboard view switch
                    case "Hospitals": showHospitalManagement(); break; 
                    case "Users": showUserManagement(); break; 
                    case "Logout": frame.dispose(); break;
                }
            });
            sidebarPanel.add(btn);
            y += 60;
        }
    }

    private JButton createDarkButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setFont(LABEL_FONT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90), 1, true));
        btn.setBackground(new Color(45, 55, 70));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){ btn.setBackground(new Color(60, 75, 95)); }
            public void mouseExited(MouseEvent e){ btn.setBackground(new Color(45, 55, 70)); }
        });
        return btn;
    }
    

    private void setupTopHeader() {
        JPanel topPanel = new JPanel(new BorderLayout());
        // Position header absolutely within the overlayPanel
        topPanel.setBounds(220, 0, frame.getWidth() - 220, 80);
        topPanel.setBackground(new Color(30, 41, 51)); // dark primary
    
        JLabel headerLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(Color.WHITE);
        topPanel.add(headerLabel, BorderLayout.CENTER);
    
        // FIX: overlayPanel is now initialized.
        overlayPanel.add(topPanel);
    }
    


    // (Uncommented/Unmodified helper methods as they were correct)
    private void showHospitalManagement() {
        new HospitalManagement(frame); // This class needs to exist
        // frame.setVisible(false);       
    }

    private void showUserManagement() {
        new UserManagement(frame);   // This class needs to exist
        // frame.setVisible(false);       
    }


}