package com.example.smartmartplus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApi {

    // =====================================================
    // LOGIN
    // =====================================================

    @POST("auth/v1/token?grant_type=password")
    Call<LoginResponse> loginUser(
            @Body LoginRequest request
    );


    // =====================================================
    // REGISTRATION
    // =====================================================

    @POST("auth/v1/signup")
    Call<RegisterResponse> registerUser(
            @Body RegisterRequest request
    );


    // =====================================================
    // STORE ENTRANCE QR
    // =====================================================

    @GET("rest/v1/stores")
    Call<List<Store>> getStoreByQR(
            @Query("entrance_qr_code") String qrCode
    );


    // =====================================================
    // PRODUCT BY BARCODE
    // =====================================================

    @GET("rest/v1/products")
    Call<List<Product>> getProductByBarcode(
            @Query("barcode") String barcode,
            @Query("store_id") String storeId
    );


    // =====================================================
    // CATEGORY BY NAME
    // =====================================================

    @GET("rest/v1/categories")
    Call<List<Category>> getCategoryByName(
            @Query("name") String name
    );


    // =====================================================
    // PRODUCTS BY CATEGORY + STORE
    // =====================================================

    @GET("rest/v1/products")
    Call<List<Product>> getProductsByCategory(
            @Query("category_id") String categoryId,
            @Query("store_id") String storeId
    );


    // =====================================================
    // SHELF BY ID
    // =====================================================

    @GET("rest/v1/shelves")
    Call<List<Shelf>> getShelfById(
            @Query("id") String shelfId
    );
    @GET("rest/v1/aisles")
    Call<List<Aisle>> getAisleById(
            @Query("id") String aisleId
    );
}