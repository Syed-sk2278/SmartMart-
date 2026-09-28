package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Store {

    // =====================================================
    // STORE ID
    // =====================================================

    @SerializedName("id")
    private String id;


    // =====================================================
    // STORE NAME
    // =====================================================

    @SerializedName("name")
    private String name;


    // =====================================================
    // STORE ADDRESS
    // =====================================================

    @SerializedName("address")
    private String address;


    // =====================================================
    // STORE ENTRANCE QR
    // =====================================================

    @SerializedName("entrance_qr_code")
    private String entranceQrCode;


    // =====================================================
    // GET STORE ID
    // =====================================================

    public String getId() {
        return id;
    }


    // =====================================================
    // GET STORE NAME
    // =====================================================

    public String getName() {
        return name;
    }


    // =====================================================
    // GET STORE ADDRESS
    // =====================================================

    public String getAddress() {
        return address;
    }


    // =====================================================
    // GET ENTRANCE QR
    // =====================================================

    public String getEntrance_qr_code() {
        return entranceQrCode;
    }


    // Optional cleaner getter
    public String getEntranceQrCode() {
        return entranceQrCode;
    }
}