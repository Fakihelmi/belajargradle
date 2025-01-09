package org.example.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String otp;
    private boolean isVerified;
    private String profilePicture;
    private String address;
    private String dateOfBirth;

    // Constructor
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isVerified = false;
    }

    public User(String username, String password, String email, String otp, boolean isVerified) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.otp = otp;
        this.isVerified = isVerified;
    }

    public User(String username, String password, String email, String otp, boolean isVerified, String profilePicture, String address, String dateOfBirth) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.otp = otp;
        this.isVerified = isVerified;
        this.profilePicture = profilePicture;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}