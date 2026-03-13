package hospital.test;

import hospital.dao.UserDAO;
import hospital.models.User;

public class TestUserDAO {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        System.out.println("=== Testing Database Connection ===");
        if (dao != null) {
            System.out.println("✅ UserDAO initialized successfully.");
        }

        System.out.println("\n=== Fetching All Users ===");
        for (User u : dao.getAllUsers()) {
            System.out.println(u.getUserId() + " | " + u.getUsername() + " | " + u.getRole() + " | " + u.getContact());
        }

        System.out.println("\n=== Testing Login ===");
        User logged = dao.validateLogin("admin1", "aiswarya");
        if (logged != null) {
            System.out.println("✅ Login Success: " + logged.getUsername() + " (" + logged.getRole() + ")");
        } else {
            System.out.println("❌ Login Failed - check password or hashing");
        }
    }
}
