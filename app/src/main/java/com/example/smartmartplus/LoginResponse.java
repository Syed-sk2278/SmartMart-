package com.example.smartmartplus;

public class LoginResponse {

    private String access_token;
    private String token_type;
    private String refresh_token;

    // Getter for Access Token
    public String getAccessToken() {
        return access_token;
    }

    // Getter for Token Type
    public String getTokenType() {
        return token_type;
    }

    // Getter for Refresh Token
    public String getRefreshToken() {
        return refresh_token;
    }

}