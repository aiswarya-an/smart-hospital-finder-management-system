
package hospital.models;

import java.sql.Timestamp;

public class Admin {
    private int adminId;
    private int userId;
    private String name;
    private String contact;
    private String email;
    private Timestamp createdAt;

    public Admin() {}

    public Admin(int userId, String name, String contact, String email) {
        this.userId = userId;
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
