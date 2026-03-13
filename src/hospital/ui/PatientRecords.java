package hospital.ui;

import javax.swing.*;
import javax.swing.table.*;

import hospital.dao.PatientDAO;
import hospital.models.Patient;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import java.util.List;

public class PatientRecords extends JFrame {
    private JFrame parent;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public PatientRecords(JFrame parentFrame) {
        this.parent = parentFrame;
        setTitle("Patient Records");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if(parent != null) {
            parent.setVisible(false);
        }
        

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 248, 250));
        setContentPane(root);

        // ===== HEADER (Top Bar) =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(22, 160, 133));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton backButton = new JButton("⬅ Back");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setOpaque(true);
        backButton.setForeground(new Color(22, 160, 133));
        backButton.setBackground(Color.white);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            if(parent != null) {
                parent.setVisible(true);
            }
        });
        

        JLabel titleLabel = new JLabel("Patient Records");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        root.add(headerPanel, BorderLayout.NORTH);

        // ===== SEARCH BAR =====
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        searchPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setToolTipText("Search by name or contact...");
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(22, 160, 133));
        searchBtn.setForeground(Color.white);
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> runFilter());
        searchPanel.add(searchBtn, BorderLayout.EAST);

        root.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        // ===== HEADER + SEARCH WRAPPER =====
        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.add(headerPanel);
        topWrapper.add(searchPanel);

        root.add(topWrapper, BorderLayout.NORTH);


        // ===== TABLE DATA =====
        String[] cols = {"Patient ID", "Name", "Age", "Contact", "Action"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 4; // only the Action (View) column is clickable
            }
        };

        // Sample rows
        // model.addRow(new Object[]{"P001", "Kiran Acharya", "28", "+91 9876543215", "02-Oct-2025", "View"});
        // model.addRow(new Object[]{"P002", "Sandeep Hegde", "35", "+91 9870001112", "15-Sep-2025", "View"});
        // model.addRow(new Object[]{"P003", "Mary Jane", "42", "+91 9998887776", "10-Sep-2025", "View"});

        table = new JTable(model);
        table.setRowHeight(38);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));
        table.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        // Table header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(22, 160, 133));
        header.setForeground(Color.white);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Alternate row color
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected)
                    c.setBackground(row % 2 == 0 ? Color.white : new Color(245, 250, 255));
                else
                    c.setBackground(new Color(220, 235, 255));
                setHorizontalAlignment(column == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);
                return c;
            }
        });

        // ===== ADD BUTTON IN LAST COLUMN =====
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), this));
        // Example pseudo-code
        PatientDAO dao = new PatientDAO();
        List<Patient> patients = dao.getAllPatients();
        for(Patient p : patients){
            model.addRow(new Object[]{p.getPatientId(), p.getName(), p.getAge(), p.getContact(), "View"});
        }

        // table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        // table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), this));
        

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sp.getViewport().setBackground(Color.white);

        root.add(sp, BorderLayout.CENTER);

        setVisible(true);
    }

    private void runFilter() {
        String q = searchField.getText().trim().toLowerCase();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        table.setRowSorter(trs);
        if (q.isEmpty()) {
            trs.setRowFilter(null);
        } else {
            trs.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(q)));
        }
    }

    // ===== Button Renderer =====
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(22, 160, 133));
            setForeground(Color.white);
            setFocusPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // ===== Button Editor (Action) =====
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private JTable tableRef;
        private JFrame frameRef;

        public ButtonEditor(JCheckBox checkBox, JFrame frame) {
            super(checkBox);
            this.frameRef = frame;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(22, 160, 133));
            button.setForeground(Color.white);
            button.setFocusPainted(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 13));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int col) {
            this.tableRef = table;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        
public Object getCellEditorValue() {
    if (clicked && tableRef != null) {
        int viewRow = tableRef.getSelectedRow(); // row as seen in table
        if (viewRow != -1) {
            int modelRow = tableRef.convertRowIndexToModel(viewRow); // actual row in model

            // Fetch patient details from table model
            String patientId = tableRef.getValueAt(modelRow, 0).toString();
            String name = tableRef.getValueAt(modelRow, 1).toString();
            String age = tableRef.getValueAt(modelRow, 2).toString();
            String contact = tableRef.getValueAt(modelRow, 3).toString();

            // Show patient details in a JDialog
            JDialog detailDialog = new JDialog(frameRef, "Patient Details", true);
            detailDialog.setSize(400, 300);
            detailDialog.setLocationRelativeTo(frameRef);
            detailDialog.setLayout(new BorderLayout());

            JPanel content = new JPanel(new GridLayout(0,1,10,10));
            content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

            content.add(new JLabel("Patient ID: " + patientId));
            content.add(new JLabel("Name: " + name));
            content.add(new JLabel("Age: " + age));
            content.add(new JLabel("Contact: " + contact));

            // You can add more details here if available in model or DAO

            detailDialog.add(content, BorderLayout.CENTER);

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(ev -> detailDialog.dispose());
            JPanel btnPanel = new JPanel();
            btnPanel.add(closeBtn);
            detailDialog.add(btnPanel, BorderLayout.SOUTH);

            detailDialog.setVisible(true);
        }
    }
    clicked = false;
    return label;
}

        
        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Main Dashboard");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        SwingUtilities.invokeLater(() -> new PatientRecords(mainFrame));
    }
}
