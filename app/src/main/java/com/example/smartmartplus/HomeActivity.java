package com.example.smartmartplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    // Header
    TextView tvUserName;
    TextView tvPoints;
    TextView tvStoreName;

    // Before entering store
    LinearLayout cardShoppingList;
    LinearLayout cardPurchaseHistory;
    LinearLayout cardOffers;
    LinearLayout cardSummary;

    // Store features
    LinearLayout btnScanner;
    LinearLayout btnCart;
    LinearLayout btnMap;
    LinearLayout btnBilling;
    LinearLayout btnPayment;

    // Other buttons
    Button btnStoreQR;
    Button btnHelp;
    Button btnEmergency;
    Button btnLostFound;
    Button btnReport;
    Button btnViewOffers;
    Button btnLogout;

    // Main layout
    View mainLayout;

    // Store section text
    TextView tvBeforeStoreTitle;
    TextView tvBeforeStoreSubtitle;
    TextView tvInsideStoreTitle;
    TextView tvInsideStoreSubtitle;

    // Theme colors
    private static final int LIGHT_BACKGROUND =
            Color.parseColor("#F7F8FC");

    private static final int DARK_BACKGROUND =
            Color.parseColor("#0B1026");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initializeViews();

        loadStoreInformation();

        checkStoreVerification();

        setupClickListeners();
    }

    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        mainLayout =
                findViewById(R.id.main);

        tvUserName =
                findViewById(R.id.tvUserName);

        tvPoints =
                findViewById(R.id.tvPoints);

        tvStoreName =
                findViewById(R.id.tvStoreName);

        cardShoppingList =
                findViewById(R.id.cardShoppingList);

        cardPurchaseHistory =
                findViewById(R.id.cardPurchaseHistory);

        cardOffers =
                findViewById(R.id.cardOffers);

        cardSummary =
                findViewById(R.id.cardSummary);

        btnScanner =
                findViewById(R.id.btnScanner);

        btnCart =
                findViewById(R.id.btnCart);

        btnMap =
                findViewById(R.id.btnMap);

        btnBilling =
                findViewById(R.id.btnBilling);

        btnPayment =
                findViewById(R.id.btnPayment);

        btnStoreQR =
                findViewById(R.id.btnStoreQR);

        btnHelp =
                findViewById(R.id.btnHelp);

        btnEmergency =
                findViewById(R.id.btnEmergency);

        btnLostFound =
                findViewById(R.id.btnLostFound);

        btnReport =
                findViewById(R.id.btnReport);

        btnViewOffers =
                findViewById(R.id.btnViewOffers);

        btnLogout =
                findViewById(R.id.btnLogout);

        tvBeforeStoreTitle =
                findViewById(R.id.tvBeforeStoreTitle);

        tvBeforeStoreSubtitle =
                findViewById(R.id.tvBeforeStoreSubtitle);

        tvInsideStoreTitle =
                findViewById(R.id.tvInsideStoreTitle);

        tvInsideStoreSubtitle =
                findViewById(R.id.tvInsideStoreSubtitle);
    }

    // =========================================================
    // LOAD STORE INFORMATION
    // =========================================================

    private void loadStoreInformation() {

        String storeName =
                getSharedPreferences(
                        "SmartMartPrefs",
                        MODE_PRIVATE
                ).getString(
                        "STORE_NAME",
                        "SmartMart+ Main Store"
                );

        tvStoreName.setText(storeName);
    }

    // =========================================================
    // CHECK STORE VERIFICATION
    // =========================================================

    private void checkStoreVerification() {

        boolean storeVerified =
                getSharedPreferences(
                        "SmartMartPrefs",
                        MODE_PRIVATE
                ).getBoolean(
                        "STORE_VERIFIED",
                        false
                );

        if (storeVerified) {

            applyDarkTheme();

        } else {

            applyLightTheme();
        }
    }

    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {

        // -----------------------------------------------------
        // SHOPPING LIST
        // -----------------------------------------------------

        cardShoppingList.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            PlannerActivity.class
                    );

            startActivity(intent);
        });

        // -----------------------------------------------------
        // PURCHASE HISTORY
        // -----------------------------------------------------

        cardPurchaseHistory.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            ReceiptActivity.class
                    );

            startActivity(intent);
        });

        // -----------------------------------------------------
        // OFFERS
        // -----------------------------------------------------

        cardOffers.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Offers & Deals",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // MONTHLY SUMMARY
        // -----------------------------------------------------

        cardSummary.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Monthly Summary",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // STORE QR
        // -----------------------------------------------------

        btnStoreQR.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            StoreQRScannerActivity.class
                    );

            startActivity(intent);
        });

        // -----------------------------------------------------
        // PRODUCT SCANNER
        // -----------------------------------------------------

        btnScanner.setOnClickListener(v -> {

            boolean verified =
                    getSharedPreferences(
                            "SmartMartPrefs",
                            MODE_PRIVATE
                    ).getBoolean(
                            "STORE_VERIFIED",
                            false
                    );

            if (!verified) {

                Toast.makeText(
                        HomeActivity.this,
                        "Please scan and verify the store QR first",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            ScannerActivity.class
                    );

            startActivity(intent);
        });

        // -----------------------------------------------------
        // CART
        // -----------------------------------------------------

        btnCart.setOnClickListener(v -> {

            boolean verified =
                    getSharedPreferences(
                            "SmartMartPrefs",
                            MODE_PRIVATE
                    ).getBoolean(
                            "STORE_VERIFIED",
                            false
                    );

            if (!verified) {

                Toast.makeText(
                        HomeActivity.this,
                        "Please verify the store first",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            CartActivity.class
                    );

            startActivity(intent);
        });

        // -----------------------------------------------------
        // STORE MAP
        // -----------------------------------------------------

        btnMap.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Store Map Coming Soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // BILLING
        // -----------------------------------------------------

        btnBilling.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Billing",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // PAYMENT
        // -----------------------------------------------------

        btnPayment.setOnClickListener(v -> {

            boolean verified =
                    getSharedPreferences(
                            "SmartMartPrefs",
                            MODE_PRIVATE
                    ).getBoolean(
                            "STORE_VERIFIED",
                            false
                    );

            if (!verified) {

                Toast.makeText(
                        HomeActivity.this,
                        "Please verify the store first",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            PaymentActivity.class
                    );

            startActivity(intent);
        });

        // -----------------------------------------------------
        // VIEW OFFERS
        // -----------------------------------------------------

        btnViewOffers.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Offers & Deals",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // HELP
        // -----------------------------------------------------

        btnHelp.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Help & Support",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // EMERGENCY
        // -----------------------------------------------------

        btnEmergency.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Emergency Assistance",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // LOST & FOUND
        // -----------------------------------------------------

        btnLostFound.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Lost & Found",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // REPORT
        // -----------------------------------------------------

        btnReport.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Report Suspicious Activity",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // -----------------------------------------------------
        // LOGOUT
        // -----------------------------------------------------

        btnLogout.setOnClickListener(v -> {

            getSharedPreferences(
                    "SmartMartPrefs",
                    MODE_PRIVATE
            )
                    .edit()
                    .clear()
                    .apply();

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }

    // =========================================================
    // LIGHT THEME
    // =========================================================

    private void applyLightTheme() {

        if (mainLayout != null) {

            mainLayout.setBackgroundColor(
                    LIGHT_BACKGROUND
            );
        }

        tvUserName.setTextColor(
                Color.parseColor("#111936")
        );

        tvPoints.setTextColor(
                Color.parseColor("#4267E8")
        );

        tvStoreName.setTextColor(
                Color.parseColor("#111936")
        );

        if (tvBeforeStoreTitle != null) {

            tvBeforeStoreTitle.setTextColor(
                    Color.parseColor("#172033")
            );
        }

        if (tvInsideStoreTitle != null) {

            tvInsideStoreTitle.setTextColor(
                    Color.parseColor("#172033")
            );
        }
    }

    // =========================================================
    // DARK STORE THEME
    // =========================================================

    private void applyDarkTheme() {

        if (mainLayout != null) {

            mainLayout.setBackgroundColor(
                    DARK_BACKGROUND
            );
        }

        tvUserName.setTextColor(
                Color.WHITE
        );

        tvPoints.setTextColor(
                Color.parseColor("#5BC5FF")
        );

        tvStoreName.setTextColor(
                Color.WHITE
        );

        if (tvBeforeStoreTitle != null) {

            tvBeforeStoreTitle.setTextColor(
                    Color.WHITE
            );
        }

        if (tvInsideStoreTitle != null) {

            tvInsideStoreTitle.setTextColor(
                    Color.WHITE
            );
        }
    }

    // =========================================================
    // STORE VERIFIED
    // =========================================================

    public void storeVerified() {

        getSharedPreferences(
                "SmartMartPrefs",
                MODE_PRIVATE
        )
                .edit()
                .putBoolean(
                        "STORE_VERIFIED",
                        true
                )
                .apply();

        applyDarkTheme();

        Toast.makeText(
                HomeActivity.this,
                "Store verified successfully!",
                Toast.LENGTH_SHORT
        ).show();
    }
}