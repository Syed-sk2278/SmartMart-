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

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView tvStockStatus;
    private TextView tvShelf;
    private TextView tvDescription;
    private TextView tvBarcode;

    private TextView tvShoppingListStatus;

    private Button btnAddToCart;
    private Button btnViewCart;


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

        // Initialize Retrofit
        RetrofitClient.initialize(this);

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

        String shelfId =
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
        // STOCK STATUS
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

        /*
         * PRODUCT_SHELF contains the shelf UUID.
         *
         * We DO NOT display that UUID to the customer.
         *
         * Instead we query the shelves table and display:
         *
         * 📍 Shelf 1
         * 📍 Shelf 2
         * 📍 Shelf 3
         * 📍 Shelf 4
         */

        if (shelfId != null
                && !shelfId.trim().isEmpty()) {

            loadShelfName(
                    shelfId
            );

        } else {

            tvShelf.setText(
                    "📍 Shelf: N/A"
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
         * image_url is currently NULL in Supabase.
         *
         * Keep the placeholder until your friend
         * connects the product image URLs.
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
                    // CHECK STOCK
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

                                    shelfId != null
                                            ? shelfId
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
                    // UPDATE SHOPPING LIST
                    // -----------------------------------------

                    updateShoppingListAfterPurchase(
                            displayProductName
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
    }


    // =================================================
    // LOAD SHELF NAME
    // =================================================

    private void loadShelfName(
            String shelfId
    ) {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(
                                SupabaseApi.class
                        );


        String shelfFilter =
                "eq." + shelfId;


        api.getShelfById(
                        shelfFilter
                )
                .enqueue(
                        new Callback<List<Shelf>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Shelf>> call,
                                    Response<List<Shelf>> response
                            ) {

                                if (!response.isSuccessful()) {

                                    tvShelf.setText(
                                            "📍 Shelf: N/A"
                                    );

                                    return;
                                }


                                List<Shelf> shelves =
                                        response.body();


                                if (shelves == null ||
                                        shelves.isEmpty()) {

                                    tvShelf.setText(
                                            "📍 Shelf: N/A"
                                    );

                                    return;
                                }


                                Shelf shelf =
                                        shelves.get(0);


                                // =====================================
                                // DISPLAY CLEAN SHELF NAME
                                // =====================================

                                tvShelf.setText(
                                        "📍 Shelf "
                                                + shelf.getShelfNumber()
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Shelf>> call,
                                    Throwable t
                            ) {

                                tvShelf.setText(
                                        "📍 Shelf: N/A"
                                );
                            }
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


                int purchasedQuantity =
                        item.optInt(
                                "purchasedQuantity",
                                0
                        );


                if (isSameShoppingItem(
                        productName,
                        listItemName
                )) {

                    showShoppingListStatus(
                            requestedQuantity,
                            purchasedQuantity
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
    // UPDATE SHOPPING LIST
    // =================================================

    private void updateShoppingListAfterPurchase(
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


                if (isSameShoppingItem(
                        productName,
                        listItemName
                )) {

                    int requestedQuantity =
                            item.optInt(
                                    "quantity",
                                    1
                            );


                    int purchasedQuantity =
                            item.optInt(
                                    "purchasedQuantity",
                                    0
                            );


                    if (purchasedQuantity
                            < requestedQuantity) {

                        purchasedQuantity++;


                        item.put(
                                "purchasedQuantity",
                                purchasedQuantity
                        );


                        prefs.edit()
                                .putString(
                                        KEY_SHOPPING_LIST,
                                        shoppingList.toString()
                                )
                                .apply();


                        showShoppingListStatus(
                                requestedQuantity,
                                purchasedQuantity
                        );
                    }


                    return;
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();
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
    // SHOPPING LIST STATUS
    // =================================================

    private void showShoppingListStatus(
            int requestedQuantity,
            int purchasedQuantity
    ) {

        if (requestedQuantity <= 0) {

            requestedQuantity = 1;
        }


        if (purchasedQuantity < 0) {

            purchasedQuantity = 0;
        }


        if (purchasedQuantity > requestedQuantity) {

            purchasedQuantity =
                    requestedQuantity;
        }


        String statusText =
                "🛒  On Your Shopping List\n"
                        + "Requested quantity: "
                        + requestedQuantity;


        if (purchasedQuantity > 0) {

            statusText =
                    statusText
                            + "\n"
                            + "✓ Purchased "
                            + purchasedQuantity
                            + " of "
                            + requestedQuantity;
        }


        tvShoppingListStatus.setText(
                statusText
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