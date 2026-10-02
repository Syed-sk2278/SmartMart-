package com.example.smartmartplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ProductDetailsActivity
        extends AppCompatActivity {

    // =================================================
    // PRODUCT VIEWS
    // =================================================

    private ImageView imgProduct;

    private TextView tvProductName;
    private TextView tvBrand;
    private TextView tvPrice;
    private TextView tvGst;
    private TextView tvDiscount;

    // Small stock status
    private TextView tvStockStatus;

    // Large stock information box
    private TextView tvStockInfo;

    private TextView tvShelf;
    private TextView tvDescription;
    private TextView tvBarcode;

    // Shopping list status
    private TextView tvShoppingListStatus;

    private Button btnAddToCart;
    private Button btnViewCart;
    private Button btnFindOnMap;


    // =================================================
    // SHARED PREFERENCES
    // =================================================

    private static final String PREF_NAME =
            "SmartMartPrefs";

    private static final String KEY_SHOPPING_LIST =
            "SHOPPING_LIST";


    // =================================================
    // ON CREATE
    // =================================================

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

        // IMPORTANT:
        // Your XML uses tvStockInfo.
        tvStockInfo =
                findViewById(
                        R.id.tvStockInfo
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

        tvShoppingListStatus =
                findViewById(
                        R.id.tvShoppingListStatus
                );

        btnAddToCart =
                findViewById(
                        R.id.btnAddToCart
                );

        btnViewCart =
                findViewById(
                        R.id.btnViewCart
                );

        btnFindOnMap =
                findViewById(
                        R.id.btnFindOnMap
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

        String categoryName =
                getIntent().getStringExtra(
                        "CATEGORY_NAME"
                );


        // =================================================
        // SAFE PRODUCT NAME
        // =================================================

        String displayProductName;

        if (productName != null
                && !productName.trim().isEmpty()) {

            displayProductName =
                    productName.trim();

        } else {

            displayProductName =
                    "Product";
        }


        // =================================================
        // PRODUCT NAME
        // =================================================

        tvProductName.setText(
                displayProductName
        );


        // =================================================
        // BRAND
        // =================================================

        if (brand != null
                && !brand.trim().isEmpty()) {

            tvBrand.setText(
                    "Brand: " + brand
            );

        } else {

            tvBrand.setText(
                    "Brand: N/A"
            );
        }


        // =================================================
        // PRICE
        // =================================================

        tvPrice.setText(
                "₹"
                        + String.format(
                        Locale.getDefault(),
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
                        Locale.getDefault(),
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
                        Locale.getDefault(),
                        "%.0f",
                        discount
                )
                        + "%"
        );


        // =================================================
        // STOCK
        // =================================================

        if (stock > 0) {

            // Small green stock status
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


            // LARGE STOCK BOX
            tvStockInfo.setText(
                    "Stock: "
                            + stock
                            + " available"
            );

            tvStockInfo.setTextColor(
                    Color.rgb(
                            16,
                            28,
                            37
                    )
            );

        } else {

            // Small red stock status
            tvStockStatus.setText(
                    "✕  Out of Stock"
            );

            tvStockStatus.setTextColor(
                    Color.RED
            );


            // LARGE STOCK BOX
            tvStockInfo.setText(
                    "Stock: 0 available"
            );

            tvStockInfo.setTextColor(
                    Color.RED
            );
        }


        // =================================================
        // PRODUCT LOCATION
        // =================================================

        if (shelf != null
                && !shelf.trim().isEmpty()) {

            /*
             * StoreSectionActivity should pass the
             * customer-friendly location such as:
             *
             * Aisle 1 • Shelf 2
             */

            if (shelf.startsWith("Aisle")) {

                tvShelf.setText(
                        "📍 " + shelf
                );

            } else {

                tvShelf.setText(
                        "📍 " + shelf
                );
            }

        } else {

            tvShelf.setText(
                    "📍 Location unavailable"
            );
        }


        // =================================================
        // DESCRIPTION
        // =================================================

        if (description != null
                && !description.trim().isEmpty()) {

            tvDescription.setText(
                    description
            );

        } else {

            tvDescription.setText(
                    "No description available"
            );
        }


        // =================================================
        // BARCODE
        // =================================================

        if (barcode != null
                && !barcode.trim().isEmpty()) {

            tvBarcode.setText(
                    "Barcode: " + barcode
            );

        } else {

            tvBarcode.setText(
                    "Barcode: N/A"
            );
        }


        // =================================================
        // PRODUCT IMAGE
        // =================================================

        /*
         * image_url is currently NULL.
         * Keep placeholder until backend image_url
         * is populated.
         */

        imgProduct.setImageResource(
                android.R.drawable.ic_menu_gallery
        );


        // =================================================
        // CHECK SHOPPING LIST
        // =================================================

        checkShoppingList(
                displayProductName
        );


        // =================================================
        // ADD TO CART
        // =================================================

        btnAddToCart.setOnClickListener(
                v -> {

                    // -----------------------------------------
                    // STOCK CHECK
                    // -----------------------------------------

                    if (stock <= 0) {

                        Toast.makeText(
                                ProductDetailsActivity.this,
                                "Product is out of stock",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                    // -----------------------------------------
                    // CREATE CART ITEM
                    // -----------------------------------------

                    CartItem item =
                            new CartItem(

                                    productId,

                                    displayProductName,

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


                    // -----------------------------------------
                    // ADD TO CART
                    // -----------------------------------------

                    CartManager.addToCart(
                            ProductDetailsActivity.this,
                            item
                    );


                    // -----------------------------------------
                    // MESSAGE
                    // -----------------------------------------

                    Toast.makeText(
                            ProductDetailsActivity.this,
                            displayProductName
                                    + " added to cart",
                            Toast.LENGTH_SHORT
                    ).show();


                    // -----------------------------------------
                    // SHOW VIEW CART
                    // -----------------------------------------

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


        // =================================================
        // FIND PRODUCT ON MAP
        // =================================================

        btnFindOnMap.setOnClickListener(
                v -> {

                    if (categoryName == null
                            || categoryName.trim().isEmpty()) {

                        Toast.makeText(
                                ProductDetailsActivity.this,
                                "Store location is not available",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                    Intent intent =
                            new Intent(
                                    ProductDetailsActivity.this,
                                    StoreMapActivity.class
                            );


                    intent.putExtra(
                            "HIGHLIGHT_CATEGORY",
                            categoryName
                    );


                    startActivity(intent);
                }
        );
    }


    // =================================================
    // CHECK SHOPPING LIST
    // =================================================

    private void checkShoppingList(
            String productName
    ) {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        String shoppingListJson =
                prefs.getString(
                        KEY_SHOPPING_LIST,
                        "[]"
                );


        try {

            JSONArray shoppingList =
                    new JSONArray(
                            shoppingListJson
                    );


            for (int i = 0;
                 i < shoppingList.length();
                 i++) {

                JSONObject item =
                        shoppingList.getJSONObject(i);


                String listItemName =
                        item.optString(
                                "name",
                                ""
                        ).trim();


                int requestedQuantity =
                        item.optInt(
                                "quantity",
                                1
                        );


                if (isSameShoppingItem(
                        productName,
                        listItemName
                )) {

                    showOnShoppingList(
                            requestedQuantity
                    );

                    return;
                }
            }


            showNotOnShoppingList();


        } catch (JSONException e) {

            showNotOnShoppingList();
        }
    }


    // =================================================
    // COMPARE SHOPPING ITEM
    // =================================================

    private boolean isSameShoppingItem(
            String productName,
            String listItemName
    ) {

        if (TextUtils.isEmpty(productName)
                || TextUtils.isEmpty(listItemName)) {

            return false;
        }


        String product =
                productName
                        .trim()
                        .toLowerCase(
                                Locale.getDefault()
                        );


        String listItem =
                listItemName
                        .trim()
                        .toLowerCase(
                                Locale.getDefault()
                        );


        if (product.equals(listItem)) {

            return true;
        }


        if (product.contains(listItem)) {

            return true;
        }


        if (listItem.contains(product)) {

            return true;
        }


        return false;
    }


    // =================================================
    // SHOW ON SHOPPING LIST
    // =================================================

    private void showOnShoppingList(
            int requestedQuantity
    ) {

        if (requestedQuantity <= 0) {

            requestedQuantity = 1;
        }


        tvShoppingListStatus.setText(
                "🛒  On Your Shopping List\n"
                        + "Requested quantity: "
                        + requestedQuantity
        );


        tvShoppingListStatus.setTextColor(
                Color.rgb(
                        8,
                        127,
                        114
                )
        );


        GradientDrawable background =
                new GradientDrawable();


        background.setColor(
                Color.rgb(
                        217,
                        239,
                        233
                )
        );


        background.setCornerRadius(
                24
        );


        tvShoppingListStatus.setBackground(
                background
        );


        tvShoppingListStatus.setVisibility(
                View.VISIBLE
        );
    }


    // =================================================
    // NOT ON SHOPPING LIST
    // =================================================

    private void showNotOnShoppingList() {

        tvShoppingListStatus.setText(
                "ℹ  Not on Your Shopping List,\n"
                        + "but please continue shopping."
        );


        tvShoppingListStatus.setTextColor(
                Color.rgb(
                        82,
                        101,
                        108
                )
        );


        GradientDrawable background =
                new GradientDrawable();


        background.setColor(
                Color.rgb(
                        240,
                        244,
                        243
                )
        );


        background.setCornerRadius(
                24
        );


        tvShoppingListStatus.setBackground(
                background
        );


        tvShoppingListStatus.setVisibility(
                View.VISIBLE
        );
    }
}