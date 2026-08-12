package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView tvProductName;
    private TextView tvBrand;
    private TextView tvPrice;
    private TextView tvGst;
    private TextView tvDiscount;
    private TextView tvStock;
    private TextView tvShelf;
    private TextView tvDescription;
    private TextView tvBarcode;

    private TextView tvBack;
    private TextView tvCartIcon;

    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_details);

        // =========================
        // INITIALIZE VIEWS
        // =========================

        tvProductName = findViewById(R.id.tvProductName);
        tvBrand = findViewById(R.id.tvBrand);
        tvPrice = findViewById(R.id.tvPrice);
        tvGst = findViewById(R.id.tvGst);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvStock = findViewById(R.id.tvStock);
        tvShelf = findViewById(R.id.tvShelf);
        tvDescription = findViewById(R.id.tvDescription);
        tvBarcode = findViewById(R.id.tvBarcode);

        tvBack = findViewById(R.id.tvBack);
        tvCartIcon = findViewById(R.id.tvCartIcon);

        btnAddToCart = findViewById(R.id.btnAddToCart);


        // =========================
        // GET PRODUCT DATA
        // =========================

        String productName =
                getIntent().getStringExtra("PRODUCT_NAME");

        String brand =
                getIntent().getStringExtra("PRODUCT_BRAND");

        String description =
                getIntent().getStringExtra("PRODUCT_DESCRIPTION");

        String shelf =
                getIntent().getStringExtra("PRODUCT_SHELF");

        String barcode =
                getIntent().getStringExtra("PRODUCT_BARCODE");


        double price =
                getIntent().getDoubleExtra(
                        "PRODUCT_PRICE",
                        0
                );

        double gst =
                getIntent().getDoubleExtra(
                        "PRODUCT_GST",
                        0
                );

        double discount =
                getIntent().getDoubleExtra(
                        "PRODUCT_DISCOUNT",
                        0
                );

        int stock =
                getIntent().getIntExtra(
                        "PRODUCT_STOCK",
                        0
                );


        // =========================
        // DISPLAY PRODUCT
        // =========================

        tvProductName.setText(
                productName != null && !productName.isEmpty()
                        ? productName
                        : "Product"
        );

        tvBrand.setText(
                "Brand: " +
                        (brand != null && !brand.isEmpty()
                                ? brand
                                : "N/A")
        );

        tvPrice.setText(
                "₹" +
                        String.format(
                                "%.2f",
                                price
                        )
        );

        tvGst.setText(
                "GST: " +
                        String.format(
                                "%.0f",
                                gst
                        ) +
                        "%"
        );

        tvDiscount.setText(
                "Discount: " +
                        String.format(
                                "%.0f",
                                discount
                        ) +
                        "%"
        );

        tvStock.setText(
                "Stock: " +
                        stock
        );

        tvShelf.setText(
                "Shelf: " +
                        (shelf != null && !shelf.isEmpty()
                                ? shelf
                                : "N/A")
        );

        tvDescription.setText(
                description != null && !description.isEmpty()
                        ? description
                        : "No description available"
        );

        tvBarcode.setText(
                "Barcode: " +
                        (barcode != null && !barcode.isEmpty()
                                ? barcode
                                : "N/A")
        );


        // =========================
        // BACK BUTTON
        // =========================

        tvBack.setOnClickListener(v -> finish());


        // =========================
        // CART ICON
        // =========================

        tvCartIcon.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProductDetailsActivity.this,
                    CartActivity.class
            );

            startActivity(intent);
        });


        // =========================
        // ADD TO CART
        // =========================

        btnAddToCart.setOnClickListener(v -> {

            String name =
                    productName != null && !productName.isEmpty()
                            ? productName
                            : "Product";

            Toast.makeText(
                    ProductDetailsActivity.this,
                    name + " added to cart",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }
}