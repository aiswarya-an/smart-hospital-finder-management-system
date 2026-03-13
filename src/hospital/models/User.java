// package hospital.models;

// public abstract class User implements Loginable {
//     protected int id;
//     protected String username;
//     protected String password;
//     protected String contact;

//     public User(int id, String username, String password, String contact) {
//         this.id = id;
//         this.username = username;
//         this.password = password;
//         this.contact = contact;
//     }

//     // Constructor for runtime session (password hidden)
//     public User(int id, String username, String contact) {
//         this.id = id;
//         this.username = username;
//         this.contact = contact;
//     }

//     // Getters and Setters
//     public int getId() {
//          return id; 
//     }
//     public void setId(int id) {
//          this.id = id; 
//     }
//     public String getUsername() {
//         return username;
//     }
//     public void setUsername(String username){
//         this.username = username;
//     }
//     public String getPasssword() {
//         return password;
//     }
//     public void setPasssword(String  password){
//         this.password = password;
//     }
//     public String getContact() {
//         return contact;
//     }
//     public void setContact(String  contact){
//         this.contact = contact;
//     }
//     public abstract String getRole();

//     @Override
//     public boolean login(String username, String password){
//         return this.username.equals(username) && this.password.equals(password);
//     }



// }


package hospital.models;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String contact;
    //private String hospitalName;
    private Timestamp createdAt;

    // Constructors
    public User() {}

    public User(String username, String password, String role, String contact) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.contact = contact;
        //this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    
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

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
