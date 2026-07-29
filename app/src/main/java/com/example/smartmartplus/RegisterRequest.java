package com.example.smartmartplus;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String phone;
    private String password;

    public RegisterRequest(String fullName,
                           String email,
                           String phone,
                           String password) {

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}