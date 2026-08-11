package com.example.smartmartplus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity
        extends AppCompatActivity {

    private TextView tvProductName;
    private TextView tvBrand;
    private TextView tvPrice;
    private TextView tvGst;
    private TextView tvDiscount;
    private TextView tvStock;
    private TextView tvShelf;
    private TextView tvDescription;
    private TextView tvBarcode;

    private Button btnAddToCart;


    @Override
    protected void onCreate(
            Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_product_details
        );


        tvProductName =
                findViewById(R.id.tvProductName);

        tvBrand =
                findViewById(R.id.tvBrand);

        tvPrice =
                findViewById(R.id.tvPrice);

        tvGst =
                findViewById(R.id.tvGst);

        tvDiscount =
                findViewById(R.id.tvDiscount);

        tvStock =
                findViewById(R.id.tvStock);

        tvShelf =
                findViewById(R.id.tvShelf);

        tvDescription =
                findViewById(R.id.tvDescription);

        tvBarcode =
                findViewById(R.id.tvBarcode);

        btnAddToCart =
                findViewById(R.id.btnAddToCart);


        // =========================
        // GET DATA
        // =========================

        String productName =
                getIntent().getStringExtra(
                        "PRODUCT_NAME"
                );

        String brand =
                getIntent().getStringExtra(
                        "PRODUCT_BRAND"
                );

        String description =
                getIntent().getStringExtra(
                        "PRODUCT_DESCRIPTION"
                );

        String shelf =
                getIntent().getStringExtra(
                        "PRODUCT_SHELF"
                );

        String barcode =
                getIntent().getStringExtra(
                        "PRODUCT_BARCODE"
                );


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
        // DISPLAY
        // =========================

        tvProductName.setText(
                productName != null
                        ? productName
                        : "Product"
        );


        tvBrand.setText(
                "Brand: " +
                        (brand != null
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
                        (shelf != null
                                ? shelf
                                : "N/A")
        );


        tvDescription.setText(
                description != null
                        ? description
                        : "No description available"
        );


        tvBarcode.setText(
                "Barcode: " +
                        (barcode != null
                                ? barcode
                                : "N/A")
        );


        // =========================
        // ADD TO CART
        // =========================

        btnAddToCart.setOnClickListener(
                v -> {

                    Toast.makeText(
                            ProductDetailsActivity.this,
                            productName +
                                    " added to cart",
                            Toast.LENGTH_SHORT
                    ).show();

                }
        );
    }
}