package hospital.test;

import hospital.models.Patient;
import hospital.dao.PatientDAO;
import java.util.List;
    
    public class TestPatientDAO {
    public static void main(String[] args) {
        PatientDAO dao = new PatientDAO();
    
        // ✅ Test 1: Check DB connection and fetch all patients            
        List<Patient> list = dao.getAllPatients();
            System.out.println("All patients in DB:");
            for (Patient p : list) {
                System.out.println(p.getPatientId() + " - " + p.getName() + " (" + p.getAge() + ")");
            }
    
        // ✅ Test 2: Get patient by userId (replace with actual id)
            Patient pat = dao.getPatientByUserId(8); 
            if (pat != null)
                System.out.println("\nFound patient: " + pat.getName() + ", Email: " + pat.getEmail());
            else
                System.out.println("\nPatient not found!");

                
                Patient p = dao.getPatientByUserId(8);
                if(p != null) {
                    p.setName("Test Update");
                    boolean ok = dao.updatePatientProfile(p);
                    System.out.println(ok ? "Updated" : "Failed");
                } else {
                    System.out.println("Patient not found!");
                }
            }             
}
    
