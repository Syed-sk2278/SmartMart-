package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("entrance_qr_code")
    private String entrance_qr_code;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEntrance_qr_code() {
        return entrance_qr_code;
    }
}