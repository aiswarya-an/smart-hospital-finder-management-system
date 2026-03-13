
package hospital.dao;

import hospital.DBConnection;
import hospital.models.Admin;
import hospital.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    
    // ===== 1. Get Admin Profile =====
    public Admin getAdminByUserId(int userId) {
        String sql = "SELECT * FROM admins WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Admin a = new Admin();
                a.setAdminId(rs.getInt("admin_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setName(rs.getString("name"));
                a.setContact(rs.getString("contact"));
                a.setEmail(rs.getString("email"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                return a;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== 2. Update Admin Profile =====
    public boolean updateAdminProfile(Admin admin) {
        String sql = "UPDATE admins SET name=?, contact=?, email=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getName());
            ps.setString(2, admin.getContact());
            ps.setString(3, admin.getEmail());
            ps.setInt(4, admin.getUserId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== 3. Get All Users =====
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, role, contact, created_at FROM users ORDER BY user_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));
                u.setContact(rs.getString("contact"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== 4. Delete User =====
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===== 5. Get All Admins =====
public List<Admin> getAllAdmins() {
    List<Admin> admins = new ArrayList<>();
    String sql = "SELECT a.admin_id, a.user_id, u.username, a.name, a.contact, a.email, a.created_at " +
                 "FROM admins a " +
                 "JOIN users u ON a.user_id = u.user_id " +
                 "ORDER BY a.admin_id ASC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Admin a = new Admin();
            a.setAdminId(rs.getInt("admin_id"));
            a.setUserId(rs.getInt("user_id"));
            a.setName(rs.getString("name"));
            a.setContact(rs.getString("contact"));
            a.setEmail(rs.getString("email"));
            a.setCreatedAt(rs.getTimestamp("created_at"));
            admins.add(a);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return admins;
}

}
