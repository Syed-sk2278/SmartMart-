package com.example.smartmartplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

    // =====================================================
    // VIEWS
    // =====================================================

    private TextView tvSectionIcon;
    private TextView tvSectionName;
    private TextView tvSectionStore;
    private TextView tvAboutText;

    private LinearLayout productsContainer;


    // =====================================================
    // CONSTANTS
    // =====================================================

    private static final String PREF_NAME =
            "SmartMartPrefs";

    private static final String KEY_STORE_ID =
            "STORE_ID";


    // =====================================================
    // DATA
    // =====================================================

    private String categoryName;
    private String storeId;


    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_store_section
        );


        // -------------------------------------------------
        // GET VIEWS
        // -------------------------------------------------

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


        // -------------------------------------------------
        // GET CATEGORY
        // -------------------------------------------------

        categoryName =
                getIntent().getStringExtra(
                        "CATEGORY_NAME"
                );


        if (categoryName == null
                || categoryName.trim().isEmpty()) {

            categoryName = "Groceries";
        }


        categoryName =
                categoryName.trim();


        // -------------------------------------------------
        // STORE ID
        // -------------------------------------------------

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        storeId =
                prefs.getString(
                        KEY_STORE_ID,
                        null
                );


        // -------------------------------------------------
        // DISPLAY HEADER
        // -------------------------------------------------

        tvSectionName.setText(
                categoryName
        );

        tvSectionIcon.setText(
                getCategoryIcon(categoryName)
        );

        tvAboutText.setText(
                getCategoryDescription(categoryName)
        );


        // -------------------------------------------------
        // LOAD PRODUCTS
        // -------------------------------------------------

        if (storeId == null
                || storeId.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Store information is not available",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        loadCategoryAndProducts();
    }


    // =====================================================
    // CATEGORY ICON
    // =====================================================

    private String getCategoryIcon(
            String category
    ) {

        if (category == null) {
            return "🛒";
        }


        switch (
                category.toLowerCase(
                        Locale.getDefault()
                )
        ) {

            case "snacks":
                return "🍪";

            case "dairy":
                return "🥛";

            case "beverages":
                return "🥤";

            case "personal care":
                return "🧴";

            case "household":
                return "🧹";

            case "groceries":
                return "🛒";

            case "fresh food":
                return "🛒";

            default:
                return "🛒";
        }
    }


    // =====================================================
    // CATEGORY DESCRIPTION
    // =====================================================

    private String getCategoryDescription(
            String category
    ) {

        if (category == null) {
            return "Browse products available in this section.";
        }


        switch (
                category.toLowerCase(
                        Locale.getDefault()
                )
        ) {

            case "snacks":
                return "Find chips, biscuits, cookies and other snack products here.";

            case "dairy":
                return "Find milk, cheese, butter and other dairy products here.";

            case "beverages":
                return "Find juices, soft drinks, water and other beverages here.";

            case "personal care":
                return "Find personal hygiene and care products here.";

            case "household":
                return "Find cleaning and household products here.";

            case "groceries":
                return "Find grocery and everyday food products here.";

            case "fresh food":
                return "Find grocery and everyday food products here.";

            default:
                return "Browse products available in this section.";
        }
    }


    // =====================================================
    // LOAD CATEGORY
    // =====================================================

    private void loadCategoryAndProducts() {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.getCategoryByName(
                "eq." + categoryName
        ).enqueue(
                new Callback<List<Category>>() {

                    @Override
                    public void onResponse(
                            Call<List<Category>> call,
                            Response<List<Category>> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().isEmpty()) {

                            /*
                             * If the database still has
                             * "Fresh Food", allow the
                             * Groceries screen to work
                             * with that category too.
                             */

                            if (categoryName.equalsIgnoreCase(
                                    "Groceries"
                            )) {

                                loadOldFreshFoodCategory();

                            } else {

                                showError(
                                        "Category not found"
                                );
                            }

                            return;
                        }


                        Category category =
                                response.body().get(0);


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
                                "Unable to load category"
                        );
                    }
                }
        );
    }


    // =====================================================
    // GROCERIES FALLBACK
    // =====================================================

    private void loadOldFreshFoodCategory() {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.getCategoryByName(
                "eq.Fresh Food"
        ).enqueue(
                new Callback<List<Category>>() {

                    @Override
                    public void onResponse(
                            Call<List<Category>> call,
                            Response<List<Category>> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().isEmpty()) {

                            showError(
                                    "Groceries category not found"
                            );

                            return;
                        }


                        Category category =
                                response.body().get(0);


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
                                "Unable to load Groceries"
                        );
                    }
                }
        );
    }


    // =====================================================
    // LOAD PRODUCTS
    // =====================================================

    private void loadProducts(
            String categoryId
    ) {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.getProductsByCategory(
                "eq." + categoryId,
                "eq." + storeId
        ).enqueue(
                new Callback<List<Product>>() {

                    @Override
                    public void onResponse(
                            Call<List<Product>> call,
                            Response<List<Product>> response
                    ) {

                        productsContainer.removeAllViews();


                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().isEmpty()) {

                            showNoProducts();

                            return;
                        }


                        for (Product product :
                                response.body()) {

                            createProductCard(
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
                                "Unable to load products"
                        );
                    }
                }
        );
    }


    // =====================================================
    // CREATE PRODUCT CARD
    // =====================================================

    private void createProductCard(
            Product product
    ) {

        LinearLayout card =
                new LinearLayout(this);


        card.setOrientation(
                LinearLayout.VERTICAL
        );


        card.setPadding(
                22,
                20,
                22,
                20
        );


        GradientDrawable background =
                new GradientDrawable();


        background.setColor(
                Color.WHITE
        );


        background.setCornerRadius(
                8
        );


        card.setBackground(
                background
        );


        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        cardParams.setMargins(
                0,
                0,
                0,
                18
        );


        card.setLayoutParams(
                cardParams
        );


        // =================================================
        // TOP ROW
        // =================================================

        LinearLayout topRow =
                new LinearLayout(this);


        topRow.setOrientation(
                LinearLayout.HORIZONTAL
        );


        topRow.setGravity(
                Gravity.CENTER_VERTICAL
        );


        // Product icon
        TextView icon =
                new TextView(this);


        icon.setText(
                getCategoryIcon(categoryName)
        );


        icon.setTextSize(
                30
        );


        icon.setGravity(
                Gravity.CENTER
        );


        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        60,
                        60
                );


        topRow.addView(
                icon,
                iconParams
        );


        // Product name + brand
        LinearLayout nameBox =
                new LinearLayout(this);


        nameBox.setOrientation(
                LinearLayout.VERTICAL
        );


        LinearLayout.LayoutParams nameBoxParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );


        TextView productName =
                new TextView(this);


        productName.setText(
                product.getProductName()
        );


        productName.setTextColor(
                Color.rgb(
                        18,
                        59,
                        99
                )
        );


        productName.setTextSize(
                22
        );


        productName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );


        TextView brand =
                new TextView(this);


        String brandText =
                product.getBrand();


        if (brandText == null
                || brandText.trim().isEmpty()) {

            brandText = "Brand unavailable";
        }


        brand.setText(
                brandText
        );


        brand.setTextColor(
                Color.rgb(
                        100,
                        100,
                        100
                )
        );


        brand.setTextSize(
                17
        );


        nameBox.addView(
                productName
        );

        nameBox.addView(
                brand
        );


        topRow.addView(
                nameBox,
                nameBoxParams
        );


        // Price
        TextView price =
                new TextView(this);


        price.setText(
                "₹"
                        + String.format(
                        Locale.getDefault(),
                        "%.2f",
                        product.getPrice()
                )
        );


        price.setTextColor(
                Color.rgb(
                        10,
                        130,
                        115
                )
        );


        price.setTextSize(
                21
        );


        price.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );


        topRow.addView(
                price
        );


        card.addView(
                topRow
        );


        // =================================================
        // DIVIDER
        // =================================================

        View divider =
                new View(this);


        divider.setBackgroundColor(
                Color.rgb(
                        220,
                        220,
                        220
                )
        );


        LinearLayout.LayoutParams dividerParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );


        dividerParams.setMargins(
                0,
                15,
                0,
                12
        );


        card.addView(
                divider,
                dividerParams
        );


        // =================================================
        // LOCATION
        // =================================================

        TextView location =
                new TextView(this);


        location.setText(
                "📍 Loading location..."
        );


        location.setTextColor(
                Color.rgb(
                        60,
                        75,
                        82
                )
        );


        location.setTextSize(
                17
        );


        card.addView(
                location
        );


        // =================================================
        // STOCK
        // =================================================

        TextView stock =
                new TextView(this);


        if (product.getStockQuantity() > 0) {

            stock.setText(
                    "✓  In Stock • "
                            + product.getStockQuantity()
                            + " available"
            );

            stock.setTextColor(
                    Color.rgb(
                            10,
                            130,
                            115
                    )
            );

        } else {

            stock.setText(
                    "✕  Out of Stock"
            );

            stock.setTextColor(
                    Color.RED
            );
        }


        stock.setTextSize(
                17
        );


        LinearLayout.LayoutParams stockParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        stockParams.setMargins(
                0,
                10,
                0,
                0
        );


        card.addView(
                stock,
                stockParams
        );


        // =================================================
        // ADD CARD TO SCREEN
        // =================================================

        productsContainer.addView(
                card
        );


        // =================================================
        // RESOLVE SHELF LOCATION
        // =================================================

        resolveProductLocation(
                product,
                location
        );


        // =================================================
        // CLICK PRODUCT
        // =================================================

        card.setOnClickListener(
                v -> {

                    /*
                     * We cannot immediately use the
                     * location TextView because the
                     * database lookup is asynchronous.
                     *
                     * Resolve it again and then open
                     * Product Details.
                     */

                    resolveLocationAndOpenDetails(
                            product
                    );
                }
        );
    }


    // =====================================================
    // RESOLVE LOCATION FOR CARD
    // =====================================================

    private void resolveProductLocation(
            Product product,
            TextView locationView
    ) {

        String shelfId =
                product.getShelfId();


        if (shelfId == null
                || shelfId.trim().isEmpty()) {

            locationView.setText(
                    "📍 Location unavailable"
            );

            return;
        }


        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.getShelfById(
                "eq." + shelfId
        ).enqueue(
                new Callback<List<Shelf>>() {

                    @Override
                    public void onResponse(
                            Call<List<Shelf>> call,
                            Response<List<Shelf>> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().isEmpty()) {

                            locationView.setText(
                                    "📍 Location unavailable"
                            );

                            return;
                        }


                        Shelf shelf =
                                response.body().get(0);


                        String aisleId =
                                shelf.getAisleId();


                        if (aisleId == null
                                || aisleId.trim().isEmpty()) {

                            locationView.setText(
                                    "📍 Shelf "
                                            + shelf.getShelfNumber()
                            );

                            return;
                        }


                        api.getAisleById(
                                "eq." + aisleId
                        ).enqueue(
                                new Callback<List<Aisle>>() {

                                    @Override
                                    public void onResponse(
                                            Call<List<Aisle>> call,
                                            Response<List<Aisle>> response
                                    ) {

                                        if (!response.isSuccessful()
                                                || response.body() == null
                                                || response.body().isEmpty()) {

                                            locationView.setText(
                                                    "📍 Shelf "
                                                            + shelf.getShelfNumber()
                                            );

                                            return;
                                        }


                                        Aisle aisle =
                                                response.body().get(0);


                                        locationView.setText(
                                                "📍 Aisle "
                                                        + aisle.getAisleNumber()
                                                        + " • Shelf "
                                                        + shelf.getShelfNumber()
                                        );
                                    }


                                    @Override
                                    public void onFailure(
                                            Call<List<Aisle>> call,
                                            Throwable t
                                    ) {

                                        locationView.setText(
                                                "📍 Shelf "
                                                        + shelf.getShelfNumber()
                                        );
                                    }
                                }
                        );
                    }


                    @Override
                    public void onFailure(
                            Call<List<Shelf>> call,
                            Throwable t
                    ) {

                        locationView.setText(
                                "📍 Location unavailable"
                        );
                    }
                }
        );
    }


    // =====================================================
    // RESOLVE LOCATION AND OPEN PRODUCT DETAILS
    // =====================================================

    private void resolveLocationAndOpenDetails(
            Product product
    ) {

        String shelfId =
                product.getShelfId();


        if (shelfId == null
                || shelfId.trim().isEmpty()) {

            openProductDetails(
                    product,
                    "Location unavailable"
            );

            return;
        }


        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.getShelfById(
                "eq." + shelfId
        ).enqueue(
                new Callback<List<Shelf>>() {

                    @Override
                    public void onResponse(
                            Call<List<Shelf>> call,
                            Response<List<Shelf>> response
                    ) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().isEmpty()) {

                            openProductDetails(
                                    product,
                                    "Location unavailable"
                            );

                            return;
                        }


                        Shelf shelf =
                                response.body().get(0);


                        String aisleId =
                                shelf.getAisleId();


                        if (aisleId == null
                                || aisleId.trim().isEmpty()) {

                            openProductDetails(
                                    product,
                                    "Shelf "
                                            + shelf.getShelfNumber()
                            );

                            return;
                        }


                        api.getAisleById(
                                "eq." + aisleId
                        ).enqueue(
                                new Callback<List<Aisle>>() {

                                    @Override
                                    public void onResponse(
                                            Call<List<Aisle>> call,
                                            Response<List<Aisle>> response
                                    ) {

                                        String location;


                                        if (response.isSuccessful()
                                                && response.body() != null
                                                && !response.body().isEmpty()) {

                                            Aisle aisle =
                                                    response.body().get(0);


                                            location =
                                                    "Aisle "
                                                            + aisle.getAisleNumber()
                                                            + " • Shelf "
                                                            + shelf.getShelfNumber();

                                        } else {

                                            location =
                                                    "Shelf "
                                                            + shelf.getShelfNumber();
                                        }


                                        openProductDetails(
                                                product,
                                                location
                                        );
                                    }


                                    @Override
                                    public void onFailure(
                                            Call<List<Aisle>> call,
                                            Throwable t
                                    ) {

                                        openProductDetails(
                                                product,
                                                "Shelf "
                                                        + shelf.getShelfNumber()
                                        );
                                    }
                                }
                        );
                    }


                    @Override
                    public void onFailure(
                            Call<List<Shelf>> call,
                            Throwable t
                    ) {

                        openProductDetails(
                                product,
                                "Location unavailable"
                        );
                    }
                }
        );
    }


    // =====================================================
    // OPEN PRODUCT DETAILS
    // =====================================================

    private void openProductDetails(
            Product product,
            String location
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


        // IMPORTANT:
        // Pass readable location, NOT UUID.
        intent.putExtra(
                "PRODUCT_SHELF",
                location
        );


        intent.putExtra(
                "IMAGE_URL",
                product.getImageUrl()
        );


        intent.putExtra(
                "CATEGORY_NAME",
                categoryName
        );


        startActivity(intent);
    }


    // =====================================================
    // NO PRODUCTS
    // =====================================================

    private void showNoProducts() {

        TextView message =
                new TextView(this);


        message.setText(
                "No products are currently available in this section."
        );


        message.setTextColor(
                Color.rgb(
                        90,
                        90,
                        90
                )
        );


        message.setTextSize(
                18
        );


        message.setGravity(
                Gravity.CENTER
        );


        message.setPadding(
                20,
                30,
                20,
                30
        );


        productsContainer.addView(
                message
        );
    }


    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}