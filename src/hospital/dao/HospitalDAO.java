// package hospital.dao;

// import hospital.models.Hospital;

// import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;


// public class HospitalDAO {

//     private static Connection conn;

//     public HospitalDAO() {
//         conn = DBConnection.getConnection();
//     }

//     // Add new hospital
//     public static boolean addHospital(Hospital hospital) {
//         String sql = "INSERT INTO hospitals (name, address, contact, speacializations) VALUES (?, ?, ?, ?)";
//         try (
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setString(1, hospital.getName());
//             ps.setString(2, hospital.getAddress());
//             ps.setString(3, hospital.getContact());
//            // ps.setString(4, hospital.getEmail());

//             return ps.executeUpdate() > 0;
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }

//     // Update hospital
//     public static boolean updateHospital(Hospital hospital) {
//         String sql = "UPDATE hospitals SET name=?, address=?, contact=? WHERE hospital_id=?";
//         try (
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setString(1, hospital.getName());
//             ps.setString(2, hospital.getAddress());
//             ps.setString(3, hospital.getContact());
//             ps.setInt(4, hospital.getId());
//             //ps.setString(4, hospital.getEmail());

//             return ps.executeUpdate() > 0;
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }

//     // Delete hospital
//     public static boolean deleteHospital(int id) {
//         String sql = "DELETE FROM hospitals WHERE hospital_id=?";
//         try (
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setInt(1, id);
//             return ps.executeUpdate() > 0;
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return false;
//         }
//     }

//     // Get single hospital
//     public static Hospital getHospital(int id) {
//         String sql = "SELECT * FROM hospitals WHERE hospital_id=?";
//         try (
//              PreparedStatement ps = conn.prepareStatement(sql)) {

//             ps.setInt(1, id);
//             ResultSet rs = ps.executeQuery();
//             if (rs.next()) {
//                 return new Hospital(
//                         rs.getInt("hospital_id"),
//                         rs.getString("name"),
//                         rs.getString("address"),
//                         rs.getString("contact")
//                         //rs.getString("email")
//                 );
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return null;
//     }

//     // Get all hospitals
//     public static List<Hospital> getAllHospitals() {
//         List<Hospital> list = new ArrayList<>();
//         String sql = "SELECT * FROM hospitals";
//         try (
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery(sql)) {

//             while (rs.next()) {
//                 list.add(new Hospital(
//                         rs.getInt("hospital_id"),
//                         rs.getString("name"),
//                         rs.getString("address"),
//                         rs.getString("contact")
//                         //rs.getString("email")
//                     ));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return list;
//     }

//     // Count hospitals (for Admin Dashboard)
//     public static int getHospitalCount() {
//         int count = 0;
//         String sql = "SELECT COUNT(*) FROM hospitals";
//         try (
//              Statement st = conn.createStatement();
//              ResultSet rs = st.executeQuery(sql)) {

//             if (rs.next()) count = rs.getInt(1);
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return count;
//     }
    

// }


package hospital.dao;

import hospital.DBConnection;
import hospital.models.Doctor;
import hospital.models.Hospital;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDAO {

    // Search hospitals with optional location and specialization filters
    public List<hospital.models.Hospital> searchHospitals(String locationFilter, String specialtyFilter) {
        List<hospital.models.Hospital> hospitals = new ArrayList<>();
            String sql = "SELECT * FROM hospitals WHERE 1=1";
            if(locationFilter != null) sql += " AND location LIKE ?";
            if(specialtyFilter != null) sql += " AND hospital_id IN (SELECT hospital_id FROM doctors WHERE specialization LIKE ?)";
            try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
                int index = 1;
                if(locationFilter != null) ps.setString(index++, "%" + locationFilter + "%");
                if(specialtyFilter != null) ps.setString(index++, "%" + specialtyFilter + "%");
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    hospital.models.Hospital h = new hospital.models.Hospital();
                    h.setId(rs.getInt("hospital_id"));
                    h.setName(rs.getString("name"));
                    h.setAddress(rs.getString("address"));
                    h.setContact(rs.getString("contact"));
                    hospitals.add(h);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return hospitals;
    }

    // ✅ Get comma-separated specializations for a hospital
    public String getHospitalSpecializations(int hospitalId) {
        StringBuilder specs = new StringBuilder();
            String sql = "SELECT DISTINCT specialization FROM doctors WHERE hospital_id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, hospitalId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(specs.length() > 0) specs.append(", ");
                specs.append(rs.getString("specialization"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return specs.toString();
    }
    // 1️⃣ Add new hospital
    public boolean addHospital(Hospital hospital) {
        String sql = "INSERT INTO hospitals (name, address, contact, speacializations) VALUES (?, ?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, hospital.getName());
            ps.setString(2, hospital.getAddress());
            ps.setString(3, hospital.getContact());
            ps.setString(4, "General"); // placeholder for specialization
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2️⃣ Update hospital
    public boolean updateHospital(Hospital hospital) {
        String sql = "UPDATE hospitals SET name=?, address=?, contact=? WHERE hospital_id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, hospital.getName());
            ps.setString(2, hospital.getAddress());
            ps.setString(3, hospital.getContact());
            ps.setInt(4, hospital.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3️⃣ Delete hospital
    public boolean deleteHospital(int id) {
        String sql = "DELETE FROM hospitals WHERE hospital_id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4️⃣ Get single hospital
    public Hospital getHospital(int id) {
        String sql = "SELECT * FROM hospitals WHERE hospital_id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Hospital(
                    rs.getInt("hospital_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("contact"),
                    rs.getString("specialization")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5️⃣ Get all hospitals
    public List<Hospital> getAllHospitals() {
        List<Hospital> list = new ArrayList<>();
        String sql = "SELECT * FROM hospitals";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql); ){
            while (rs.next()) {
                Hospital h = new Hospital();
                h.setId(rs.getInt("hospital_id"));
                h.setName(rs.getString("name"));
                h.setAddress(rs.getString("address"));
                h.setContact(rs.getString("contact"));
                h.setSpecialization(rs.getString("specialization"));
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Hospital> getHospitalsByFilter(String location, String specialization) {
        List<Hospital> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM hospitals WHERE 1=1");
        if (location != null) sql.append(" AND address LIKE ?");
        if (specialization != null) sql.append(" AND specialization LIKE ?");
    
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int i = 1;
            if (location != null) ps.setString(i++, "%" + location + "%");
            if (specialization != null) ps.setString(i++, "%" + specialization + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hospital h = new Hospital();
                h.setId(rs.getInt("hospital_id"));
                h.setName(rs.getString("name"));
                h.setAddress(rs.getString("address"));
                h.setContact(rs.getString("contact"));
                h.setSpecialization(rs.getString("specialization"));
                list.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // 6️⃣ Count hospitals (for Admin Dashboard)
    public int getHospitalCount() {
        String sql = "SELECT COUNT(*) FROM hospitals";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery(sql); 
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Doctor> getDoctorsByHospitalId(int hospitalId) {
        List<Doctor> list = new ArrayList<>();

        String sql = """
                SELECT d.doctor_id, d.name, d.specialization, d.email, d.phone, d.experience
                FROM doctor d
                WHERE d.hospital_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hospitalId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Doctor doc = new Doctor();
                doc.setDoctorId(rs.getInt("doctor_id"));
                doc.setName(rs.getString("name"));
                doc.setSpecialization(rs.getString("specialization"));
                doc.setEmail(rs.getString("email"));
                doc.setContact(rs.getString("phone"));
                doc.setExperience(rs.getInt("experience"));
                list.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching doctors for hospital ID " + hospitalId);
            e.printStackTrace();
        }

        return list;
}

}

