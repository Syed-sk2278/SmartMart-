package com.example.smartmartplus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApi {

    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> loginUser(
            @Body LoginRequest request
    );


    @POST("auth/v1/signup")
    Call<RegisterResponse> registerUser(
            @Body RegisterRequest request
    );


    // ==========================================
    // FORGOT PASSWORD
    // ==========================================

    @POST("auth/v1/recover")
    Call<Void> resetPassword(
            @Body ForgotPasswordRequest request
    );


    // ==========================================
    // PRODUCTS
    // ==========================================

    @GET("rest/v1/products")
    Call<List<Product>> getProductByBarcode(
            @Query("barcode") String barcode,
            @Query("store_id") String storeId
    );


    // ==========================================
    // STORES
    // ==========================================

    @GET("rest/v1/stores")
    Call<List<Store>> getStoreByQR(
            @Query("entrance_qr_code") String qrCode
    );
}