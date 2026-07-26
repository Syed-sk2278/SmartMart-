package com.example.smartmartplus;

public class RegisterRequest {

    private String fullName;
    private String phone;
    private String password;

    public RegisterRequest(String fullName,
                           String phone,
                           String password) {

        this.fullName = fullName;
        this.phone = phone;
        this.password = password;
    }

}