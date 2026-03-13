package hospital;

import hospital.dao.*;
import java.sql.Connection;

public class DAOTest {
    public static void main(String[] args) {
        // 1. Get a single connection to use (or let each DAO open its own)
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            System.out.println("Database connected successfully!");

            System.out.println("\n===== Testing UserDAO =====");
            UserDAO userDAO = new UserDAO();
            userDAO.getAllUsers().forEach(System.out::println);

            System.out.println("\n===== Testing AdminDAO =====");
            AdminDAO adminDAO = new AdminDAO();
            adminDAO.getAllUsers().forEach(System.out::println);

            System.out.println("\n===== Testing DoctorDAO =====");
            DoctorDAO doctorDAO = new DoctorDAO();
            doctorDAO.getAllDoctors().forEach(System.out::println);

            System.out.println("\n===== Testing PatientDAO =====");
            PatientDAO patientDAO = new PatientDAO();
            patientDAO.getAllPatients().forEach(System.out::println);

            System.out.println("\n===== Testing AppointmentDAO =====");
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            appointmentDAO.getAllAppointments().forEach(System.out::println);

            System.out.println("\n===== Testing HospitalDAO =====");
            HospitalDAO hospitalDAO = new HospitalDAO();
            hospitalDAO.getAllHospitals().forEach(System.out::println);

            System.out.println("\n===== Testing HospitalAdminDAO =====");
            HospitalAdminDAO hospitalAdminDAO = new HospitalAdminDAO();
            hospitalAdminDAO.getAllAdmins().forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the connection only once at the end
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Database connection closed.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("\n===== DONE TESTING DAOs =====");
    }
}
