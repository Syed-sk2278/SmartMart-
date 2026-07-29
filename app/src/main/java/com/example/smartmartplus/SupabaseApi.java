package com.example.smartmartplus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SupabaseApi {

    // Login API
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> loginUser(
            @Body LoginRequest request
    );

    // Register API
    @POST("auth/v1/signup")
    Call<RegisterResponse> registerUser(
            @Body RegisterRequest request
    );
}