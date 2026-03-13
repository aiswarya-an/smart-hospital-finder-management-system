package hospital.test;

import hospital.dao.HospitalDAO;
import hospital.models.Hospital;

import java.util.List;

public class TestHospitalDAO {
    public static void main(String[] args) {
        System.out.println("=== Testing HospitalDAO ===");

        // // 1️⃣ Add a new hospital
        // Hospital h1 = new Hospital("Sunrise Hospital", "Kochi", "0484-555555", "sunrise@example.com");
        // boolean added = HospitalDAO.addHospital(h1);
        // System.out.println("Add Hospital: " + added);

        // 2️⃣ Get all hospitals
        HospitalDAO dao = new HospitalDAO();
        List<Hospital> hospitals = dao.getAllHospitals();
        System.out.println("All Hospitals:");
        for (Hospital h : hospitals) {
            System.out.println(h);
        }

        // 3️⃣ Get a single hospital
        if (!hospitals.isEmpty()) {
            int id = hospitals.get(0).getId();
            Hospital single = HospitalDAO.getHospital(id);
            System.out.println("Single Hospital: " + single);
        }

        // 4️⃣ Update a hospital
        if (!hospitals.isEmpty()) {
            Hospital hUpdate = hospitals.get(0);
            hUpdate.setName("Updated Hospital Name");
            boolean updated = HospitalDAO.updateHospital(hUpdate);
            System.out.println("Update Hospital: " + updated);
        }

        // 5️⃣ Count hospitals
        int count = HospitalDAO.getHospitalCount();
        System.out.println("Total Hospitals: " + count);

        // 6️⃣ Delete a hospital (optional)
        // if (!hospitals.isEmpty()) {
        //     int idToDelete = hospitals.get(0).getId();
        //     boolean deleted = HospitalDAO.deleteHospital(idToDelete);
        //     System.out.println("Delete Hospital: " + deleted);
        // }
    }
}
