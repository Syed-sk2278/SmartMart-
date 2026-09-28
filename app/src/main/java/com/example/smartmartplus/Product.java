package com.example.smartmartplus;

import com.google.gson.annotations.SerializedName;

public class Product {

    // =========================================
    // PRODUCT ID
    // =========================================

    @SerializedName("id")
    private String id;


    // =========================================
    // STORE ID
    // =========================================

    @SerializedName("store_id")
    private String storeId;


    // =========================================
    // CATEGORY ID
    // =========================================

    @SerializedName("category_id")
    private String categoryId;


    // =========================================
    // SHELF ID
    // =========================================

    @SerializedName("shelf_id")
    private String shelfId;


    // =========================================
    // BARCODE
    // =========================================

    @SerializedName("barcode")
    private String barcode;


    // =========================================
    // PRODUCT NAME
    // =========================================

    @SerializedName("product_name")
    private String productName;


    // =========================================
    // DESCRIPTION
    // =========================================

    @SerializedName("description")
    private String description;


    // =========================================
    // BRAND
    // =========================================

    @SerializedName("brand")
    private String brand;


    // =========================================
    // PRICE
    // =========================================

    @SerializedName("price")
    private double price;


    // =========================================
    // GST
    // =========================================

    @SerializedName("gst_percentage")
    private double gstPercentage;


    // =========================================
    // DISCOUNT
    // =========================================

    @SerializedName("discount_percentage")
    private double discountPercentage;


    // =========================================
    // STOCK
    // =========================================

    @SerializedName("stock_quantity")
    private int stockQuantity;


    // =========================================
    // IMAGE URL
    // =========================================

    @SerializedName("image_url")
    private String imageUrl;


    // =========================================
    // AVAILABLE
    // =========================================

    @SerializedName("available")
    private boolean available;


    // =========================================
    // GETTERS
    // =========================================

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


    // Keep these because your existing code
    // may use getName()

    public String getName() {
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


    // Existing code compatibility

    public double getGst() {
        return gstPercentage;
    }


    public double getDiscountPercentage() {
        return discountPercentage;
    }


    // Existing code compatibility

    public double getDiscount() {
        return discountPercentage;
    }


    public int getStockQuantity() {
        return stockQuantity;
    }


    // Existing code compatibility

    public int getStock_quantity() {
        return stockQuantity;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    // Existing code compatibility

    public String getImage_url() {
        return imageUrl;
    }


    public String getStore_id() {
        return storeId;
    }


    public String getCategory_id() {
        return categoryId;
    }


    public String getShelf_id() {
        return shelfId;
    }


    public String getStoreIdValue() {
        return storeId;
    }


    public boolean isAvailable() {
        return available;
    }
}