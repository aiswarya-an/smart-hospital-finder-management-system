


package hospital.models;

import java.sql.Date;
import java.sql.Time;

public class Appointment {
    private int appointmentId;
    private int patientUserId;
    private int doctorUserId;
    private String patientName;
    private String doctorName;
    private Date date;
    private Time time;
    private String status;
    private String notes;
    private String hospitalName = "N/A";

    public Appointment() {}

    public Appointment(int patientUserId, int doctorUserId, Date appointmentDate, Time appointmentTime, String status) {
        this.patientUserId = patientUserId;
        this.doctorUserId = doctorUserId;
        this.date = appointmentDate;
        this.time = appointmentTime;
        this.status = status;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    
    public int getPatientUserId() { return patientUserId; }
    public void setPatientUserId(int patientUserId) { this.patientUserId = patientUserId; }
    
    public int getDoctorUserId() { return doctorUserId; }
    public void setDoctorUserId(int doctorUserId) { this.doctorUserId = doctorUserId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getHospitalName() { return hospitalName; }
    // You would typically fetch hospital name via a separate join or Doctor/Hospital DAO
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; } 
    // Remaining getters/setters omitted for brevity
}