
package hospital.models;

import java.sql.Date;

public class Patient {
    private int patientId;
    private int userId;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String email;
    private String address;
    private Date dob;
    private Date lastAppointmentDate;   // date of last appointment
    private String lastDoctorName; 
private String lastVisit;
private int totalVisits;

// Getters + Setters for above
     // doctor name for last appointment


    public Patient() {}

    public Patient(int userId, String name, int age, String gender, String contact, String email, String address, Date dob) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.dob = dob;

    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public Date getLastAppointmentDate() { return lastAppointmentDate; }
    public void setLastAppointmentDate(Date lastAppointmentDate) { this.lastAppointmentDate = lastAppointmentDate; }

    public String getLastDoctorName() { return lastDoctorName; }
    public void setLastDoctorName(String lastDoctorName) { this.lastDoctorName = lastDoctorName; }

    public int getTotalVisits() { return totalVisits; }
    public void setTotalVisits(int totalVisits) { this.totalVisits = totalVisits; }

}

