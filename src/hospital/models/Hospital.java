package hospital.models;

public class Hospital {
    private int id;
    private String name;
    private String address;
    private String contact;
    private String specialization;
    // private String email;

    public Hospital() { }

    public Hospital(int id, String name, String address, String contact, String specialization) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.specialization = specialization;
    }

    public Hospital(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        // this.email = email;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    // public String getEmail() { return email; }
    //public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Hospital [id=" + id + ", name=" + name + ", address=" + address +
               ", contact=" + contact +  "]";
    }
}

