package com.example.smartmartplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreSectionActivity extends AppCompatActivity {

    // =========================================
    // VIEWS
    // =========================================

    private TextView tvSectionIcon;
    private TextView tvSectionName;
    private TextView tvSectionStore;
    private TextView tvAboutText;

    private LinearLayout productsContainer;

    private Button btnBackMap;


    // =========================================
    // CONSTANTS
    // =========================================

    private static final String PREF_NAME =
            "SmartMartPrefs";

    private static final String KEY_STORE_ID =
            "STORE_ID";


    // =========================================
    // DATA
    // =========================================

    private String categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================================
        // INITIALIZE RETROFIT
        // =========================================

        RetrofitClient.initialize(this);


        // =========================================
        // SET LAYOUT
        // =========================================

        setContentView(
                R.layout.activity_store_section
        );


        // =========================================
        // INITIALIZE VIEWS
        // =========================================

        initializeViews();


        // =========================================
        // GET CATEGORY
        // =========================================

        categoryName =
                getIntent().getStringExtra(
                        "CATEGORY_NAME"
                );


        if (categoryName == null ||
                categoryName.trim().isEmpty()) {

            categoryName =
                    "Store Section";
        }


        // =========================================
        // DISPLAY SECTION
        // =========================================

        displaySection();


        // =========================================
        // BACK BUTTON
        // =========================================

        btnBackMap.setOnClickListener(v ->
                finish()
        );


        // =========================================
        // LOAD CATEGORY + PRODUCTS
        // =========================================

        loadCategoryAndProducts();
    }


    // =========================================
    // INITIALIZE VIEWS
    // =========================================

    private void initializeViews() {

        tvSectionIcon =
                findViewById(
                        R.id.tvSectionIcon
                );


        tvSectionName =
                findViewById(
                        R.id.tvSectionName
                );


        tvSectionStore =
                findViewById(
                        R.id.tvSectionStore
                );


        tvAboutText =
                findViewById(
                        R.id.tvAboutText
                );


        productsContainer =
                findViewById(
                        R.id.productsContainer
                );


        btnBackMap =
                findViewById(
                        R.id.btnBackMap
                );
    }


    // =========================================
    // DISPLAY SECTION
    // =========================================

    private void displaySection() {

        tvSectionName.setText(
                categoryName
        );


        tvSectionIcon.setText(
                getCategoryIcon(
                        categoryName
                )
        );


        tvSectionStore.setText(
                "📍  SmartMart+ Store"
        );


        tvAboutText.setText(
                getCategoryDescription(
                        categoryName
                )
        );
    }


    // =========================================
    // CATEGORY ICON
    // =========================================

    private String getCategoryIcon(
            String category
    ) {

        String name =
                category.toLowerCase(
                        Locale.getDefault()
                );


        if (name.contains("snack")) {

            return "🍪";
        }


        if (name.contains("dairy")) {

            return "🥛";
        }


        if (name.contains("beverage")) {

            return "🥤";
        }


        if (name.contains("personal")) {

            return "🧴";
        }


        if (name.contains("household")) {

            return "🧹";
        }


        // =========================================
        // GROCERIES
        // =========================================

        if (name.contains("grocery") ||
                name.contains("groceries")) {

            return "🛒";
        }


        return "🛒";
    }


    // =========================================
    // CATEGORY DESCRIPTION
    // =========================================

    private String getCategoryDescription(
            String category
    ) {

        String name =
                category.toLowerCase(
                        Locale.getDefault()
                );


        if (name.contains("snack")) {

            return "Find chips, biscuits, cookies and other snack products here.";
        }


        if (name.contains("dairy")) {

            return "Find milk, curd, butter and other dairy products here.";
        }


        if (name.contains("beverage")) {

            return "Find drinks, juices, soft drinks and other beverages here.";
        }


        if (name.contains("personal")) {

            return "Find personal care and hygiene products here.";
        }


        if (name.contains("household")) {

            return "Find cleaning and household products here.";
        }


        // =========================================
        // GROCERIES
        // =========================================

        if (name.contains("grocery") ||
                name.contains("groceries")) {

            return "Find rice, flour, pulses, cooking essentials and other grocery products here.";
        }


        return "Browse products available in this section.";
    }


    // =========================================
    // LOAD CATEGORY
    // =========================================

    private void loadCategoryAndProducts() {

        String categoryFilter =
                "eq." + categoryName;


        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(
                                SupabaseApi.class
                        );


        api.getCategoryByName(
                        categoryFilter
                )
                .enqueue(
                        new Callback<List<Category>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Category>> call,
                                    Response<List<Category>> response
                            ) {

                                if (!response.isSuccessful()) {

                                    showError(
                                            "Category request failed: "
                                                    + response.code()
                                    );

                                    return;
                                }


                                List<Category> categories =
                                        response.body();


                                if (categories == null ||
                                        categories.isEmpty()) {

                                    showError(
                                            "Category not found: "
                                                    + categoryName
                                    );

                                    return;
                                }


                                Category category =
                                        categories.get(0);


                                loadProducts(
                                        category.getId()
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Category>> call,
                                    Throwable t
                            ) {

                                showError(
                                        "Network error: "
                                                + t.getMessage()
                                );
                            }
                        }
                );
    }


    // =========================================
    // LOAD PRODUCTS
    // =========================================

    private void loadProducts(
            String categoryId
    ) {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        String storeId =
                prefs.getString(
                        KEY_STORE_ID,
                        null
                );


        if (storeId == null ||
                storeId.trim().isEmpty()) {

            showError(
                    "Store information not found. Please scan the Store QR again."
            );

            return;
        }


        String categoryFilter =
                "eq." + categoryId;


        String storeFilter =
                "eq." + storeId;


        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(
                                SupabaseApi.class
                        );


        api.getProductsByCategory(
                        categoryFilter,
                        storeFilter
                )
                .enqueue(
                        new Callback<List<Product>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Product>> call,
                                    Response<List<Product>> response
                            ) {

                                if (!response.isSuccessful()) {

                                    showError(
                                            "Product request failed: "
                                                    + response.code()
                                    );

                                    return;
                                }


                                List<Product> products =
                                        response.body();


                                productsContainer
                                        .removeAllViews();


                                if (products == null ||
                                        products.isEmpty()) {

                                    showNoProducts();

                                    return;
                                }


                                for (Product product :
                                        products) {

                                    addProductCard(
                                            product
                                    );
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Product>> call,
                                    Throwable t
                            ) {

                                showError(
                                        "Network error: "
                                                + t.getMessage()
                                );
                            }
                        }
                );
    }


    // =========================================
    // PRODUCT CARD
    // =========================================

    private void addProductCard(
            Product product
    ) {

        LinearLayout card =
                new LinearLayout(this);


        card.setOrientation(
                LinearLayout.HORIZONTAL
        );


        card.setGravity(
                Gravity.CENTER_VERTICAL
        );


        card.setPadding(
                16,
                16,
                16,
                16
        );


        card.setBackgroundColor(
                Color.WHITE
        );


        card.setElevation(
                4
        );


        // =====================================
        // PRODUCT ICON
        // =====================================

        TextView icon =
                new TextView(this);


        icon.setText(
                getCategoryIcon(
                        categoryName
                )
        );


        icon.setTextSize(
                34
        );


        icon.setGravity(
                Gravity.CENTER
        );


        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        65,
                        65
                );


        iconParams.setMargins(
                0,
                0,
                15,
                0
        );


        card.addView(
                icon,
                iconParams
        );


        // =====================================
        // INFORMATION
        // =====================================

        LinearLayout information =
                new LinearLayout(this);


        information.setOrientation(
                LinearLayout.VERTICAL
        );


        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );


        // =====================================
        // PRODUCT NAME
        // =====================================

        TextView name =
                new TextView(this);


        name.setText(
                product.getProductName()
        );


        name.setTextColor(
                Color.parseColor(
                        "#123B63"
                )
        );


        name.setTextSize(
                18
        );


        name.setTypeface(
                null,
                Typeface.BOLD
        );


        information.addView(
                name
        );


        // =====================================
        // BRAND
        // =====================================

        if (product.getBrand() != null &&
                !product.getBrand().trim().isEmpty()) {

            TextView brand =
                    new TextView(this);


            brand.setText(
                    product.getBrand()
            );


            brand.setTextColor(
                    Color.DKGRAY
            );


            brand.setTextSize(
                    14
            );


            information.addView(
                    brand
            );
        }


        // =====================================
        // PRICE
        // =====================================

        TextView price =
                new TextView(this);


        price.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        product.getPrice()
                )
        );


        price.setTextColor(
                Color.parseColor(
                        "#16805B"
                )
        );


        price.setTextSize(
                17
        );


        price.setTypeface(
                null,
                Typeface.BOLD
        );


        information.addView(
                price
        );


        // =====================================
        // SHELF
        // =====================================

        if (product.getShelfId() != null &&
                !product.getShelfId().trim().isEmpty()) {

            loadShelfName(
                    product.getShelfId(),
                    information
            );
        }


        // =====================================
        // STOCK
        // =====================================

        TextView stock =
                new TextView(this);


        if (product.getStockQuantity() > 0) {

            stock.setText(
                    "✓ In Stock"
            );


            stock.setTextColor(
                    Color.parseColor(
                            "#16805B"
                    )
            );

        } else {

            stock.setText(
                    "✕ Out of Stock"
            );


            stock.setTextColor(
                    Color.RED
            );
        }


        stock.setTextSize(
                13
        );


        information.addView(
                stock
        );


        // =====================================
        // ADD INFORMATION TO CARD
        // =====================================

        card.addView(
                information,
                infoParams
        );


        // =====================================
        // ARROW
        // =====================================

        TextView arrow =
                new TextView(this);


        arrow.setText(
                "›"
        );


        arrow.setTextSize(
                32
        );


        arrow.setTextColor(
                Color.parseColor(
                        "#123B63"
                )
        );


        arrow.setGravity(
                Gravity.CENTER
        );


        card.addView(
                arrow,
                new LinearLayout.LayoutParams(
                        40,
                        65
                )
        );


        // =====================================
        // CLICK PRODUCT
        // =====================================

        card.setOnClickListener(
                v -> openProductDetails(
                        product
                )
        );


        // =====================================
        // CARD MARGIN
        // =====================================

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        cardParams.setMargins(
                0,
                0,
                0,
                12
        );


        productsContainer.addView(
                card,
                cardParams
        );
    }


    // =========================================
    // LOAD SHELF INFORMATION
    // =========================================

    private void loadShelfName(
            String shelfId,
            LinearLayout information
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
                                    return;
                                }


                                List<Shelf> shelves =
                                        response.body();


                                if (shelves == null ||
                                        shelves.isEmpty()) {

                                    return;
                                }


                                Shelf shelf =
                                        shelves.get(0);


                                TextView shelfText =
                                        new TextView(
                                                StoreSectionActivity.this
                                        );


                                shelfText.setText(
                                        "📍 Shelf "
                                                + shelf.getShelfNumber()
                                );


                                shelfText.setTextColor(
                                        Color.GRAY
                                );


                                shelfText.setTextSize(
                                        13
                                );


                                information.addView(
                                        shelfText
                                );
                            }


                            @Override
                            public void onFailure(
                                    Call<List<Shelf>> call,
                                    Throwable t
                            ) {

                                // Keep the UUID hidden.
                                // Nothing is displayed if
                                // the shelf lookup fails.
                            }
                        }
                );
    }


    // =========================================
    // OPEN PRODUCT DETAILS
    // =========================================

    private void openProductDetails(
            Product product
    ) {

        Intent intent =
                new Intent(
                        StoreSectionActivity.this,
                        ProductDetailsActivity.class
                );


        intent.putExtra(
                "PRODUCT_ID",
                product.getId()
        );


        intent.putExtra(
                "PRODUCT_NAME",
                product.getProductName()
        );


        intent.putExtra(
                "PRODUCT_BRAND",
                product.getBrand()
        );


        intent.putExtra(
                "PRODUCT_DESCRIPTION",
                product.getDescription()
        );


        intent.putExtra(
                "PRODUCT_BARCODE",
                product.getBarcode()
        );


        intent.putExtra(
                "PRODUCT_PRICE",
                product.getPrice()
        );


        intent.putExtra(
                "PRODUCT_GST",
                product.getGstPercentage()
        );


        intent.putExtra(
                "PRODUCT_DISCOUNT",
                product.getDiscountPercentage()
        );


        intent.putExtra(
                "PRODUCT_STOCK",
                product.getStockQuantity()
        );


        // Keep the actual UUID internally.
        intent.putExtra(
                "PRODUCT_SHELF",
                product.getShelfId()
        );


        intent.putExtra(
                "IMAGE_URL",
                product.getImageUrl()
        );


        startActivity(intent);
    }


    // =========================================
    // NO PRODUCTS
    // =========================================

    private void showNoProducts() {

        TextView message =
                new TextView(this);


        message.setText(
                "🛒\n\nNo products found in this section."
        );


        message.setTextColor(
                Color.GRAY
        );


        message.setTextSize(
                17
        );


        message.setGravity(
                Gravity.CENTER
        );


        message.setPadding(
                20,
                40,
                20,
                40
        );


        productsContainer.addView(
                message
        );
    }


    // =========================================
    // ERROR
    // =========================================

    private void showError(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();


        productsContainer.removeAllViews();


        TextView error =
                new TextView(this);


        error.setText(
                "⚠️ " + message
        );


        error.setTextColor(
                Color.RED
        );


        error.setTextSize(
                15
        );


        error.setGravity(
                Gravity.CENTER
        );


        error.setPadding(
                20,
                30,
                20,
                30
        );


        productsContainer.addView(
                error
        );
    }
}