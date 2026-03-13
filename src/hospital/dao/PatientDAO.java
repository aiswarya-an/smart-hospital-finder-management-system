
package hospital.dao;

import hospital.models.Patient;
import hospital.DBConnection;
import hospital.models.Appointment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public int getAppointmentCount(int patientUserId, String type) {
        int count = 0;
        String dateComparison;
        String statusFilter = "";
        
        // UPCOMING: Date > TODAY and Status NOT Cancelled/Completed
        if ("UPCOMING".equalsIgnoreCase(type)) {
            dateComparison = "a.appointment_date > ?";
            statusFilter = " AND a.status IN ('Scheduled', 'Confirmed')";
        } 
        // PAST: Date <= TODAY or Status is Completed/Cancelled (even if date is future, for history)
        else if ("PAST".equalsIgnoreCase(type)) {
            // Count all completed/cancelled or those in the past regardless of status
            dateComparison = "(a.appointment_date <= ? OR a.status IN ('Completed', 'Cancelled'))";
            // Exclude future appointments that are NOT completed/cancelled to prevent double counting
            statusFilter = " AND NOT (a.appointment_date > ? AND a.status IN ('Scheduled', 'Confirmed'))";
        } else {
            return 0; // Invalid type
        }

        String sql = "SELECT COUNT(*) FROM appointments a WHERE a.patient_user_id=? AND " + dateComparison + statusFilter;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String todayDate = LocalDate.now().toString(); // YYYY-MM-DD
            
            if ("UPCOMING".equalsIgnoreCase(type)) {
                ps.setInt(1, patientUserId);
                ps.setString(2, todayDate);
            } else if ("PAST".equalsIgnoreCase(type)) {
                ps.setInt(1, patientUserId);
                ps.setString(2, todayDate);
                ps.setString(3, todayDate); // for the second condition in statusFilter
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointment count for patient " + patientUserId);
            e.printStackTrace();
        }
        return count;
    }

    // ===== 1. Get Patient Profile (UPDATED to use fresh connection) =====
    public Patient getPatientByUserId(int userId) {
        String sql = 
            "SELECT p.*, " +
            "      a.appointment_date AS last_date, " +
            "      d.name AS doctor_name " +
            "FROM patients p " +
            "LEFT JOIN (" +
            "    SELECT a1.patient_user_id, a1.appointment_date, a1.doctor_user_id " +
            "    FROM appointments a1 " +
            "    INNER JOIN (" +
            "        SELECT patient_user_id, MAX(appointment_date) AS max_date " +
            "        FROM appointments WHERE status='Completed' " + // Only consider Completed appointments for 'last' visit
            "        GROUP BY patient_user_id" +
            "    ) AS sub ON a1.patient_user_id = sub.patient_user_id AND a1.appointment_date = sub.max_date" +
            ") AS a ON a.patient_user_id = p.user_id " +
            "LEFT JOIN doctors d ON a.doctor_user_id = d.user_id " +
            "WHERE p.user_id = ?";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setContact(rs.getString("contact"));
                p.setEmail(rs.getString("email"));
                p.setAddress(rs.getString("address"));
                p.setDob(rs.getDate("dob"));
                
                // These might be null if no appointment exists
                p.setLastAppointmentDate(rs.getDate("last_date"));
                p.setLastDoctorName(rs.getString("doctor_name"));
                
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ===================================
    // ===== 2. UPDATE Patient Profile =====
    // ===================================
    /**
     * Updates the editable fields of a patient's profile in the 'patients' table.
     * @param patient The Patient object containing the updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updatePatientProfile(Patient patient) {
        // We assume 'name', 'contact', 'email', and 'address' are the editable fields
        String sql = "UPDATE patients SET name = ?, contact = ?, email = ?, address = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, patient.getName());
            ps.setString(2, patient.getContact());
            ps.setString(3, patient.getEmail());
            ps.setString(4, patient.getAddress());
            ps.setInt(5, patient.getUserId()); // Use user_id for the WHERE clause
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating profile for user " + patient.getUserId());
            e.printStackTrace();
            return false;
        }
        
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT patient_id, name, age, contact FROM patients";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery() ; 

            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setContact(rs.getString("contact"));
                patients.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    // ===== 3. Get Appointments for a Patient =====
    public List<Appointment> getAppointmentsByPatient(int userId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.name as doctor_name FROM appointments a " +
                     "JOIN patients d ON a.doctor_user_id = d.user_id " + // ASSUMPTION: Doctor user_id is linked to patients table? This is likely a mistake and should join to a 'doctors' or 'users' table. Assuming 'users' for now based on your old commented out code.
                     "WHERE a.patient_user_id=? ORDER BY a.appointment_date DESC";
                     
        // Corrected SQL (assuming 'doctors' table or 'users' table holds doctor names)
        String correctSql = "SELECT a.*, u.name as doctor_name FROM appointments a " +
                            "JOIN users u ON a.doctor_user_id = u.user_id " +
                            "WHERE a.patient_user_id=? ORDER BY a.appointment_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(correctSql)) {
            
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentId(rs.getInt("appointment_id"));
                    a.setPatientUserId(rs.getInt("patient_user_id"));
                    a.setDoctorUserId(rs.getInt("doctor_user_id"));
                    a.setDoctorName(rs.getString("doctor_name"));
                    a.setDate(rs.getDate("appointment_date"));
                    a.setTime(rs.getTime("appointment_time"));
                    a.setStatus(rs.getString("status"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== 4. Book Appointment =====
    public boolean bookAppointment(Appointment appt) {
        String sql = "INSERT INTO appointments (patient_user_id, doctor_user_id, appointment_date, appointment_time, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appt.getPatientUserId());
            ps.setInt(2, appt.getDoctorUserId());
            ps.setDate(3, new java.sql.Date(appt.getDate().getTime()));
            ps.setTime(4, new java.sql.Time(appt.getTime().getTime()));
            ps.setString(5, appt.getStatus());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== 5. Cancel Appointment =====
    public boolean cancelAppointment(int appointmentId) {
        String sql = "UPDATE appointments SET status='Cancelled' WHERE appointment_id=?";
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

    // ===== 6. Add Patient (Registration) =====
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (user_id, name, age, gender, contact, email, address, dob) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patient.getUserId());
            ps.setString(2, patient.getName());
            ps.setInt(3, patient.getAge());
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getContact());
            ps.setString(6, patient.getEmail());
            ps.setString(7, patient.getAddress());
            ps.setDate(8, patient.getDob());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== 7. Count Patients =====
    public int getPatientCount() {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}