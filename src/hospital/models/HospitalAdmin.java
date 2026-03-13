package hospital.models;

public class HospitalAdmin {
    private int id;// Auto-increment in DB
    private int userId;        
    private String name;
    private String email;
    private String contact;
    private int hospitalId;    // The hospital they are assigned to
    private String status;  // true if admin approved

    public HospitalAdmin() {}

    public HospitalAdmin(int userId, String name, String email, String contact, int hospitalId, String status ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.hospitalId = hospitalId;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
