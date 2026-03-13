package hospital.test;

import hospital.dao.AdminDAO;
import hospital.models.Admin;
import hospital.models.User;

import java.util.List;

public class TestAdminDAO {
    public static void main(String[] args) {
        AdminDAO dao = new AdminDAO();

        // ✅ Test 1: Get Admin by userId
        int testUserId = 1; // replace with an actual admin's user_id
        Admin admin = dao.getAdminByUserId(testUserId);
        if (admin != null) {
            System.out.println("Admin found: " + admin.getName() + ", Email: " + admin.getEmail());
        } else {
            System.out.println("Admin not found!");
        }

        // // ✅ Test 2: Update Admin Profile
        // if (admin != null) {
        //     admin.setContact("9990001112"); // update contact
        //     boolean updated = dao.updateAdminProfile(admin);
        //     System.out.println("Admin update status: " + updated);
        // }

        // ✅ Test 3: Get All Users
        List<User> users = dao.getAllUsers();
        System.out.println("\nAll users in DB:");
        for (User u : users) {
            System.out.println(u.getUserId() + " - " + u.getUsername() + " (" + u.getRole() + ")");
        }

        // ✅ Test 4: Delete User
        // int deleteUserId = 9; // replace with actual user_id you want to delete
        // boolean deleted = dao.deleteUser(deleteUserId);
        // System.out.println("\nUser deletion status: " + deleted);
    
        // ✅ Test 5: Get All Admins
        List<Admin> admins = dao.getAllAdmins();
        System.out.println("\nAll Admins in DB:");
        for (Admin a : admins) {
            System.out.println(a.getAdminId() + " - " + a.getName() + " (" + a.getEmail() + ")");
        }

    }

}
