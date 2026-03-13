package hospital.models;

public class UserWithHospital {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String contact;
    private String hospitalName;
    private String realName;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

}
