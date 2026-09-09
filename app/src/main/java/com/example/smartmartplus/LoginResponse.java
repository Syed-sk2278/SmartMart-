package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("token_type")
    private String token_type;

    @SerializedName("refresh_token")
    private String refresh_token;

    @SerializedName("user")
    private User user;


    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public User getUser() {
        return user;
    }


    // ==============================
    // USER
    // ==============================

    public static class User {

        @SerializedName("id")
        private String id;

        @SerializedName("email")
        private String email;

        @SerializedName("user_metadata")
        private UserMetadata userMetadata;


        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public UserMetadata getUserMetadata() {
            return userMetadata;
        }
    }


    // ==============================
    // USER METADATA
    // ==============================

    public static class UserMetadata {

        @SerializedName("full_name")
        private String fullName;

        @SerializedName("name")
        private String name;


        public String getFullName() {
            return fullName;
        }

        public String getName() {
            return name;
        }
    }
}