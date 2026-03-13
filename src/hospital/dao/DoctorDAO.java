
package hospital.dao;

import hospital.models.Doctor;
import hospital.DBConnection;
import hospital.models.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DoctorDAO {

    // ===== 1. Get Doctor Profile =====
    public Doctor getDoctorByUserId(int userId) {
        String sql = "SELECT * FROM doctors WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setUserId(rs.getInt("user_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setContact(rs.getString("contact"));
                d.setEmail(rs.getString("email"));
                d.setHospitalId(rs.getInt("hospital_id"));
                d.setExperience(rs.getInt("experience"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setUserId(rs.getInt("user_id"));
        d.setName(rs.getString("name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setContact(rs.getString("contact"));
        d.setEmail(rs.getString("email"));
        d.setHospitalId(rs.getInt("hospital_id"));
        d.setExperience(rs.getInt("experience"));
        d.setStatus(rs.getString("status")); // <--- Add this
        return d;
    }
    
    // ===== 2. Update Doctor Profile =====
    // public boolean updateDoctorProfile(Doctor doctor) {
    //     String sql = "UPDATE doctors SET name=?, specialization=?, contact=?, email=?, experience=?, status=? WHERE user_id=?";
    //     try (PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setString(1, doctor.getName());
    //         ps.setString(2, doctor.getSpecialization());
    //         ps.setString(3, doctor.getContact());
    //         ps.setString(4, doctor.getEmail());
    //         ps.setString(5, doctor.getExperience());
    //         ps.setString(6, doctor.getStatus()); 
    //         ps.setInt(7, doctor.getUserId());
    //         int rows = ps.executeUpdate();
    //         return rows > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return false;
    // }

    // ===== 3. Get Appointments for Doctor =====
    public List<Appointment> getAppointmentsForDoctor(int userId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name FROM appointments a " +
                     "JOIN patients p ON a.patient_user_id = p.user_id " +
                     "WHERE a.doctor_user_id=? ORDER BY a.appointment_date DESC";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientUserId(rs.getInt("patient_user_id"));
                a.setDoctorUserId(rs.getInt("doctor_user_id"));
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


    // ===== 5. Get Appointment Counts for Dashboard =====
    public int getAppointmentCount(int doctorUserId, String date, String status) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_user_id=? AND DATE(appointment_date)=?";
        
        if (!"ALL".equals(status)) {
            sql += " AND status=?";
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorUserId);
            ps.setString(2, date); // Date should be in 'YYYY-MM-DD' format
            
            if (!"ALL".equals(status)) {
                ps.setString(3, status);
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching appointment count for doctor " + doctorUserId);
            e.printStackTrace();
        }
        return count;
    }
    // ===== 4. Update Appointment Status =====
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status=? WHERE appointment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = new ArrayList<>();
        // Note: Using LIKE for partial matches is useful, but an exact match ('=') 
        // may be more appropriate depending on your database schema consistency.
        String sql = "SELECT user_id, name, specialization, experience, contact FROM doctors WHERE specialization = ?"; 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Set the specialization parameter
            ps.setString(1, specialization);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor d = new Doctor();
                    d.setUserId(rs.getInt("user_id"));
                    d.setName(rs.getString("name"));
                    d.setSpecialization(rs.getString("specialization"));
                    d.setExperience(rs.getInt("experience"));
                    d.setContact(rs.getString("contact"));
                    doctors.add(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctors by specialization: " + specialization);
            e.printStackTrace();
        }
        return doctors;
    }

    // Count doctors
    public int getDoctorCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM doctors";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);

            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // ===== Get all doctors =====
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);
                while (rs.next()) {
                    list.add(mapResultSetToDoctor(rs));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Doctor> getAllActiveDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE status='ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                list.add(mapResultSetToDoctor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    
    // ===== Search doctors =====
    public List<Doctor> searchDoctors(String query) {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE (name LIKE ? OR specialization LIKE ?) AND status='ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + query + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setUserId(rs.getInt("user_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setContact(rs.getString("contact"));
                d.setEmail(rs.getString("email"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== Add doctor =====
    public int addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, contact, email, specialization, experience) VALUES (?,?,?,?,?)";
        int doctorId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getContact());
            ps.setString(3, doctor.getEmail());
            ps.setString(4, doctor.getSpecialization());
            ps.setInt(5, doctor.getExperience());
    
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) doctorId = rs.getInt(1);
    
        } catch (SQLException e) { e.printStackTrace(); }
        return doctorId;
    }
    

    // ===== Update doctor =====
    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctors SET name=?, specialization=?, contact=?, email=?, experience=?, status=? WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, doctor.getName());
                ps.setString(2, doctor.getSpecialization());
                ps.setString(3, doctor.getContact());
                ps.setString(4, doctor.getEmail());
                ps.setInt(5, doctor.getExperience());
                ps.setString(6, doctor.getStatus());
                ps.setInt(7, doctor.getDoctorId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== Delete doctor =====
    public static boolean deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // public static boolean deactivateDoctor(int doctorId) {
    //     String sql = "UPDATE doctors SET status='INACTIVE' WHERE doctor_id=?";
    //     try (Connection conn = DBConnection.getConnection();
    //          PreparedStatement ps = conn.prepareStatement(sql)) {
    //         ps.setInt(1, doctorId);
    //         return ps.executeUpdate() > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    // }
    

    // // Method to get next available doctor_id
    public int getNextDoctorIds() {
        String sql = "SELECT MAX(doctor_id) AS max_id FROM doctors";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return maxId + 1; // next available ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // start with 1 if table empty
    }


    public List<Doctor> getDoctorsByHospital(int hospitalId) {
        List<Doctor> doctors = new ArrayList<>();

            String sql = "SELECT * FROM doctors WHERE hospital_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hospitalId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setEmail(rs.getString("email"));
                doctors.add(d);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return doctors;
    }
    
}


