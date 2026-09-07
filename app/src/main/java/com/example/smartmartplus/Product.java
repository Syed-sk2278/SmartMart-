package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Product {

    // ==============================
    // PRODUCT ID
    // ==============================

    @SerializedName("id")
    private String id;


    // ==============================
    // BARCODE
    // ==============================

    @SerializedName("barcode")
    private String barcode;


    // ==============================
    // PRODUCT NAME
    // ==============================

    @SerializedName("product_name")
    private String productName;


    // ==============================
    // DESCRIPTION
    // ==============================

    @SerializedName("description")
    private String description;


    // ==============================
    // BRAND
    // ==============================

    @SerializedName("brand")
    private String brand;


    // ==============================
    // PRICE
    // ==============================

    @SerializedName("price")
    private double price;


    // ==============================
    // GST
    // ==============================

    @SerializedName("gst_percentage")
    private double gstPercentage;


    // ==============================
    // DISCOUNT
    // ==============================

    @SerializedName("discount_percentage")
    private double discountPercentage;


    // ==============================
    // STOCK
    // ==============================

    @SerializedName("stock_quantity")
    private int stockQuantity;


    // ==============================
    // SHELF
    // ==============================

    @SerializedName("shelf_id")
    private String shelfId;


    // ==============================
    // IMAGE URL
    // ==============================

    @SerializedName("image_url")
    private String imageUrl;


    // ==============================
    // STORE ID
    // ==============================

    @SerializedName("store_id")
    private String storeId;


    // ==============================
    // GETTERS
    // ==============================

    public String getId() {
        return id;
    }


    public String getBarcode() {
        return barcode;
    }


    public String getName() {
        return productName;
    }


    public String getProductName() {
        return productName;
    }


    public String getDescription() {
        return description;
    }


    public String getBrand() {
        return brand;
    }


    public double getPrice() {
        return price;
    }


    public double getGst() {
        return gstPercentage;
    }


    public double getGstPercentage() {
        return gstPercentage;
    }


    public double getDiscount() {
        return discountPercentage;
    }


    public double getDiscountPercentage() {
        return discountPercentage;
    }


    public int getStock_quantity() {
        return stockQuantity;
    }


    public int getStockQuantity() {
        return stockQuantity;
    }


    public String getShelf_id() {
        return shelfId;
    }


    public String getShelfId() {
        return shelfId;
    }


    public String getImage_url() {
        return imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public String getStore_id() {
        return storeId;
    }


    public String getStoreId() {
        return storeId;
    }
}