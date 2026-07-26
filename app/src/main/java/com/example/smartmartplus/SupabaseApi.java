package com.example.smartmartplus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SupabaseApi {

    @Headers({
            "apikey: YOUR_SUPABASE_ANON_KEY",
            "Content-Type: application/json"
    })

    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> loginUser(
            @Body LoginRequest request
    );
    @POST("YOUR_REGISTER_ENDPOINT")

    Call<RegisterResponse> registerUser(
            @Body RegisterRequest request
    );
}