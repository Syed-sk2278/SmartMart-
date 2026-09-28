package com.example.smartmartplus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // ============================================
    // GET PRODUCT BY BARCODE
    // ============================================

    @GET("rest/v1/products")
    Call<List<Product>> getProductByBarcode(
            @Query("barcode") String barcode,
            @Query("store_id") String storeId
    );


    // ============================================
    // GET ALL PRODUCTS FOR A STORE
    // ============================================

    @GET("rest/v1/products")
    Call<List<Product>> getProductsByStore(
            @Query("store_id") String storeId
    );
}