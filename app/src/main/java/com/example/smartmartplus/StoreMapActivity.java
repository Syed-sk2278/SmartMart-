package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StoreMapActivity extends AppCompatActivity {

    // =========================================
    // MAP VIEWS
    // =========================================

    private TextView mapEntrance;
    private TextView mapDairy;
    private TextView mapSnacks;
    private TextView mapBeverages;
    private TextView mapPersonalCare;
    private TextView mapHousehold;

    // Keep this ID to avoid XML/Java errors.
    // The displayed category is now Groceries.
    private TextView mapFreshFood;

    private TextView mapBilling;
    private TextView mapExit;

    private Button btnBackHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store_map);

        // =========================================
        // INITIALIZE VIEWS
        // =========================================

        initializeViews();

        // =========================================
        // CLICK LISTENERS
        // =========================================

        setupClickListeners();
    }


    // =========================================
    // INITIALIZE VIEWS
    // =========================================

    private void initializeViews() {

        mapEntrance =
                findViewById(R.id.mapEntrance);

        mapDairy =
                findViewById(R.id.mapDairy);

        mapSnacks =
                findViewById(R.id.mapSnacks);

        mapBeverages =
                findViewById(R.id.mapBeverages);

        mapPersonalCare =
                findViewById(R.id.mapPersonalCare);

        mapHousehold =
                findViewById(R.id.mapHousehold);

        mapFreshFood =
                findViewById(R.id.mapFreshFood);

        mapBilling =
                findViewById(R.id.mapBilling);

        mapExit =
                findViewById(R.id.mapExit);

        btnBackHome =
                findViewById(R.id.btnBackHome);
    }


    // =========================================
    // CLICK LISTENERS
    // =========================================

    private void setupClickListeners() {

        // -----------------------------------------
        // ENTRANCE
        // -----------------------------------------

        mapEntrance.setOnClickListener(v -> {

            Toast.makeText(
                    StoreMapActivity.this,
                    "You are at the store entrance.",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // -----------------------------------------
        // DAIRY
        // -----------------------------------------

        mapDairy.setOnClickListener(v ->
                openSection("Dairy")
        );


        // -----------------------------------------
        // SNACKS
        // -----------------------------------------

        mapSnacks.setOnClickListener(v ->
                openSection("Snacks")
        );


        // -----------------------------------------
        // BEVERAGES
        // -----------------------------------------

        mapBeverages.setOnClickListener(v ->
                openSection("Beverages")
        );


        // -----------------------------------------
        // PERSONAL CARE
        // -----------------------------------------

        mapPersonalCare.setOnClickListener(v ->
                openSection("Personal Care")
        );


        // -----------------------------------------
        // HOUSEHOLD
        // -----------------------------------------

        mapHousehold.setOnClickListener(v ->
                openSection("Household")
        );


        // -----------------------------------------
        // GROCERIES
        // -----------------------------------------

        mapFreshFood.setOnClickListener(v ->
                openSection("Groceries")
        );


        // -----------------------------------------
        // BILLING
        // -----------------------------------------

        mapBilling.setOnClickListener(v -> {

            Toast.makeText(
                    StoreMapActivity.this,
                    "Billing / Checkout area",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // -----------------------------------------
        // EXIT
        // -----------------------------------------

        mapExit.setOnClickListener(v -> {

            Toast.makeText(
                    StoreMapActivity.this,
                    "Store Exit",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // -----------------------------------------
        // BACK HOME
        // -----------------------------------------

        btnBackHome.setOnClickListener(v ->
                finish()
        );
    }


    // =========================================
    // OPEN CATEGORY
    // =========================================

    private void openSection(String categoryName) {

        Intent intent =
                new Intent(
                        StoreMapActivity.this,
                        StoreSectionActivity.class
                );

        intent.putExtra(
                "CATEGORY_NAME",
                categoryName
        );

        startActivity(intent);
    }
}