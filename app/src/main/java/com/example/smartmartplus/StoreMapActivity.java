package com.example.smartmartplus;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StoreMapActivity extends AppCompatActivity {

    private TextView mapEntrance;
    private TextView mapDairy;
    private TextView mapSnacks;
    private TextView mapBeverages;
    private TextView mapPersonalCare;
    private TextView mapHousehold;
    private TextView mapFreshFood;
    private TextView mapBilling;
    private TextView mapExit;

    private Button btnBackHome;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_store_map
        );

        initializeViews();

        setupClickListeners();


        // =================================================
        // CHECK FOR HIGHLIGHT CATEGORY
        // =================================================

        String highlightCategory =
                getIntent().getStringExtra(
                        "HIGHLIGHT_CATEGORY"
                );

        if (highlightCategory != null
                && !highlightCategory.trim().isEmpty()) {

            highlightSection(
                    highlightCategory
            );
        }
    }


    // =================================================
    // INITIALIZE VIEWS
    // =================================================

    private void initializeViews() {

        mapEntrance =
                findViewById(
                        R.id.mapEntrance
                );

        mapDairy =
                findViewById(
                        R.id.mapDairy
                );

        mapSnacks =
                findViewById(
                        R.id.mapSnacks
                );

        mapBeverages =
                findViewById(
                        R.id.mapBeverages
                );

        mapPersonalCare =
                findViewById(
                        R.id.mapPersonalCare
                );

        mapHousehold =
                findViewById(
                        R.id.mapHousehold
                );

        mapFreshFood =
                findViewById(
                        R.id.mapFreshFood
                );

        mapBilling =
                findViewById(
                        R.id.mapBilling
                );

        mapExit =
                findViewById(
                        R.id.mapExit
                );

        btnBackHome =
                findViewById(
                        R.id.btnBackHome
                );
    }


    // =================================================
    // CLICK LISTENERS
    // =================================================

    private void setupClickListeners() {

        mapEntrance.setOnClickListener(
                v -> {

                    Toast.makeText(
                            StoreMapActivity.this,
                            "You are at the store entrance.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        mapDairy.setOnClickListener(
                v -> openSection("Dairy")
        );


        mapSnacks.setOnClickListener(
                v -> openSection("Snacks")
        );


        mapBeverages.setOnClickListener(
                v -> openSection("Beverages")
        );


        mapPersonalCare.setOnClickListener(
                v -> openSection("Personal Care")
        );


        mapHousehold.setOnClickListener(
                v -> openSection("Household")
        );


        // Existing ID remains mapFreshFood.
        // Customer-facing category is Groceries.

        mapFreshFood.setOnClickListener(
                v -> openSection("Groceries")
        );


        mapBilling.setOnClickListener(
                v -> {

                    Toast.makeText(
                            StoreMapActivity.this,
                            "Billing / Checkout area",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        mapExit.setOnClickListener(
                v -> {

                    Toast.makeText(
                            StoreMapActivity.this,
                            "Store Exit",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        btnBackHome.setOnClickListener(
                v -> finish()
        );
    }


    // =================================================
    // OPEN CATEGORY
    // =================================================

    private void openSection(
            String categoryName
    ) {

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


    // =================================================
    // HIGHLIGHT SECTION
    // =================================================

    private void highlightSection(
            String categoryName
    ) {

        String category =
                categoryName
                        .trim()
                        .toLowerCase();


        TextView selectedSection = null;


        // =================================================
        // FIND CATEGORY
        // =================================================

        if (category.equals("dairy")) {

            selectedSection =
                    mapDairy;

        } else if (category.equals("snacks")) {

            selectedSection =
                    mapSnacks;

        } else if (category.equals("beverages")) {

            selectedSection =
                    mapBeverages;

        } else if (category.equals("personal care")) {

            selectedSection =
                    mapPersonalCare;

        } else if (category.equals("household")) {

            selectedSection =
                    mapHousehold;

        } else if (
                category.equals("groceries")
                        || category.equals("fresh food")
        ) {

            selectedSection =
                    mapFreshFood;
        }


        // =================================================
        // HIGHLIGHT MATCHING SECTION
        // =================================================

        if (selectedSection != null) {

            selectedSection.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Color.rgb(
                                    255,
                                    214,
                                    92
                            )
                    )
            );


            selectedSection.setTextColor(
                    Color.rgb(
                            40,
                            55,
                            70
                    )
            );


            selectedSection.setScaleX(
                    1.04f
            );


            selectedSection.setScaleY(
                    1.04f
            );


            Toast.makeText(
                    StoreMapActivity.this,
                    "📍 "
                            + categoryName
                            + " section highlighted",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}