package com.example.smartmartplus;

public class LoginResponse {

    private String access_token;
    private String token_type;
    private String refresh_token;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
}