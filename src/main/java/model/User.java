// User.java
package model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;
    private boolean isActive;
    private String role;
    private Timestamp joinedAt;
    private String profileUrl;
    
    // Default constructor
    public User() {
    }
    
    // Constructor with essential fields
    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.role = "customer";
    }
    
    // Full constructor
    public User(int userId, String firstname, String lastname, String email, 
                String password, String phoneNumber, boolean isActive, 
                String role, Timestamp joinedAt, String profileUrl) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.role = role;
        this.joinedAt = joinedAt;
        this.profileUrl = profileUrl;
    }
    
    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
    
    // Utility methods
    public String getFullName() {
        return firstname + " " + lastname;
    }
    
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
    
    public boolean isManager() {
        return "manager".equalsIgnoreCase(role);
    }
    
    public boolean isCustomer() {
        return "customer".equalsIgnoreCase(role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", role='" + role + '\'' +
                '}';
    }
}

