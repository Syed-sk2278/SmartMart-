package com.example.smartmartplus;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String phone;
    private String password;

    // Constructor
    public RegisterRequest(String fullName,
                           String email,
                           String phone,
                           String password) {

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // Getter Methods

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}