package hospital.test;

import hospital.dao.AppointmentDAO;
import hospital.models.Appointment;

import java.util.List;

public class TestAppointmentDAO {
    public static void main(String[] args) {
        AppointmentDAO dao = new AppointmentDAO();
        System.out.println("Database connected successfully!");
        System.out.println("=== Testing AppointmentDAO ===");

        // 1. Get all appointments
        List<Appointment> allAppointments = dao.getAllAppointments();
        System.out.println("All Appointments:");
        for (Appointment a : allAppointments) {
            System.out.printf("%d\t%d\t%d\t%s\t%s\t%s\n",
                    a.getAppointmentId(),
                    a.getPatientUserId(),
                    a.getDoctorUserId(),
                    a.getPatientName(),
                    a.getDoctorName(),
                    a.getDate(),
                    a.getTime(),
                    a.getStatus());
        }

        // // 2. Update the status of the first appointment if it exists
        // if (!allAppointments.isEmpty()) {
        //     Appointment first = allAppointments.get(0);
        //     boolean updated = dao.updateAppointmentStatus(first.getAppointmentId(), "Confirmed");
        //     System.out.println("Update Appointment Status for ID " + first.getAppointmentId() + ": " + updated);
        // }

        // 3. Get appointments by status "Scheduled"
        List<Appointment> scheduledAppointments = dao.getAppointmentsByStatus("Scheduled");
        System.out.println("Scheduled Appointments Count: " + scheduledAppointments.size());

        // // 4. Delete the first appointment if it exists
        // if (!allAppointments.isEmpty()) {
        //     Appointment first = allAppointments.get(0);
        //     boolean deleted = dao.deleteAppointment(first.getAppointmentId());
        //     System.out.println("Delete Appointment ID " + first.getAppointmentId() + ": " + deleted);
        // }

        // 5. Total appointments count
        int totalCount = dao.getAppointmentCount();
        System.out.println("Total Appointments in DB: " + totalCount);
    }
}
