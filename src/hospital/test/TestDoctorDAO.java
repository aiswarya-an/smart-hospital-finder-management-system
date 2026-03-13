package hospital.test;

import hospital.dao.DoctorDAO;
import hospital.models.Doctor;
import hospital.models.Appointment;

import java.util.List;

public class TestDoctorDAO {
    public static void main(String[] args) {
        DoctorDAO dao = new DoctorDAO();

        // ===== 1. Add a new doctor (Admin action) =====
        Doctor newDoctor = new Doctor();
        newDoctor.setUserId(5); // assume user_id exists
        newDoctor.setName("Dr. Test");
        newDoctor.setSpecialization("Neurology");
        newDoctor.setContact("9998887777");
        newDoctor.setEmail("dr.test@hospital.com");
        newDoctor.setHospitalId(1);
        newDoctor.setExperience("3 years");
        newDoctor.setStatus("ACTIVE");

        boolean added = DoctorDAO.addDoctor(newDoctor);
        System.out.println("Add doctor: " + added);

        // ===== 2. Get Doctor Profile (Doctor action) =====
        Doctor doctorProfile = dao.getDoctorByUserId(5);
        System.out.println("Doctor Profile: " + doctorProfile.getName() + ", " + doctorProfile.getSpecialization());

        // ===== 3. Update Doctor Profile (Doctor action) =====
        doctorProfile.setContact("9112223333");
        doctorProfile.setExperience("4 years");
        boolean updatedProfile = dao.updateDoctor(doctorProfile);
        System.out.println("Update Doctor Profile: " + updatedProfile);

        // ===== 4. Get Appointments for Doctor =====
        List<Appointment> appointments = dao.getAppointmentsForDoctor(5);
        System.out.println("Appointments Count: " + appointments.size());
        for (Appointment a : appointments) {
            System.out.println(a.getAppointmentId() + " - " + a.getPatientName() + " - " + a.getStatus());
        }

        // ===== 5. Update Appointment Status =====
        if (!appointments.isEmpty()) {
            boolean statusUpdated = dao.updateAppointmentStatus(appointments.get(0).getAppointmentId(), "COMPLETED");
            System.out.println("Update Appointment Status: " + statusUpdated);
        }

        // ===== 6. Count Doctors =====
        int doctorCount = DoctorDAO.getDoctorCount();
        System.out.println("Total Doctors: " + doctorCount);

        // ===== 7. Get All Doctors (Admin view) =====
        List<Doctor> allDoctors = dao.getAllDoctors();
        System.out.println("All Doctors:");
        for (Doctor d : allDoctors) {
            System.out.println(d.getName() + " - " + d.getStatus());
        }

        // ===== 8. Get All Active Doctors (Patient view) =====
        List<Doctor> activeDoctors = dao.getAllActiveDoctors();
        System.out.println("Active Doctors:");
        for (Doctor d : activeDoctors) {
            System.out.println(d.getName() + " - " + d.getSpecialization());
        }

        // ===== 9. Search Doctors (Patient view) =====
        List<Doctor> searchResult = dao.searchDoctors("Neuro");
        System.out.println("Search Doctors for 'Neuro':");
        for (Doctor d : searchResult) {
            System.out.println(d.getName() + " - " + d.getSpecialization());
        }

        // ===== 10. Update Doctor (Admin action) =====
        Doctor adminUpdateDoctor = allDoctors.get(0);
        adminUpdateDoctor.setStatus("INACTIVE");
        boolean updatedDoctor = dao.updateDoctor(adminUpdateDoctor);
        System.out.println("Admin Update Doctor (Deactivate): " + updatedDoctor);

        // ===== 11. Delete Doctor (Admin action) =====
        if (allDoctors.size() > 1) {
            boolean deleted = DoctorDAO.deleteDoctor(allDoctors.get(1).getDoctorId());
            System.out.println("Delete Doctor: " + deleted);
        }

        // ===== 12. Get Next Doctor ID (Optional) =====
        int nextId = dao.getNextDoctorIds();
        System.out.println("Next Doctor ID: " + nextId);
    }
}

