package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private String id;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("shelf_id")
    private String shelfId;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("brand")
    private String brand;

    @SerializedName("price")
    private double price;

    @SerializedName("gst_percentage")
    private double gstPercentage;

    @SerializedName("discount_percentage")
    private double discountPercentage;

    @SerializedName("stock_quantity")
    private int stockQuantity;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("available")
    private boolean available;

    public String getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getShelfId() {
        return shelfId;
    }

    public String getBarcode() {
        return barcode;
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

    public double getGstPercentage() {
        return gstPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }
}