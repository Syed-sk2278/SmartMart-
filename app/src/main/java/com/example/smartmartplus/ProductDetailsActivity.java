package com.example.smartmartplus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity
        extends AppCompatActivity {

    private ImageView imgProduct;

    private TextView tvProductName;
    private TextView tvBrand;
    private TextView tvPrice;
    private TextView tvGst;
    private TextView tvDiscount;
    private TextView tvStockStatus;
    private TextView tvShelf;
    private TextView tvDescription;
    private TextView tvBarcode;

    private Button btnAddToCart;
    private Button btnViewCart;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_product_details
        );


        // =================================================
        // FIND VIEWS
        // =================================================

        imgProduct =
                findViewById(
                        R.id.imgProduct
                );


        tvProductName =
                findViewById(
                        R.id.tvProductName
                );


        tvBrand =
                findViewById(
                        R.id.tvBrand
                );


        tvPrice =
                findViewById(
                        R.id.tvPrice
                );


        tvGst =
                findViewById(
                        R.id.tvGst
                );


        tvDiscount =
                findViewById(
                        R.id.tvDiscount
                );


        tvStockStatus =
                findViewById(
                        R.id.tvStockStatus
                );


        tvShelf =
                findViewById(
                        R.id.tvShelf
                );


        tvDescription =
                findViewById(
                        R.id.tvDescription
                );


        tvBarcode =
                findViewById(
                        R.id.tvBarcode
                );


        btnAddToCart =
                findViewById(
                        R.id.btnAddToCart
                );


        btnViewCart =
                findViewById(
                        R.id.btnViewCart
                );


        // =================================================
        // GET PRODUCT DATA
        // =================================================

        String productId =
                getIntent().getStringExtra(
                        "PRODUCT_ID"
                );


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


        String barcode =
                getIntent().getStringExtra(
                        "PRODUCT_BARCODE"
                );


        String shelf =
                getIntent().getStringExtra(
                        "PRODUCT_SHELF"
                );


        String imageUrl =
                getIntent().getStringExtra(
                        "IMAGE_URL"
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


        // =================================================
        // DISPLAY NAME
        // =================================================

        tvProductName.setText(
                productName != null
                        && !productName.isEmpty()
                        ? productName
                        : "Product"
        );


        // =================================================
        // BRAND
        // =================================================

        tvBrand.setText(
                "Brand: "
                        + (
                        brand != null
                                && !brand.isEmpty()
                                ? brand
                                : "N/A"
                )
        );


        // =================================================
        // PRICE
        // =================================================

        tvPrice.setText(
                "₹"
                        + String.format(
                        "%.2f",
                        price
                )
        );


        // =================================================
        // GST
        // =================================================

        tvGst.setText(
                "GST: "
                        + String.format(
                        "%.0f",
                        gst
                )
                        + "%"
        );


        // =================================================
        // DISCOUNT
        // =================================================

        tvDiscount.setText(
                "Discount: "
                        + String.format(
                        "%.0f",
                        discount
                )
                        + "%"
        );


        // =================================================
        // STOCK
        // =================================================

        if (stock > 0) {

            tvStockStatus.setText(
                    "✓  In Stock"
            );

            tvStockStatus.setTextColor(
                    Color.rgb(
                            10,
                            125,
                            112
                    )
            );

        } else {

            tvStockStatus.setText(
                    "✕  Out of Stock"
            );

            tvStockStatus.setTextColor(
                    Color.RED
            );
        }


        // =================================================
        // SHELF
        // =================================================

        tvShelf.setText(
                "Shelf: "
                        + (
                        shelf != null
                                && !shelf.isEmpty()
                                ? shelf
                                : "N/A"
                )
        );


        // =================================================
        // DESCRIPTION
        // =================================================

        tvDescription.setText(
                description != null
                        && !description.isEmpty()
                        ? description
                        : "No description available"
        );


        // =================================================
        // BARCODE
        // =================================================

        tvBarcode.setText(
                "Barcode: "
                        + (
                        barcode != null
                                && !barcode.isEmpty()
                                ? barcode
                                : "N/A"
                )
        );


        // =================================================
        // IMAGE
        // =================================================

        /*
         * image_url is currently NULL in Supabase.
         *
         * Therefore we keep the simple placeholder for now.
         *
         * When your friend adds image_url later,
         * we can connect an image loader.
         */

        imgProduct.setImageResource(
                android.R.drawable.ic_menu_gallery
        );


        // =================================================
        // ADD TO CART
        // =================================================

        btnAddToCart.setOnClickListener(
                v -> {

                    if (stock <= 0) {

                        Toast.makeText(
                                ProductDetailsActivity.this,
                                "Product is out of stock",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                    CartItem item =
                            new CartItem(

                                    productId,

                                    productName != null
                                            ? productName
                                            : "Product",

                                    brand != null
                                            ? brand
                                            : "N/A",

                                    barcode != null
                                            ? barcode
                                            : "",

                                    price,

                                    gst,

                                    discount,

                                    stock,

                                    shelf != null
                                            ? shelf
                                            : "",

                                    description != null
                                            ? description
                                            : "",

                                    imageUrl != null
                                            ? imageUrl
                                            : "",

                                    1
                            );


                    CartManager.addToCart(
                            ProductDetailsActivity.this,
                            item
                    );


                    Toast.makeText(
                            ProductDetailsActivity.this,
                            productName
                                    + " added to cart",
                            Toast.LENGTH_SHORT
                    ).show();


                    btnViewCart.setVisibility(
                            View.VISIBLE
                    );
                }
        );


        // =================================================
        // VIEW CART
        // =================================================

        btnViewCart.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    ProductDetailsActivity.this,
                                    CartActivity.class
                            );

                    startActivity(intent);
                }
        );
    }
}