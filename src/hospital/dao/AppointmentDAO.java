package hospital.dao;

import hospital.DBConnection;
import hospital.models.Appointment;
import hospital.models.Doctor;
import hospital.models.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {


    // ===== 1. Get All Appointments =====
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
             "FROM appointments a " +
             "JOIN patients p ON a.patient_user_id = p.user_id " +
             "LEFT JOIN doctors d ON a.doctor_user_id = d.user_id " +
             "ORDER BY a.appointment_date DESC";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = mapResultSetToAppointment(rs);
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Update appointment status
    // public boolean updateAppointmentStatus(int appointmentId, String status) {
    //     String sql = "UPDATE appointments SET status=? WHERE appointment_id=?";
    //     try (Connection conn = DBConnection.getConnection();
    //          PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setString(1, status);
    //         ps.setInt(2, appointmentId);
    //         int rows = ps.executeUpdate();
    //         return rows > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return false;
    // }
    
    // ===== 2. Get Appointments by Status =====
    public List<Appointment> getAppointmentsByStatus(String status) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
             "FROM appointments a " +
             "JOIN patients p ON a.patient_user_id = p.user_id " +
             "LEFT JOIN doctors d ON a.doctor_user_id = d.user_id " +
             "WHERE a.status=? ORDER BY a.appointment_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = mapResultSetToAppointment(rs);
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== 3. Delete Appointment (Admin Function) =====
    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Count Appointments
    public int getAppointmentCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM appointments";

        // Open connection here
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) count = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setPatientUserId(rs.getInt("patient_user_id"));
        a.setDoctorUserId(rs.getInt("doctor_user_id"));
        try{a.setPatientName(rs.getString("patient_name"));}catch(Exception ignored){}
        try{a.setDoctorName(rs.getString("doctor_name"));}catch(Exception ignored){}
        a.setDate(rs.getDate("appointment_date"));
        a.setTime(rs.getTime("appointment_time"));
        a.setStatus(rs.getString("status"));
        try{a.setNotes(rs.getString("notes"));}catch(Exception ignored){}
        return a;
    }
    

    // ===== NEW: Book Appointment Logic (Core update) =====
    public boolean bookAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (patient_user_id, doctor_user_id, appointment_date, appointment_time, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Note: Appointment model needs fields for notes and status
            ps.setInt(1, appt.getPatientUserId());
            ps.setInt(2, appt.getDoctorUserId());
            ps.setDate(3, appt.getDate());
            ps.setTime(4, appt.getTime());
            ps.setString(5, appt.getStatus()); // 'Scheduled' or similar
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== NEW: Get Appointments by Patient ID with Filter (for UI) =====
    // public List<Appointment> getAppointmentsByPatientId(int patientUserId, String filter) {
    //     List<Appointment> list = new ArrayList<>();
    //     LocalDate today = LocalDate.now();
    //     String sql = 
    //          "SELECT a.*, p.name AS patient_name, d.name AS doctor_name, h.name AS hospital_name " +
    //          "FROM appointments a " +
    //          "JOIN patients p ON a.patient_user_id = p.user_id " +
    //          "LEFT JOIN doctors d ON a.doctor_user_id = d.user_id " +
    //          // Assuming doctors table has hospital_id, and hospital table has name
    //          "LEFT JOIN hospitals h ON d.hospital_id = h.hospital_id " +
    //          "WHERE a.patient_user_id=? "; 

    //     // Apply filter logic based on the UI selection (Upcoming/Past)
    //     if ("Upcoming".equalsIgnoreCase(filter)) {
    //          sql += "AND a.appointment_date > ? AND a.status IN ('Scheduled', 'Confirmed', 'Pending') ";
    //     } else if ("Past".equalsIgnoreCase(filter)) {
    //          sql += "AND (a.appointment_date <= ? OR a.status IN ('Completed', 'Cancelled')) ";
    //     } 
    //     // Note: 'All' filter doesn't add date/status restriction

    //     sql += "ORDER BY a.appointment_date DESC, a.appointment_time DESC";

    //     try (Connection conn = DBConnection.getConnection();
    //          PreparedStatement ps = conn.prepareStatement(sql)) {

    //         ps.setInt(1, patientUserId);
            
    //         // Set date parameter if filtering by Upcoming or Past
    //         if (!"All".equalsIgnoreCase(filter)) {
    //              ps.setDate(2, Date.valueOf(today));
    //         }
            
    //         ResultSet rs = ps.executeQuery();
    //         while (rs.next()) {
    //             Appointment a = mapResultSetToAppointment(rs);
    //             // Set the hospital name (if the JOIN worked)
    //             try { a.setHospitalName(rs.getString("hospital_name")); } catch(Exception ignored){}
    //             list.add(a);
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return list;
    // }

    public List<Appointment> getAppointmentsByDoctorId(int doctorUserId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, u.username AS patient_name FROM appointments a " +
             "JOIN users u ON a.patient_user_id = u.user_id " +
             "WHERE a.doctor_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, doctorUserId);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientName(rs.getString("patient_name"));
                a.setDate(rs.getDate("appointment_date"));
                a.setTime(rs.getTime("appointment_time"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<Patient> getPatientHistoryByDoctor(Doctor doctor) {
        List<Patient> list = new ArrayList<>();
        String sql = """
            SELECT p.user_id, p.name, 
                   MAX(a.appointment_date) AS last_visit, 
                   COUNT(*) AS total_visits
            FROM appointments a
            JOIN patients p ON a.patient_user_id = p.user_id
            WHERE a.doctor_user_id = ?
            GROUP BY p.user_id, p.name
            ORDER BY last_visit DESC
        """;
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, doctor.getUserId());  // ✅ FIXED — get doctor ID properly
    
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Patient p = new Patient();
                p.setUserId(rs.getInt("user_id"));
                p.setName(rs.getString("name"));
                p.setLastAppointmentDate(rs.getDate("last_visit" ));
                p.setTotalVisits(rs.getInt("total_visits"));
                list.add(p);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return list;
    }


    public static List<String> getAppointmentsByPatientAndDoctor(String patientId, int doctorId) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT date, status FROM appointments WHERE patient_id = ? AND doctor_id = ? ORDER BY date DESC";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setString(1, patientId);
            stmt.setInt(2, doctorId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String date = rs.getString("date");
                String status = rs.getString("status");
                history.add(date + " : " + status);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return history;
    }
    
    public List<Appointment> getAppointmentsByPatientId(int patientId, String filter) {
        List<Appointment> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
    
        String sql = "SELECT a.*, d.name AS doctor_name, h.name AS hospital_name " +
                     "FROM appointments a " +
                     "LEFT JOIN doctors d ON a.doctor_user_id = d.user_id " +
                     "LEFT JOIN hospitals h ON d.hospital_id = h.hospital_id " +
                     "WHERE a.patient_user_id = ? ";
    
        // Filter by status or date
        if ("Upcoming".equalsIgnoreCase(filter)) {
            sql += "AND a.appointment_date >= ? AND a.status IN ('Scheduled','Confirmed') ";
        } else if ("Past".equalsIgnoreCase(filter)) {
            sql += "AND (a.appointment_date < ? OR a.status IN ('Completed','Cancelled')) ";
        } // "All" does not require additional filter
    
        sql += "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            int paramIndex = 1;
            ps.setInt(paramIndex++, patientId);
    
            if ("Upcoming".equalsIgnoreCase(filter) || "Past".equalsIgnoreCase(filter)) {
                ps.setDate(paramIndex++, Date.valueOf(today));
            }
    
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientUserId(rs.getInt("patient_user_id"));
                a.setDoctorUserId(rs.getInt("doctor_user_id"));
                a.setDoctorName(rs.getString("doctor_name"));
                a.setHospitalName(rs.getString("hospital_name"));
                a.setDate(rs.getDate("appointment_date"));
                a.setTime(rs.getTime("appointment_time"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return list;
    }
    
    
    

    // =========================
    // 2. Update appointment status (cancel)
    // =========================
    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    


    
    
    }





