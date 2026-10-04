
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

public class ProductDetailsActivity extends AppCompatActivity {

    // =========================================================
    // PRODUCT VIEWS
    // =========================================================

    private ImageView imgProduct;

    private TextView tvProductName;
    private TextView tvBrand;
    private TextView tvPrice;
    private TextView tvGst;
    private TextView tvDiscount;

    private TextView tvStockStatus;
    private TextView tvStockInfo;

    private TextView tvShelf;
    private TextView tvDescription;
    private TextView tvBarcode;

    private TextView tvShoppingListStatus;

    private Button btnAddToCart;
    private Button btnViewCart;
    private Button btnFindOnMap;


    // =========================================================
    // LOCATION
    // =========================================================

    private String resolvedShelfLocation = "";
    private String resolvedCategoryName = "";


    // =========================================================
    // SHARED PREFERENCES
    // =========================================================

    private static final String PREF_NAME =
            "SmartMartPrefs";

    private static final String KEY_SHOPPING_LIST =
            "SHOPPING_LIST";


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_product_details
        );


        // =====================================================
        // FIND VIEWS
        // =====================================================

        imgProduct =
                findViewById(R.id.imgProduct);

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

        tvStockStatus =
                findViewById(R.id.tvStockStatus);

        tvStockInfo =
                findViewById(R.id.tvStockInfo);

        tvShelf =
                findViewById(R.id.tvShelf);

        tvDescription =
                findViewById(R.id.tvDescription);

        tvBarcode =
                findViewById(R.id.tvBarcode);

        tvShoppingListStatus =
                findViewById(R.id.tvShoppingListStatus);

        btnAddToCart =
                findViewById(R.id.btnAddToCart);

        btnViewCart =
                findViewById(R.id.btnViewCart);

        btnFindOnMap =
                findViewById(R.id.btnFindOnMap);


        // =====================================================
        // GET PRODUCT DATA
        // =====================================================

        final String productId =
                getIntent().getStringExtra(
                        "PRODUCT_ID"
                );

        final String productName =
                getIntent().getStringExtra(
                        "PRODUCT_NAME"
                );

        final String brand =
                getIntent().getStringExtra(
                        "PRODUCT_BRAND"
                );

        final String description =
                getIntent().getStringExtra(
                        "PRODUCT_DESCRIPTION"
                );

        final String shelf =
                getIntent().getStringExtra(
                        "PRODUCT_SHELF"
                );

        String imageUrl =
                getIntent().getStringExtra(
                        "IMAGE_URL"
                );

        if (imageUrl == null ||
                imageUrl.trim().isEmpty()) {

            imageUrl =
                    getIntent().getStringExtra(
                            "PRODUCT_IMAGE"
                    );
        }

        final String finalImageUrl =
                imageUrl != null
                        ? imageUrl
                        : "";

        final String barcode =
                getIntent().getStringExtra(
                        "PRODUCT_BARCODE"
                );

        final double price =
                getIntent().getDoubleExtra(
                        "PRODUCT_PRICE",
                        0
                );

        final double gst =
                getIntent().getDoubleExtra(
                        "PRODUCT_GST",
                        0
                );

        final double discount =
                getIntent().getDoubleExtra(
                        "PRODUCT_DISCOUNT",
                        0
                );

        final int stock =
                getIntent().getIntExtra(
                        "PRODUCT_STOCK",
                        0
                );

        final String categoryName =
                getIntent().getStringExtra(
                        "CATEGORY_NAME"
                );


        // =====================================================
        // SAFE PRODUCT NAME
        // =====================================================

        final String finalProductName;

        if (productName != null &&
                !productName.trim().isEmpty()) {

            finalProductName =
                    productName.trim();

        } else {

            finalProductName =
                    "Product";
        }


        // =====================================================
        // CATEGORY
        // =====================================================

        if (categoryName != null &&
                !categoryName.trim().isEmpty()) {

            resolvedCategoryName =
                    normalizeCategoryName(
                            categoryName
                    );
        }


        // =====================================================
        // PRODUCT NAME
        // =====================================================

        tvProductName.setText(
                finalProductName
        );


        // =====================================================
        // BRAND
        // =====================================================

        if (brand != null &&
                !brand.trim().isEmpty()) {

            tvBrand.setText(
                    "Brand: " + brand
            );

        } else {

            tvBrand.setText(
                    "Brand: N/A"
            );
        }


        // =====================================================
        // PRICE
        // =====================================================

        tvPrice.setText(
                "₹" +
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                price
                        )
        );


        // =====================================================
        // GST
        // =====================================================

        tvGst.setText(
                "GST: " +
                        String.format(
                                Locale.getDefault(),
                                "%.0f",
                                gst
                        ) +
                        "%"
        );


        // =====================================================
        // DISCOUNT
        // =====================================================

        tvDiscount.setText(
                "Discount: " +
                        String.format(
                                Locale.getDefault(),
                                "%.0f",
                                discount
                        ) +
                        "%"
        );


        // =====================================================
        // STOCK
        // =====================================================

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

            tvStockInfo.setText(
                    "Stock: " +
                            stock +
                            " available"
            );

            tvStockInfo.setTextColor(
                    Color.rgb(
                            16,
                            28,
                            37
                    )
            );

        } else {

            tvStockStatus.setText(
                    "✕  Out of Stock"
            );

            tvStockStatus.setTextColor(
                    Color.RED
            );

            tvStockInfo.setText(
                    "Stock: 0 available"
            );

            tvStockInfo.setTextColor(
                    Color.RED
            );
        }


        // =====================================================
        // PRODUCT LOCATION
        // =====================================================

        if (shelf != null &&
                !shelf.trim().isEmpty()) {

            if (looksLikeUuid(shelf)) {

                tvShelf.setText(
                        "📍 Finding product location..."
                );

                resolveShelfLocation(
                        shelf.trim()
                );

            } else {

                resolvedShelfLocation =
                        shelf.trim();

                tvShelf.setText(
                        "📍 " +
                                resolvedShelfLocation
                );
            }

        } else {

            tvShelf.setText(
                    "📍 Location unavailable"
            );
        }


        // =====================================================
        // DESCRIPTION
        // =====================================================

        if (description != null &&
                !description.trim().isEmpty()) {

            tvDescription.setText(
                    description
            );

        } else {

            tvDescription.setText(
                    "No description available"
            );
        }


        // =====================================================
        // BARCODE
        // =====================================================

        if (barcode != null &&
                !barcode.trim().isEmpty()) {

            tvBarcode.setText(
                    "Barcode: " +
                            barcode
            );

        } else {

            tvBarcode.setText(
                    "Barcode: N/A"
            );
        }


        // =====================================================
        // IMAGE
        // =====================================================

        imgProduct.setImageResource(
                android.R.drawable.ic_menu_gallery
        );


        // =====================================================
        // SHOPPING LIST
        // =====================================================

        checkShoppingList(
                finalProductName
        );


        // =====================================================
        // FINAL BRAND
        // =====================================================

        final String finalBrand =
                brand != null
                        ? brand
                        : "N/A";

        final String finalBarcode =
                barcode != null
                        ? barcode
                        : "";

        final String finalDescription =
                description != null
                        ? description
                        : "";

        final String originalShelf =
                shelf != null
                        ? shelf
                        : "";


        // =====================================================
        // ADD TO CART
        // =====================================================

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


                    String cartShelf;

                    if (resolvedShelfLocation != null &&
                            !resolvedShelfLocation
                                    .trim()
                                    .isEmpty()) {

                        cartShelf =
                                resolvedShelfLocation;

                    } else {

                        cartShelf =
                                originalShelf;
                    }


                    CartItem item =
                            new CartItem(

                                    productId != null
                                            ? productId
                                            : "",

                                    finalProductName,

                                    finalBrand,

                                    finalBarcode,

                                    price,

                                    gst,

                                    discount,

                                    stock,

                                    cartShelf,

                                    finalDescription,

                                    finalImageUrl,

                                    1
                            );


                    CartManager.addToCart(
                            ProductDetailsActivity.this,
                            item
                    );


                    markShoppingListItemPurchased(
                            finalProductName
                    );


                    Toast.makeText(
                            ProductDetailsActivity.this,
                            finalProductName +
                                    " added to cart",
                            Toast.LENGTH_SHORT
                    ).show();


                    btnViewCart.setVisibility(
                            View.VISIBLE
                    );
                }
        );


        // =====================================================
        // VIEW CART
        // =====================================================

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


        // =====================================================
        // FIND ON MAP
        // =====================================================

        btnFindOnMap.setOnClickListener(
                v -> {

                    String category =
                            resolvedCategoryName;


                    if (category == null ||
                            category.trim().isEmpty()) {

                        Toast.makeText(
                                ProductDetailsActivity.this,
                                "Product map location is not available yet",
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
                            category
                    );


                    startActivity(intent);
                }
        );
    }


    // =========================================================
    // SHELF → AISLE
    // =========================================================

    private void resolveShelfLocation(
            final String shelfId
    ) {

        SupabaseClient
                .getApi()
                .getShelfById(
                        "eq." + shelfId
                )
                .enqueue(
                        new Callback<List<Shelf>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Shelf>> call,
                                    Response<List<Shelf>> response
                            ) {

                                // ---------------------------------
                                // CHECK RESPONSE
                                // ---------------------------------

                                if (!response.isSuccessful()) {

                                    tvShelf.setText(
                                            "📍 Location unavailable"
                                    );

                                    return;
                                }


                                if (response.body() == null ||
                                        response.body().isEmpty()) {

                                    tvShelf.setText(
                                            "📍 Location unavailable"
                                    );

                                    return;
                                }


                                // ---------------------------------
                                // GET SHELF
                                // ---------------------------------

                                Shelf shelf =
                                        response.body().get(0);


                                String aisleId =
                                        shelf.getAisleId();


                                int shelfNumber =
                                        shelf.getShelfNumber();


                                // ---------------------------------
                                // CHECK AISLE ID
                                // ---------------------------------

                                if (aisleId == null ||
                                        aisleId.trim().isEmpty()) {

                                    if (shelfNumber > 0) {

                                        resolvedShelfLocation =
                                                "Shelf " +
                                                        shelfNumber;

                                        tvShelf.setText(
                                                "📍 " +
                                                        resolvedShelfLocation
                                        );

                                    } else {

                                        tvShelf.setText(
                                                "📍 Location unavailable"
                                        );
                                    }

                                    return;
                                }


                                // ---------------------------------
                                // RESOLVE AISLE
                                // ---------------------------------

                                resolveAisleLocation(
                                        aisleId.trim(),
                                        shelfNumber
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Shelf>> call,
                                    Throwable t
                            ) {

                                tvShelf.setText(
                                        "📍 Location unavailable"
                                );
                            }
                        }
                );
    }


    // =========================================================
    // AISLE RESOLUTION
    // =========================================================

    private void resolveAisleLocation(
            final String aisleId,
            final int shelfNumber
    ) {

        SupabaseClient
                .getApi()
                .getAisleById(
                        "eq." + aisleId
                )
                .enqueue(
                        new Callback<List<Aisle>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Aisle>> call,
                                    Response<List<Aisle>> response
                            ) {

                                if (!response.isSuccessful()) {

                                    showShelfOnly(
                                            shelfNumber
                                    );

                                    return;
                                }


                                if (response.body() == null ||
                                        response.body().isEmpty()) {

                                    showShelfOnly(
                                            shelfNumber
                                    );

                                    return;
                                }


                                // ---------------------------------
                                // GET AISLE
                                // ---------------------------------

                                Aisle aisle =
                                        response.body().get(0);


                                int aisleNumber =
                                        aisle.getAisleNumber();


                                String aisleName =
                                        aisle.getName();


                                // ---------------------------------
                                // BUILD LOCATION
                                // ---------------------------------

                                String location;


                                if (aisleNumber > 0 &&
                                        shelfNumber > 0) {

                                    location =
                                            "Aisle " +
                                                    aisleNumber +
                                                    " • Shelf " +
                                                    shelfNumber;

                                } else if (aisleNumber > 0) {

                                    location =
                                            "Aisle " +
                                                    aisleNumber;

                                } else if (shelfNumber > 0) {

                                    location =
                                            "Shelf " +
                                                    shelfNumber;

                                } else {

                                    location =
                                            "";
                                }


                                // ---------------------------------
                                // DISPLAY LOCATION
                                // ---------------------------------

                                if (!location.isEmpty()) {

                                    resolvedShelfLocation =
                                            location;

                                    tvShelf.setText(
                                            "📍 " +
                                                    location
                                    );

                                } else {

                                    tvShelf.setText(
                                            "📍 Location unavailable"
                                    );
                                }


                                // ---------------------------------
                                // DETERMINE MAP CATEGORY
                                // ---------------------------------

                                String category =
                                        normalizeCategoryName(
                                                aisleName
                                        );


                                if (category.isEmpty()) {

                                    category =
                                            categoryFromAisleNumber(
                                                    aisleNumber
                                            );
                                }


                                if (!category.isEmpty()) {

                                    resolvedCategoryName =
                                            category;
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Aisle>> call,
                                    Throwable t
                            ) {

                                showShelfOnly(
                                        shelfNumber
                                );
                            }
                        }
                );
    }


    // =========================================================
    // SHELF ONLY
    // =========================================================

    private void showShelfOnly(
            int shelfNumber
    ) {

        if (shelfNumber > 0) {

            resolvedShelfLocation =
                    "Shelf " +
                            shelfNumber;

            tvShelf.setText(
                    "📍 " +
                            resolvedShelfLocation
            );

        } else {

            tvShelf.setText(
                    "📍 Location unavailable"
            );
        }
    }


    // =========================================================
    // CATEGORY FROM AISLE NUMBER
    // =========================================================

    private String categoryFromAisleNumber(
            int aisleNumber
    ) {

        switch (aisleNumber) {

            case 1:
                return "Dairy";

            case 2:
                return "Snacks";

            case 3:
                return "Beverages";

            case 4:
                return "Personal Care";

            case 5:
                return "Household";

            case 6:
                return "Groceries";

            case 7:
                return "Billing / Checkout";

            default:
                return "";
        }
    }


    // =========================================================
    // NORMALIZE CATEGORY
    // =========================================================

    private String normalizeCategoryName(
            String category
    ) {

        if (category == null ||
                category.trim().isEmpty()) {

            return "";
        }


        String value =
                category.trim();


        if (value.equalsIgnoreCase(
                "Fresh Food"
        )) {

            return "Groceries";
        }


        if (value.equalsIgnoreCase(
                "Grocery"
        )) {

            return "Groceries";
        }


        return value;
    }


    // =========================================================
    // CHECK UUID
    // =========================================================

    private boolean looksLikeUuid(
            String value
    ) {

        if (value == null) {
            return false;
        }


        return value.matches(
                "[0-9a-fA-F]{8}-" +
                        "[0-9a-fA-F]{4}-" +
                        "[0-9a-fA-F]{4}-" +
                        "[0-9a-fA-F]{4}-" +
                        "[0-9a-fA-F]{12}"
        );
    }


    // =========================================================
    // CHECK SHOPPING LIST
    // =========================================================

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


    // =========================================================
    // MARK SHOPPING LIST PURCHASED
    // =========================================================

    private void markShoppingListItemPurchased(
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


                    if (purchasedQuantity <
                            requestedQuantity) {

                        purchasedQuantity++;
                    }


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


                    return;
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


    // =========================================================
    // COMPARE SHOPPING ITEMS
    // =========================================================

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


        return listItem.contains(product);
    }


    // =========================================================
    // SHOW ON SHOPPING LIST
    // =========================================================

    private void showOnShoppingList(
            int requestedQuantity
    ) {

        if (requestedQuantity <= 0) {

            requestedQuantity = 1;
        }


        tvShoppingListStatus.setText(
                "🛒  On Your Shopping List\n" +
                        "Requested quantity: " +
                        requestedQuantity
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


    // =========================================================
    // NOT ON SHOPPING LIST
    // =========================================================

    private void showNotOnShoppingList() {

        tvShoppingListStatus.setText(
                "ℹ  Not on Your Shopping List,\n" +
                        "but please continue shopping."
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

