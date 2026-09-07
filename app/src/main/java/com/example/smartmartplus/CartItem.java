package com.example.smartmartplus;

public class CartItem {

    private String productId;
    private String productName;
    private String brand;
    private String barcode;
    private double price;
    private double gst;
    private double discount;
    private int stock;
    private String shelf;
    private String description;
    private String imageUrl;
    private int quantity;

    public CartItem(
            String productId,
            String productName,
            String brand,
            String barcode,
            double price,
            double gst,
            double discount,
            int stock,
            String shelf,
            String description,
            String imageUrl,
            int quantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.barcode = barcode;
        this.price = price;
        this.gst = gst;
        this.discount = discount;
        this.stock = stock;
        this.shelf = shelf;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public String getBarcode() {
        return barcode;
    }

    public double getPrice() {
        return price;
    }

    public double getGst() {
        return gst;
    }

    public double getDiscount() {
        return discount;
    }

    public int getStock() {
        return stock;
    }

    public String getShelf() {
        return shelf;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}