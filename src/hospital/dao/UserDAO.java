
package hospital.dao;

import hospital.DBConnection;
import hospital.models.Doctor;
import hospital.models.HospitalAdmin;
import hospital.models.User;
import hospital.models.UserWithHospital;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // ===== 1. Validate Login =====
    public User validateLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=SHA2(?,256)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));
                u.setContact(rs.getString("contact"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== 2. Register New User =====
    public int registerUser(User user) {
        int userId = -1;
        String sql = "INSERT INTO users (username, password, role, contact) VALUES (?, SHA2(?,256), ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getContact());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }


    // ===== 3. Get User By Username =====
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));
                u.setContact(rs.getString("contact"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===== 4. Update Password =====
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password=SHA2(?,256) WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


//     // ===== 5. Get All Users with Hospital Name =====
// public List<UserWithHospital> getAllUsersWithHospital() {
//     List<UserWithHospital> list = new ArrayList<>();
//     String sql = "SELECT u.user_id, u.username, u.role, u.contact, " +
//                  "COALESCE(dh.name, hah.name, '-') AS hospital_name " +
//                  "FROM users u " +
//                  "LEFT JOIN doctors d ON u.user_id = d.user_id " +
//                  "LEFT JOIN hospitals dh ON d.hospital_id = dh.hospital_id " +
//                  "LEFT JOIN hospital_admins ha ON u.user_id = ha.user_id " +
//                  "LEFT JOIN hospitals hah ON ha.hospital_id = hah.hospital_id " +
//                  "ORDER BY u.user_id";
    
//     try (Connection conn = DBConnection.getConnection();
//         PreparedStatement ps = conn.prepareStatement(sql);
//          ResultSet rs = ps.executeQuery()) {
//         while (rs.next()) {
//             UserWithHospital uwh = new UserWithHospital();
//             uwh.setUserId(rs.getInt("user_id"));
//             uwh.setUsername(rs.getString("username"));
//             uwh.setRole(rs.getString("role"));
//             uwh.setContact(rs.getString("contact"));
//             uwh.setHospitalName(rs.getString("hospital_name"));
//             list.add(uwh);
//         }
//     } catch (SQLException e) {
//         e.printStackTrace();
//     }
//     return list;
// }


// Get User by ID
public UserWithHospital getUserById(int userId) {
    String sql = "SELECT u.user_id, u.username, u.role, u.contact, " +
                 "COALESCE(dh.name, hah.name, '-') AS hospital_name " +
                 "FROM users u " +
                 "LEFT JOIN doctors d ON u.user_id = d.user_id " +
                 "LEFT JOIN hospitals dh ON d.hospital_id = dh.hospital_id " +
                 "LEFT JOIN hospital_admins ha ON u.user_id = ha.user_id " +
                 "LEFT JOIN hospitals hah ON ha.hospital_id = hah.hospital_id " +
                 "WHERE u.user_id=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            UserWithHospital uwh = new UserWithHospital();
            uwh.setUserId(rs.getInt("user_id"));
            uwh.setUsername(rs.getString("username"));
            uwh.setRole(rs.getString("role"));
            uwh.setContact(rs.getString("contact"));
            uwh.setHospitalName(rs.getString("hospital_name"));
            return uwh;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

// --- 1️⃣ Get all users with real name and hospital ---
    public List<UserWithHospital> getAllUsersWithHospital() {
        List<UserWithHospital> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.role, u.contact, " +
                     "COALESCE(p.name, d.name, ha.name, '-') AS real_name, " +
                     "COALESCE(hd.name, hha.name, '-') AS hospital_name " +
                     "FROM users u " +
                     "LEFT JOIN patients p ON u.user_id = p.user_id " +
                     "LEFT JOIN doctors d ON u.user_id = d.user_id " +
                     "LEFT JOIN hospital_admins ha ON u.user_id = ha.user_id " +
                     "LEFT JOIN hospitals hd ON d.hospital_id = hd.hospital_id " +
                     "LEFT JOIN hospitals hha ON ha.hospital_id = hha.hospital_id " +
                     "ORDER BY u.user_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserWithHospital uwh = new UserWithHospital();
                uwh.setUserId(rs.getInt("user_id"));
                uwh.setUsername(rs.getString("username"));
                uwh.setRole(rs.getString("role"));
                uwh.setContact(rs.getString("contact"));
                uwh.setRealName(rs.getString("real_name"));
                uwh.setHospitalName(rs.getString("hospital_name"));
                list.add(uwh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

// Update username
public boolean updateUsername(UserWithHospital u) {
    String sql = "UPDATE users SET username=? WHERE user_id=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, u.getUsername());
        ps.setInt(2, u.getUserId());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

// Delete user
public boolean deleteUser(int userId) {
    String sql = "DELETE FROM users WHERE user_id=?";
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public Iterable<User> getAllUsers() {
    List<User> list = new ArrayList<>();
    String sql = "SELECT u.user_id, u.username, u.role, u.contact, " +
                 "COALESCE(dh.name, hah.name, '-') AS hospital_name " +
                 "FROM users u " +
                 "LEFT JOIN doctors d ON u.user_id = d.user_id " +
                 "LEFT JOIN hospitals dh ON d.hospital_id = dh.hospital_id " +
                 "LEFT JOIN hospital_admins ha ON u.user_id = ha.user_id " +
                 "LEFT JOIN hospitals hah ON ha.hospital_id = hah.hospital_id " +
                 "ORDER BY u.user_id";
    
    try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            User u = new User();
            u.setUserId(rs.getInt("user_id"));
            u.setUsername(rs.getString("username"));
            u.setRole(rs.getString("role"));
            u.setContact(rs.getString("contact"));
            list.add(u);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}



}
