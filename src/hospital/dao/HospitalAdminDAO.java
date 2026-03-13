package hospital.dao;

import hospital.DBConnection;
import hospital.models.HospitalAdmin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalAdminDAO {


    // 1️⃣ Add new hospital admin (status = PENDING by default)
    public boolean addHospitalAdmin(HospitalAdmin admin) {
        String sql = "INSERT INTO hospital_admins (user_id, name, email, contact, hospital_id, status) " +
                     "VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, admin.getUserId());
            ps.setString(2, admin.getName());
            ps.setString(3, admin.getEmail());
            ps.setString(4, admin.getContact());
            ps.setInt(5, admin.getHospitalId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2️⃣ Get Hospital Admin by UserId
    public HospitalAdmin getByUserId(int admin) {
        String sql = "SELECT * FROM hospital_admins WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, admin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToAdmin(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3️⃣ Approve Admin (set status = ACTIVE)
    public static boolean approveAdmin(int id) {
        return updateStatus(id, "ACTIVE");
    }

    // 4️⃣ Reject Admin (set status = REJECTED)
    public boolean rejectAdmin(int id) {
        return updateStatus(id, "REJECTED");
    }

    // Helper to update status
    private static boolean updateStatus(int id, String status) {
        String sql = "UPDATE hospital_admins SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5️⃣ Delete Admin
    public static boolean deleteAdmin(int id) {
        String sql = "DELETE FROM hospital_admins WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6️⃣ Get all Admins
    public List<HospitalAdmin> getAllAdmins() {
        List<HospitalAdmin> list = new ArrayList<>();
        String sql = "SELECT * FROM hospital_admins";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                list.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 7️⃣ Get Pending Admins
    public List<HospitalAdmin> getPendingAdmins() {
        List<HospitalAdmin> list = new ArrayList<>();
        String sql = "SELECT * FROM hospital_admins WHERE status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) list.add(mapResultSetToAdmin(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 8️⃣ Count Admins
    public int getAdminCount() {
        String sql = "SELECT COUNT(*) FROM hospital_admins";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery(sql);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 9️⃣ Map ResultSet to HospitalAdmin object
    private static HospitalAdmin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        HospitalAdmin admin = new HospitalAdmin();
        admin.setId(rs.getInt("id"));
        admin.setUserId(rs.getInt("user_id"));
        admin.setName(rs.getString("name"));
        admin.setEmail(rs.getString("email"));
        admin.setContact(rs.getString("contact"));
        admin.setHospitalId(rs.getInt("hospital_id"));
        admin.setStatus(rs.getString("status")); // store as String
        return admin;
    }

    // 🔟 Get Hospital Admin by Username (JOIN with users table)
    public  HospitalAdmin getByUsername(String username) {
    String sql = """
        SELECT ha.*
        FROM hospital_admins ha
        JOIN users u ON ha.user_id = u.user_id
        WHERE u.username = ?
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapResultSetToAdmin(rs);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

}
