// Supplier.java
package model;

import java.sql.Timestamp;

public class Supplier {
    private int supplierId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String contactPerson;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Default constructor
    public Supplier() {
    }
    
    // Constructor with essential fields
    public Supplier(String name, String email) {
        this.name = name;
        this.email = email;
        this.isActive = true;
    }
    
    // Full constructor
    public Supplier(int supplierId, String name, String email, String phone, 
                    String address, String contactPerson, boolean isActive, 
                    Timestamp createdAt) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.contactPerson = contactPerson;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
