// // package hospital.ui;

// // import javax.swing.*;
// // import javax.swing.table.*;
// // import java.awt.*;
// // import java.awt.event.*;
// // import java.util.List;         // ⬅️ ADD THIS IMPORT
// // import java.util.ArrayList;    // ⬅️ ADD THIS IMPORT

// // import hospital.dao.DoctorDAO; // ⬅️ ADD THIS IMPORT
// // import hospital.dao.HospitalDAO;
// // import hospital.models.Doctor;   // ⬅️ ADD THIS IMPORT
// // import hospital.models.Hospital;
// // import hospital.models.User;

// // public class DoctorList extends JFrame {
// //     private JTable table;
// //     private DefaultTableModel model;
// //     private JFrame parent;

// //     private DoctorDAO doctorDAO;
// //     private List<Doctor> doctors;

// //     public DoctorList(JFrame parentFrame, int hospitalId) {
// //         this.parent = parentFrame;
// //         this.doctorDAO = new DoctorDAO();
// //         this.doctors = doctorDAO.getDoctorsByHospital(hospitalId); // ✅ fetch specific hospital doctors 
// //             setTitle("Doctors List");
// //         setSize(900, 600);
// //         setLocationRelativeTo(null);
// //         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

// //         JPanel root = new JPanel(new BorderLayout(15, 15));
// //         root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
// //         root.setBackground(new Color(245, 247, 250));
// //         setContentPane(root);

// //         JPanel header = new JPanel(new BorderLayout());
// //         header.setOpaque(false);

// //         JLabel title = new JLabel("🩺 Doctors Available");
// //         title.setFont(new Font("SansSerif", Font.BOLD, 24));
// //         header.add(title, BorderLayout.WEST);

// //         JButton backBtn = new JButton("← Back");
// //         backBtn.setFocusPainted(false);
// //         backBtn.setBackground(new Color(66, 133, 244));
// //         backBtn.setForeground(Color.WHITE);
// //         backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
// //         backBtn.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
// //         backBtn.addActionListener(e -> {
// //             dispose();
// //             parent.setVisible(true);
// //         });
// //         header.add(backBtn, BorderLayout.EAST);

// //         root.add(header, BorderLayout.NORTH);

// //         // Table setup
// //         String[] columns = {"Doctor Name", "Specialization", "Experience", "Contact", "Action"};
// //         model = new DefaultTableModel(columns,0);
// //         table = new JTable(model) {
// //             public boolean isCellEditable(int row, int column) { return column == 4; }
// //             public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
// //                 Component c = super.prepareRenderer(renderer,row,column);
// //                 if(!isRowSelected(row)) {
// //                     c.setBackground(row % 2 == 0 ? new Color(250,250,250) : new Color(230,230,230));
// //                 } else {
// //                     c.setBackground(new Color(180, 215, 255));
// //                 }
// //                 return c;
// //             }
// //         };
// //         table.setRowHeight(35);
// //         table.setFont(new Font("SansSerif", Font.PLAIN, 14));
// //         table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
// //         table.getTableHeader().setBackground(new Color(66,133,244));
// //         table.getTableHeader().setForeground(Color.WHITE);

// //         // // Sample data
// //         // model.addRow(new Object[]{"Dr. Nisha", "Cardiologist", "10 years", "9876543210", "Book Appointment"});
// //         // model.addRow(new Object[]{"Dr. Raj", "Neurologist", "8 years", "9123456780", "Book Appointment"});
// //         loadDoctorData();
// //         table.getColumn("Action").setCellRenderer(new ButtonRenderer());
// //         HospitalDAO hospitals = HospitalDAO.searchHospitals(location, specialty); // Load hospitals first
// //         table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), hospitals));
// //         JScrollPane scroll = new JScrollPane(table);
// //         scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
// //         root.add(scroll, BorderLayout.CENTER);

// //         setVisible(true);
// //     }

// //     private void loadDoctorData() {
        
// //         // Clear existing data (if called multiple times)
// //         model.setRowCount(0); 

// //         if (doctors != null) {
// //             for (Doctor d : doctors) {
// //                 // IMPORTANT: Ensure the column order matches your table setup!
// //                 model.addRow(new Object[]{
// //                     d.getName(), 
// //                     d.getSpecialization(), 
// //                     d.getExperience() + " years", // Assuming getExperience() returns an int
// //                     d.getContact(), 
// //                     "Book Appointment"
// //                 });
// //             }
// //         }
// //         if (model.getRowCount() == 0) {
// //             JOptionPane.showMessageDialog(this, "No doctors are currently available.", "Info", JOptionPane.INFORMATION_MESSAGE);
// //         }
// //     }

// //     class ButtonRenderer extends JButton implements TableCellRenderer {
// //         public ButtonRenderer() { 
// //             setOpaque(true); 
// //             setFocusPainted(false); 
// //             setBackground(new Color(15,157,88)); 
// //             setForeground(Color.WHITE); 
// //             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
// //         }
// //         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
// //             setText((value==null)?"":value.toString());
// //             return this;
// //         }
// //     }

// //     class ButtonEditor extends DefaultCellEditor {
// //         protected JButton button;
// //         private String label;
// //         private boolean clicked;
// //         private List<Hospital> hospitalList;
    
// //         public ButtonEditor(JCheckBox checkBox, List<Hospital> hospitals) {
// //             super(checkBox);
// //             this.hospitalList = hospitals; // ✅ Assign correctly
// //             button = new JButton();
// //             button.setOpaque(true);
// //             button.setBackground(new Color(66,133,244));
// //             button.setForeground(Color.WHITE);
// //             button.setFocusPainted(false);
// //             button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
// //             button.addActionListener(e -> fireEditingStopped());
// //         }
    
// //         public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
// //             label = (value==null)?"":value.toString();
// //             button.setText(label);
// //             clicked = true;
// //             return button;
// //         }
    
// //         public Object getCellEditorValue() {
// //             if(clicked) {
// //                 int row = table.getSelectedRow();
// //                 if (row >= 0 && row < hospitalList.size()) {
// //                     Hospital selectedHospital = hospitalList.get(row);
// //                     DoctorList dl = new DoctorList(parent, selectedHospital.getHospitalId());
// //                     dl.setVisible(true);
// //                     HospitalSearch.this.setVisible(false);
// //                 } else {
// //                     JOptionPane.showMessageDialog(null, "Error: Could not select hospital.", "Error", JOptionPane.ERROR_MESSAGE);
// //                 }
// //             }
// //             clicked = false;
// //             return label;
// //         }
// //     }
// // }    


// package hospital.ui;

// import javax.swing.*;
// import javax.swing.table.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.List;
// import java.util.ArrayList;

// import hospital.dao.DoctorDAO;
// import hospital.dao.HospitalDAO;
// import hospital.models.Doctor;
// import hospital.models.Hospital;

// public class DoctorList extends JFrame {
//     private JTable table;
//     private DefaultTableModel model;
//     private JFrame parent;

//     private DoctorDAO doctorDAO;
//     private List<Doctor> doctors;

//     public DoctorList(JFrame parentFrame, int hospitalId) {
//         this.parent = parentFrame;
//         this.doctorDAO = new DoctorDAO();
//         this.doctors = doctorDAO.getDoctorsByHospital(hospitalId); // ✅ fetch doctors of a hospital

//         setTitle("Doctors List");
//         setSize(900, 600);
//         setLocationRelativeTo(null);
//         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//         JPanel root = new JPanel(new BorderLayout(15, 15));
//         root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//         root.setBackground(new Color(245, 247, 250));
//         setContentPane(root);

//         JPanel header = new JPanel(new BorderLayout());
//         header.setOpaque(false);

//         JLabel title = new JLabel("🩺 Doctors Available");
//         title.setFont(new Font("SansSerif", Font.BOLD, 24));
//         header.add(title, BorderLayout.WEST);

//         JButton backBtn = new JButton("← Back");
//         backBtn.setFocusPainted(false);
//         backBtn.setBackground(new Color(66, 133, 244));
//         backBtn.setForeground(Color.WHITE);
//         backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         backBtn.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
//         backBtn.addActionListener(e -> {
//             dispose();
//             parent.setVisible(true);
//         });
//         header.add(backBtn, BorderLayout.EAST);

//         root.add(header, BorderLayout.NORTH);

//         // Table setup
//         String[] columns = {"Doctor Name", "Specialization", "Experience", "Contact", "Action"};
//         model = new DefaultTableModel(columns, 0);
//         table = new JTable(model) {
//             public boolean isCellEditable(int row, int column) { return column == 4; }
//             public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//                 Component c = super.prepareRenderer(renderer,row,column);
//                 if(!isRowSelected(row)) {
//                     c.setBackground(row % 2 == 0 ? new Color(250,250,250) : new Color(230,230,230));
//                 } else {
//                     c.setBackground(new Color(180, 215, 255));
//                 }
//                 return c;
//             }
//         };

//         table.setRowHeight(35);
//         table.setFont(new Font("SansSerif", Font.PLAIN, 14));
//         table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
//         table.getTableHeader().setBackground(new Color(66,133,244));
//         table.getTableHeader().setForeground(Color.WHITE);

//         loadDoctorData();

//         table.getColumn("Action").setCellRenderer(new ButtonRenderer());
//         table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), doctors));

//         JScrollPane scroll = new JScrollPane(table);
//         scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//         root.add(scroll, BorderLayout.CENTER);

//         setVisible(true);
//     }

//     private void loadDoctorData() {
//         model.setRowCount(0); 

//         if (doctors != null && !doctors.isEmpty()) {
//             for (Doctor d : doctors) {
//                 model.addRow(new Object[]{
//                     d.getName(),
//                     d.getSpecialization(),
//                     d.getExperience() + " years",
//                     d.getContact(),
//                     "Book Appointment"
//                 });
//             }
//         } else {
//             JOptionPane.showMessageDialog(this, "No doctors are currently available.", "Info", JOptionPane.INFORMATION_MESSAGE);
//         }
//     }

//     class ButtonRenderer extends JButton implements TableCellRenderer {
//         public ButtonRenderer() { 
//             setOpaque(true); 
//             setFocusPainted(false); 
//             setBackground(new Color(15,157,88)); 
//             setForeground(Color.WHITE); 
//             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         }
//         public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//             setText((value==null)?"":value.toString());
//             return this;
//         }
//     }

//     class ButtonEditor extends DefaultCellEditor {
//         protected JButton button;
//         private String label;
//         private boolean clicked;
//         private List<Doctor> doctorList;
//         private final int patientUserId = 8; // Replace with logged-in patient ID
    
//         public ButtonEditor(JCheckBox checkBox, List<Doctor> doctors) {
//             super(checkBox);
//             this.doctorList = doctors;
//             button = new JButton();
//             button.setOpaque(true);
//             button.setBackground(new Color(66,133,244));
//             button.setForeground(Color.WHITE);
//             button.setFocusPainted(false);
//             button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//             button.addActionListener(e -> fireEditingStopped());
//         }
    
//         public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//             label = (value==null)?"":value.toString();
//             button.setText(label);
//             clicked = true;
//             return button;
//         }
    
//         public Object getCellEditorValue() {
//             if(clicked) {
//                 int row = table.getSelectedRow();
//                 if (row >= 0 && row < doctorList.size()) {
//                     Doctor selectedDoctor = doctorList.get(row);
    
//                     // ✅ Open Appointment Booking UI
//                     new AppointmentBooking(
//                         patientUserId, 
//                         selectedDoctor.getUserId(), // Assuming Doctor model has userId
//                         selectedDoctor.getName()
//                     ).setVisible(true);
//                 }
//             }
//             clicked = false;
//             return label;
//         }
//     }
// }
    