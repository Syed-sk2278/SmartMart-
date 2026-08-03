package com.example.smartmartplus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SupabaseApi {

    // Login
    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> loginUser(
            @Body LoginRequest request
    );

    // Registration
    @POST("auth/v1/signup")
    Call<RegisterResponse> registerUser(
            @Body RegisterRequest request
    );
}