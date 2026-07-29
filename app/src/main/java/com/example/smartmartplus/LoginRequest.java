package com.example.smartmartplus;

public class LoginRequest {

    // Variables
    private String email;
    private String password;

    // Constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter Method for Email
    public String getEmail() {
        return email;
    }

    // Getter Method for Password
    public String getPassword() {
        return password;
    }

}