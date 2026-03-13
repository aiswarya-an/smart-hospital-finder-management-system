package hospital.test;

import hospital.dao.HospitalAdminDAO;
import hospital.models.HospitalAdmin;

import java.util.List;

public class TestHospitalAdminDAO {
    public static void main(String[] args) {
        HospitalAdminDAO dao = new HospitalAdminDAO();

        // // 1️⃣ Add new hospital admin
        HospitalAdmin newAdmin = new HospitalAdmin();
        newAdmin.setUserId(10); // Replace with a valid user_id
        newAdmin.setName("Test Admin");
        newAdmin.setEmail("testadmin@hms.com");
        newAdmin.setContact("9991112223");
        newAdmin.setHospitalId(1);

        boolean added = dao.addHospitalAdmin(newAdmin);
        System.out.println("Admin added: " + added);

        // 2️⃣ Get all admins
        List<HospitalAdmin> allAdmins = dao.getAllAdmins();
        System.out.println("\nAll Admins:");
        for (HospitalAdmin a : allAdmins) {
            System.out.println(a.getId() + " - " + a.getName() + " [" + a.getStatus() + "]");
        }

        // 3️⃣ Get pending admins
        List<HospitalAdmin> pendingAdmins = dao.getPendingAdmins();
        System.out.println("\nPending Admins:");
        for (HospitalAdmin a : pendingAdmins) {
            System.out.println(a.getId() + " - " + a.getName() + " [" + a.getStatus() + "]");
        }

        // 4️⃣ Approve first pending admin (if any)
        if (!pendingAdmins.isEmpty()) {
            boolean approved = dao.approveAdmin(pendingAdmins.get(0).getId());
            System.out.println("\nFirst pending admin approved: " + approved);
        }

        // 5️⃣ Count of admins
        System.out.println("\nTotal admins: " + dao.getAdminCount());
    }
}
