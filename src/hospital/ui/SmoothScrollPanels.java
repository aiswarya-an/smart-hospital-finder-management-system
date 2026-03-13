package hospital.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.xml.crypto.Data;

import hospital.models.Hospital;

public class SmoothScrollPanels {

    private enum Alignment { LEFT, RIGHT }
    private JPanel mainPanel;
    private JFrame frame;
    private JScrollPane scrollPane;
    private JPanel doctorsPanel;  // top of services
    private JPanel contactPanel;  // bottom of page


    private static class SectionData {
        String title;
        String description;
        String imagePath;
        Alignment alignment;

        public SectionData(String title, String description, String imagePath, Alignment alignment) {
            this.title = title;
            this.description = description;
            this.imagePath = imagePath;
            this.alignment = alignment;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmoothScrollPanels::new);
    }

    public SmoothScrollPanels() {
        frame = new JFrame("Smart Hospital Finder and Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);

        // === Background images ===
        String image1 = "src/icons/123.jpg";
        String image2 = "src/icons/124.jpg";
        String image3 = "src/icons/125.jpg";

        // === Panel 2 circular images ===
        String circleImgPath1 = "src/icons/neuro.gif";
        String circleImgPath2 = "src/icons/heart.gif";
        String circleImgPath3 = "src/icons/ped.gif";
        String circleImgPath4 = "src/icons/vet.gif";

        // === Panel 3 circular images ===
        String circleImgPath5 = "src/icons/den.gif";
        String circleImgPath6 = "src/icons/gyno.gif";
        String circleImgPath7 = "src/icons/ortho.gif";
        String circleImgPath8 = "src/icons/derm.gif";

        // === Panels ===
        mainPanel.add(createFirstPanel(image1));

        mainPanel.add(createImagePanel(image2, new SectionData[]{
                new SectionData("Neurologists –", "Doctors who specialize in diagnosing and treating disorders of the brain, spinal cord, and nervous system. They manage conditions like epilepsy, stroke, multiple sclerosis, and Parkinson’s disease.", circleImgPath1, Alignment.RIGHT),
                new SectionData("Cardiologists", "Doctors who focus on the heart and blood vessels. They treat heart diseases such as coronary artery disease, heart failure, arrhythmias, and high blood pressure.", circleImgPath2, Alignment.LEFT),
                new SectionData("Pediatricians", "They guide children’s growth and development, prevent diseases through vaccinations, and treat illnesses early, ensuring a healthy start in life.", circleImgPath3, Alignment.RIGHT),
                new SectionData("Veterinarians –", "They care for animals, ensuring pets and livestock stay healthy, which protects both animal welfare and human health by preventing the spread of diseases.", circleImgPath4, Alignment.LEFT)
        }));

        JPanel servicesPanel = createImagePanel(image3, new SectionData[]{
                new SectionData("Dentists", "Oral health is key to overall health. Dentists prevent cavities, gum disease, and other oral problems, which can impact nutrition, speech, and self-confidence.", circleImgPath5, Alignment.RIGHT),
                new SectionData("Gynecologists", "Ensure women’s reproductive health, supporting pregnancy, childbirth, and overall wellness. Regular care helps detect diseases early and promotes lifelong health for women.", circleImgPath6, Alignment.LEFT),
                new SectionData("Orthopedic Doctors", "They keep our bones, joints, and muscles strong and functional. Orthopedic care helps prevent injuries, treats fractures, and restores mobility, improving daily life and physical activity.", circleImgPath7, Alignment.RIGHT),
                new SectionData("Dermatologists", "Doctors who specialize in skin, hair, and nail conditions. They treat acne, eczema, psoriasis, skin infections, and perform skin cancer screenings.", circleImgPath8, Alignment.LEFT)
        });
        mainPanel.add(servicesPanel);

        // === Contact Section at the bottom ===
        mainPanel.add(createContactPanel());

        // === ScrollPane ===
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);

        // 👇 Always open at the top (first panel)
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

        frame.add(scrollPane);
        frame.setVisible(true);
    }

    private JPanel createFirstPanel(String bgImagePath) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel panel = new JPanel(new BorderLayout()) {
            Image bg = new ImageIcon(bgImagePath).getImage()
                    .getScaledInstance(screen.width, screen.height, Image.SCALE_SMOOTH);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setPreferredSize(new Dimension(screen.width, screen.height));
        panel.setOpaque(false);

        // === Navigation Bar ===
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 20)){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 150)); // semi-transparent black background
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        navBar.setOpaque(false);
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        navBar.add(createNavLabel("Login"));
        navBar.add(createNavLabel("Register"));
        navBar.add(createNavLabel("About Us"));
        navBar.add(createNavLabel("Service"));
        navBar.add(createNavLabel("Contact"));

        // === Title ===
        JLabel titleLabel = new JLabel("SMART HOSPITAL FINDER AND MANAGEMENT SYSTEM", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));


        //💬 Tagline
        JLabel taglineLabel = new JLabel("Bridging Patients and Healthcare Digitally.", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Georgia", Font.ITALIC, 22));
        taglineLabel.setForeground(new Color(200, 200, 255));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // WELCOME MESSAGE
        JTextArea welcomeText = new JTextArea(
                "Welcome to the Smart Hospital Finder and Management System — a smart, user-friendly system " +
                "that connects patients, hospitals, and doctors in one place. Easily search hospitals by " +
                "location or specialty, view available doctors, and book appointments from home."
        );

        welcomeText.setFont(new Font("Segoe UI", Font.PLAIN, 20));welcomeText.setForeground(Color.WHITE);
        welcomeText.setOpaque(false);
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);
        welcomeText.setEditable(false);
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText.setMaximumSize(new Dimension(1000, 120));
        welcomeText.setBorder(BorderFactory.createEmptyBorder(20, 100, 60, 100));


        // === Description / Main Statement ===
        JTextArea statementArea = new JTextArea(
                "Administrators and hospital staff can efficiently manage hospital records, doctor details, " + 
                "and patient appointments through a user-friendly interface. The system ensures data security, " + 
                "fast access, and real-time updates, promoting a smoother hospital management process." + 
                 ", "
                        + "\n\nSelect your role to get started:"
                        + "\n• Admin – Manage hospitals, users, and reports"
                        + "\n• Hospital Admin – Manage doctors, patients, and appointments"
                        + "\n• Doctor – View schedules and patient details"
                        + "\n• Patient – Search hospitals and book appointments ");
        statementArea.setLineWrap(true);
        statementArea.setWrapStyleWord(true);
        statementArea.setEditable(false);
        statementArea.setOpaque(false);
        statementArea.setForeground(Color.LIGHT_GRAY);
        statementArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        statementArea.setBorder(BorderFactory.createEmptyBorder(20, 100, 60, 100));

        
        // === Layout ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(navBar, BorderLayout.EAST);
    
        

        // === Combine everything ===
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(Box.createVerticalGlue());
        content.add(topPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(taglineLabel);
        content.add(Box.createVerticalStrut(20));
        content.add(welcomeText);
        content.add(Box.createVerticalStrut(20));
        content.add(statementArea);
        content.add(Box.createVerticalGlue());

        // === Add to main panel ===
        panel.add(navBar, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createImagePanel(String imagePath, SectionData[] sections) {
        JPanel panel = new JPanel(new BorderLayout()) {
            Image bg = new ImageIcon(imagePath).getImage()
                    .getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
                            Toolkit.getDefaultToolkit().getScreenSize().height,
                            Image.SCALE_SMOOTH);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        JPanel sectionContainer = new JPanel();
        sectionContainer.setLayout(new BoxLayout(sectionContainer, BoxLayout.Y_AXIS));
        sectionContainer.setOpaque(false);
        sectionContainer.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        for (int i = 0; i < sections.length; i++) {
            sectionContainer.add(createContentSection(sections[i]));
            if (i < sections.length - 1)
                sectionContainer.add(Box.createVerticalStrut(50));
        }

        contentPanel.add(sectionContainer);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createContentSection(SectionData data) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        JPanel sectionPanel = new JPanel(new BorderLayout(40, 0));
        sectionPanel.setOpaque(false);
        sectionPanel.setPreferredSize(new Dimension(1000, 280));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(data.title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 255, 255));

        JTextArea descArea = new JTextArea(data.description);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setForeground(Color.LIGHT_GRAY);
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        int circleSize = 260;

        JPanel imagePanel = new JPanel() {
            private final ImageIcon gifIcon = new ImageIcon(data.imagePath);

            {
                setPreferredSize(new Dimension(circleSize, circleSize));
                setOpaque(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gifIcon.getImage() == null) return;

                int size = Math.min(getWidth(), getHeight());
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Shape circle = new Ellipse2D.Float(x, y, size, size);
                g2.setClip(circle);

                Image img = gifIcon.getImage();
                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);
                float scale = Math.max((float) size / imgWidth, (float) size / imgHeight);
                int drawWidth = (int) (imgWidth * scale);
                int drawHeight = (int) (imgHeight * scale);
                int drawX = x + (size - drawWidth) / 2;
                int drawY = y + (size - drawHeight) / 2;

                g2.drawImage(img, drawX, drawY, drawWidth, drawHeight, this);
                g2.setClip(null);
                g2.dispose();
            }
        };

        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.setOpaque(false);
        imageWrapper.setPreferredSize(new Dimension(circleSize + 20, circleSize + 20));
        imageWrapper.add(imagePanel);

        if (data.alignment == Alignment.LEFT) {
            textPanel.add(titleLabel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(descArea);
            sectionPanel.add(imageWrapper, BorderLayout.WEST);
            sectionPanel.add(textPanel, BorderLayout.CENTER);
        } else {
            textPanel.add(titleLabel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(descArea);
            JPanel rightAlignWrapper = new JPanel(new BorderLayout());
            rightAlignWrapper.setOpaque(false);
            rightAlignWrapper.add(textPanel, BorderLayout.EAST);
            sectionPanel.add(imageWrapper, BorderLayout.EAST);
            sectionPanel.add(rightAlignWrapper, BorderLayout.CENTER);
        }

        wrapper.add(sectionPanel);
        return wrapper;
    }

    private JPanel createContactPanel() {
        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBackground(Color.DARK_GRAY);
        contactPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));

        JLabel contactTitle = new JLabel("Contact Us");
        contactTitle.setForeground(Color.WHITE);
        contactTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        contactTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel email = new JLabel("Email: support@hospital.com");
        email.setForeground(Color.WHITE);
        email.setFont(new Font("SansSerif", Font.PLAIN, 16));
        email.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel phone = new JLabel("Phone: +91 9876543210");
        phone.setForeground(Color.WHITE);
        phone.setFont(new Font("SansSerif", Font.PLAIN, 16));
        phone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel address = new JLabel("Address: 123 Health Street, City, Country");
        address.setForeground(Color.WHITE);
        address.setFont(new Font("SansSerif", Font.PLAIN, 16));
        address.setAlignmentX(Component.CENTER_ALIGNMENT);

        contactPanel.add(contactTitle);
        contactPanel.add(Box.createVerticalStrut(15));
        contactPanel.add(email);
        contactPanel.add(phone);
        contactPanel.add(address);

        return contactPanel;
    }

    private JLabel createNavLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) { label.setForeground(Color.CYAN); }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) { label.setForeground(Color.WHITE); }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (text.equals("Login")) Login();
                if (text.equals("Register")) {

                    new Register();
                }
                if (text.equals("About Us")) showAboutPage();
                if (text.equals("Service")) scrollToSection(1);  // Service panel index
                if (text.equals("Contact")) scrollToSection(2);  // Contact panel index
            }
        });

        return label;
    }

    private void scrollToSection(int panelIndex) {
        int y = 0;
        for (int i = 0; i < panelIndex; i++) {
            y += mainPanel.getComponent(i).getHeight() + 50;
        }
        final int scrollY = y;
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollY));
    }

    private void Login() {
        try {
            new hospital.ui.Login();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to open Login window.");
        }
    }

    private void showAboutPage() {
        JPanel aboutPage = createAboutPage();
        JFrame aboutFrame = new JFrame("About Us");
        aboutFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        aboutFrame.add(aboutPage);
        aboutFrame.setVisible(true);
    }

    private JPanel createAboutPage() {
        BackgroundPanel page = new BackgroundPanel("src/icons/about.jpg");
        page.setLayout(new BorderLayout());

        // About Content
        JPanel aboutBox = new JPanel();
        aboutBox.setLayout(new BoxLayout(aboutBox, BoxLayout.Y_AXIS));
        aboutBox.setOpaque(false);
        aboutBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutBox.setMaximumSize(new Dimension(700, 400));

        JLabel aboutTitle = new JLabel("About Our Hospital");
        aboutTitle.setFont(new Font("Arial", Font.BOLD, 28));
        aboutTitle.setForeground(Color.WHITE);
        aboutTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea aboutText = new JTextArea(

                "The Smart Hospital Finder and Management System (SHFMS) is a Java Swing–based desktop application "
                +"integrated with a MySQL database. The main objective of this system is to bridge the gap between "
                +"patients and healthcare institutions by offering a simple, interactive, and efficient solution "
                +"for hospital discovery and appointment management.\n"

                +"Key Features:\n"
                +"• Search and locate hospitals based on city or specialization\n"
                +"• Manage hospitals, doctors, and patient records efficiently\n"
                +"• Book and track appointments easily\n"
                +"• Role-based access for Admin, Hospital Admin, Doctor, and Patient\n"
                +"• Data security ensured through authentication and encrypted passwords\n"


                +"\n\nDeveloped as part of a college project to demonstrate the use of object-oriented programming, \n"
                +"database connectivity, and real-world application of software engineering principles."
                        );
        aboutText.setEditable(false);
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        aboutText.setOpaque(false);
        aboutText.setForeground(Color.WHITE);
        aboutText.setFont(new Font("Arial", Font.PLAIN, 18));
        aboutText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = new JButton("⬅ Back to Home");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(new Color(66, 133, 244));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.addActionListener(e -> {
            ((JFrame) SwingUtilities.getWindowAncestor(backBtn)).dispose();
        });

        aboutBox.add(aboutTitle);
        aboutBox.add(Box.createVerticalStrut(20));
        aboutBox.add(aboutText);
        aboutBox.add(Box.createVerticalStrut(30));
        aboutBox.add(backBtn);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);
        centerWrapper.add(Box.createVerticalGlue());
        centerWrapper.add(aboutBox);
        centerWrapper.add(Box.createVerticalGlue());

        page.add(centerWrapper, BorderLayout.CENTER);

        return page;
    }
}
