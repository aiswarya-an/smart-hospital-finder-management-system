

package hospital.models;

public class Doctor {
    private int doctorId;
    private int userId;
    private int hospitalId;
    private String name;
    private String specialization;
    private String contact;
    private String email;
    private int experience;
    private String status; // ACTIVE, INACTIVE, ON_LEAVE

    public Doctor() {}

    public Doctor(int userId, String name, String specialization, String contact, String email, int hospitalId, int experience) {
        this.userId = userId;
        this.name = name;
        this.specialization = specialization;
        this.contact = contact;
        this.email = email;
        this.hospitalId = hospitalId;
        this.experience = experience;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
