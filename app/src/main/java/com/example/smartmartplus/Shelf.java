package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Shelf {

    @SerializedName("id")
    private String id;

    @SerializedName("aisle_id")
    private String aisleId;

    @SerializedName("name")
    private String name;

    @SerializedName("shelf_number")
    private int shelfNumber;

    @SerializedName("created_at")
    private String createdAt;


    public String getId() {
        return id;
    }


    public String getAisleId() {
        return aisleId;
    }


    public String getName() {
        return name;
    }


    public int getShelfNumber() {
        return shelfNumber;
    }


    public String getCreatedAt() {
        return createdAt;
    }
}