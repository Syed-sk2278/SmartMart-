package com.example.smartmartplus;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest {

    private String email;
    private String password;
    private Map<String, String> data;

    public RegisterRequest(
            String fullName,
            String email,
            String phone,
            String password) {

        this.email = email;
        this.password = password;

        // Store extra user information in Supabase user metadata
        data = new HashMap<>();
        data.put("full_name", fullName);
        data.put("phone", phone);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, String> getData() {
        return data;
    }
}