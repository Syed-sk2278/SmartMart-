package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Aisle {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("aisle_number")
    private int aisleNumber;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAisleNumber() {
        return aisleNumber;
    }
}