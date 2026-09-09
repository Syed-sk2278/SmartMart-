package com.example.smartmartplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    // =========================================================
    // MAIN LAYOUT
    // =========================================================

    private ScrollView main;


    // =========================================================
    // HEADER
    // =========================================================

    private TextView tvProfileCircle;
    private TextView tvUserName;
    private TextView tvPoints;


    // =========================================================
    // SELECTED STORE
    // =========================================================

    private LinearLayout layoutSelectedStore;
    private TextView tvStoreName;
    private TextView tvChangeStore;


    // =========================================================
    // BEFORE STORE
    // =========================================================

    private TextView tvBeforeStoreTitle;
    private TextView tvBeforeStoreSubtitle;

    private LinearLayout cardShoppingList;
    private LinearLayout cardPurchaseHistory;
    private LinearLayout cardOffers;
    private LinearLayout cardSummary;

    private TextView tvListCount;
    private TextView tvOrderCount;
    private TextView tvOfferCount;
    private TextView tvMonthlySpent;


    // =========================================================
    // STORE QR
    // =========================================================

    private LinearLayout layoutStoreQR;
    private Button btnStoreQR;


    // =========================================================
    // INSIDE STORE
    // =========================================================

    private TextView tvInsideStoreTitle;
    private TextView tvInsideStoreSubtitle;

    private LinearLayout btnScanner;
    private LinearLayout btnCart;
    private LinearLayout btnMap;
    private LinearLayout btnBilling;
    private LinearLayout btnPayment;


    // =========================================================
    // QUICK ACCESS
    // =========================================================

    private TextView tvQuickAccess;

    private Button btnHelp;
    private Button btnEmergency;
    private Button btnLostFound;
    private Button btnReport;


    // =========================================================
    // OFFER BANNER
    // =========================================================

    private LinearLayout layoutOfferBanner;
    private Button btnViewOffers;


    // =========================================================
    // LOGOUT
    // =========================================================

    private Button btnLogout;


    // =========================================================
    // STORE STATUS
    // =========================================================

    private boolean insideStore = false;

    private static final String PREF_NAME =
            "SmartMartPrefs";

    private static final String KEY_STORE_VERIFIED =
            "STORE_VERIFIED";

    private static final String KEY_STORE_ID =
            "STORE_ID";

    private static final String KEY_STORE_NAME =
            "STORE_NAME";

    private static final String KEY_STORE_ADDRESS =
            "STORE_ADDRESS";

    private static final String KEY_USER_NAME =
            "USER_NAME";


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        initializeViews();

        loadUserAndStoreData();

        setupClickListeners();

        checkStoreStatus();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        // Main
        main = findViewById(R.id.main);


        // Header
        tvProfileCircle =
                findViewById(R.id.tvProfileCircle);

        tvUserName =
                findViewById(R.id.tvUserName);

        tvPoints =
                findViewById(R.id.tvPoints);


        // Selected Store
        layoutSelectedStore =
                findViewById(R.id.layoutSelectedStore);

        tvStoreName =
                findViewById(R.id.tvStoreName);

        tvChangeStore =
                findViewById(R.id.tvChangeStore);


        // Before Store
        tvBeforeStoreTitle =
                findViewById(R.id.tvBeforeStoreTitle);

        tvBeforeStoreSubtitle =
                findViewById(R.id.tvBeforeStoreSubtitle);

        cardShoppingList =
                findViewById(R.id.cardShoppingList);

        cardPurchaseHistory =
                findViewById(R.id.cardPurchaseHistory);

        cardOffers =
                findViewById(R.id.cardOffers);

        cardSummary =
                findViewById(R.id.cardSummary);

        tvListCount =
                findViewById(R.id.tvListCount);

        tvOrderCount =
                findViewById(R.id.tvOrderCount);

        tvOfferCount =
                findViewById(R.id.tvOfferCount);

        tvMonthlySpent =
                findViewById(R.id.tvMonthlySpent);


        // Store QR
        layoutStoreQR =
                findViewById(R.id.layoutStoreQR);

        btnStoreQR =
                findViewById(R.id.btnStoreQR);


        // Inside Store
        tvInsideStoreTitle =
                findViewById(R.id.tvInsideStoreTitle);

        tvInsideStoreSubtitle =
                findViewById(R.id.tvInsideStoreSubtitle);

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


        // Quick Access
        tvQuickAccess =
                findViewById(R.id.tvQuickAccess);

        btnHelp =
                findViewById(R.id.btnHelp);

        btnEmergency =
                findViewById(R.id.btnEmergency);

        btnLostFound =
                findViewById(R.id.btnLostFound);

        btnReport =
                findViewById(R.id.btnReport);


        // Offer Banner
        layoutOfferBanner =
                findViewById(R.id.layoutOfferBanner);

        btnViewOffers =
                findViewById(R.id.btnViewOffers);


        // Logout
        btnLogout =
                findViewById(R.id.btnLogout);
    }


    // =========================================================
    // LOAD USER + STORE DATA
    // =========================================================

    private void loadUserAndStoreData() {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        // -----------------------------------------------------
        // USER NAME
        // -----------------------------------------------------

        String userName =
                prefs.getString(
                        KEY_USER_NAME,
                        "SmartMart User"
                );


        if (userName == null ||
                userName.trim().isEmpty()) {

            userName = "SmartMart User";
        }


        userName = userName.trim();


        // -----------------------------------------------------
        // DISPLAY USER NAME
        // -----------------------------------------------------

        tvUserName.setText(
                userName
        );


        // -----------------------------------------------------
        // DISPLAY USER INITIALS
        // -----------------------------------------------------

        tvProfileCircle.setText(
                getInitials(userName)
        );


        // -----------------------------------------------------
        // STORE NAME
        // -----------------------------------------------------

        String storeName =
                prefs.getString(
                        KEY_STORE_NAME,
                        "SmartMart+ Main Store"
                );


        if (storeName == null ||
                storeName.trim().isEmpty()) {

            storeName =
                    "SmartMart+ Main Store";
        }


        tvStoreName.setText(
                storeName
        );
    }


    // =========================================================
    // GET USER INITIALS
    // =========================================================

    private String getInitials(String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "SM";
        }


        String[] words =
                name.trim().split("\\s+");


        // -----------------------------------------------------
        // ONE-WORD NAME
        // -----------------------------------------------------

        if (words.length == 1) {

            String word = words[0];


            if (word.length() >= 2) {

                return word.substring(0, 2)
                        .toUpperCase(Locale.getDefault());
            }


            return word.substring(0, 1)
                    .toUpperCase(Locale.getDefault());
        }


        // -----------------------------------------------------
        // FIRST + LAST NAME
        // -----------------------------------------------------

        String first =
                words[0].substring(0, 1);

        String last =
                words[words.length - 1]
                        .substring(0, 1);


        return (first + last)
                .toUpperCase(Locale.getDefault());
    }


    // =========================================================
    // CHECK STORE STATUS
    // =========================================================

    private void checkStoreStatus() {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        boolean verified =
                prefs.getBoolean(
                        KEY_STORE_VERIFIED,
                        false
                );


        if (verified) {

            insideStore = true;

            applyInsideStoreTheme();

        } else {

            insideStore = false;

            applyBeforeStoreTheme();
        }
    }


    // =========================================================
    // BEFORE STORE THEME
    // =========================================================

    private void applyBeforeStoreTheme() {

        // White background
        main.setBackgroundColor(
                Color.WHITE
        );


        // Store QR visible
        layoutStoreQR.setVisibility(
                View.VISIBLE
        );


        // Before store text
        tvBeforeStoreTitle.setText(
                "🛍  Before you enter the store"
        );

        tvBeforeStoreSubtitle.setText(
                "Plan ahead for a smarter shopping experience."
        );


        // Inside store title
        tvInsideStoreTitle.setText(
                "🏪  Once you're inside the store"
        );

        tvInsideStoreSubtitle.setText(
                "Access all self-billing features."
        );


        // Normal cards
        cardShoppingList.setBackgroundResource(
                R.drawable.home_card_bg
        );

        cardPurchaseHistory.setBackgroundResource(
                R.drawable.home_card_bg
        );

        cardOffers.setBackgroundResource(
                R.drawable.home_card_bg
        );

        cardSummary.setBackgroundResource(
                R.drawable.home_card_bg
        );


        // Store cards
        btnScanner.setBackgroundResource(
                R.drawable.home_store_card
        );

        btnCart.setBackgroundResource(
                R.drawable.home_store_card
        );

        btnMap.setBackgroundResource(
                R.drawable.home_store_card
        );

        btnBilling.setBackgroundResource(
                R.drawable.home_store_card
        );

        btnPayment.setBackgroundResource(
                R.drawable.home_store_card
        );


        // Selected store
        layoutSelectedStore.setBackgroundResource(
                R.drawable.home_store_bg
        );


        // Quick access
        btnHelp.setBackgroundResource(
                R.drawable.home_small_button
        );

        btnEmergency.setBackgroundResource(
                R.drawable.home_small_button
        );

        btnLostFound.setBackgroundResource(
                R.drawable.home_small_button
        );

        btnReport.setBackgroundResource(
                R.drawable.home_small_button
        );


        // Offer button
        btnViewOffers.setBackgroundResource(
                R.drawable.home_offer_button
        );


        // Logout
        btnLogout.setBackgroundResource(
                R.drawable.home_logout_bg
        );


        // Restore change store color
        tvChangeStore.setTextColor(
                Color.parseColor("#5632C7")
        );


        // Restore quick access color
        tvQuickAccess.setTextColor(
                Color.parseColor("#172033")
        );
    }


    // =========================================================
    // INSIDE STORE BLUE THEME
    // =========================================================

    private void applyInsideStoreTheme() {

        // Blue background
        main.setBackgroundColor(
                Color.parseColor("#EAF4FF")
        );


        // Hide store QR
        layoutStoreQR.setVisibility(
                View.GONE
        );


        // Before store section
        tvBeforeStoreTitle.setText(
                "🛒  SmartMart+ Shopping"
        );

        tvBeforeStoreSubtitle.setText(
                "You're inside the store. Start shopping smart."
        );


        // Inside store section
        tvInsideStoreTitle.setText(
                "🔵  In-Store Features"
        );

        tvInsideStoreSubtitle.setText(
                "Scan products, manage your cart and complete self-billing."
        );


        // Selected store blue
        layoutSelectedStore.setBackgroundResource(
                R.drawable.store_selected_bg
        );


        // Before-store cards blue
        cardShoppingList.setBackgroundResource(
                R.drawable.store_blue_card
        );

        cardPurchaseHistory.setBackgroundResource(
                R.drawable.store_blue_card
        );

        cardOffers.setBackgroundResource(
                R.drawable.store_blue_card
        );

        cardSummary.setBackgroundResource(
                R.drawable.store_blue_card
        );


        // Store feature cards blue
        btnScanner.setBackgroundResource(
                R.drawable.store_blue_card
        );

        btnCart.setBackgroundResource(
                R.drawable.store_blue_card
        );

        btnMap.setBackgroundResource(
                R.drawable.store_blue_card
        );

        btnBilling.setBackgroundResource(
                R.drawable.store_blue_card
        );

        btnPayment.setBackgroundResource(
                R.drawable.store_blue_card
        );


        // Quick access blue
        btnHelp.setBackgroundResource(
                R.drawable.store_quick_button
        );

        btnEmergency.setBackgroundResource(
                R.drawable.store_quick_button
        );

        btnLostFound.setBackgroundResource(
                R.drawable.store_quick_button
        );

        btnReport.setBackgroundResource(
                R.drawable.store_quick_button
        );


        // Offer button blue
        btnViewOffers.setBackgroundResource(
                R.drawable.store_offer_button
        );


        // Logout blue
        btnLogout.setBackgroundResource(
                R.drawable.store_logout_button
        );


        // Store name
        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        String storeName =
                prefs.getString(
                        KEY_STORE_NAME,
                        "SmartMart+ Main Store"
                );


        if (storeName == null ||
                storeName.trim().isEmpty()) {

            storeName =
                    "SmartMart+ Main Store";
        }


        tvStoreName.setText(
                storeName
        );


        // Change store color
        tvChangeStore.setTextColor(
                Color.parseColor("#1677FF")
        );


        // Quick access title
        tvQuickAccess.setTextColor(
                Color.parseColor("#0756C9")
        );
    }


    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {


        // =====================================================
        // SCAN STORE QR
        // =====================================================

        btnStoreQR.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    HomeActivity.this,
                                    StoreQRScannerActivity.class
                            );

                    startActivity(intent);
                }
        );


        // =====================================================
        // CHANGE STORE
        // =====================================================

        tvChangeStore.setOnClickListener(
                v -> {

                    if (insideStore) {

                        Toast.makeText(
                                HomeActivity.this,
                                "Please finish your current shopping session first.",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Intent intent =
                                new Intent(
                                        HomeActivity.this,
                                        StoreQRScannerActivity.class
                                );

                        startActivity(intent);
                    }
                }
        );


        // =====================================================
        // PRODUCT SCANNER
        // =====================================================

        btnScanner.setOnClickListener(
                v -> {

                    if (!isInsideStore()) {

                        showStoreRequiredMessage();

                        return;
                    }


                    Intent intent =
                            new Intent(
                                    HomeActivity.this,
                                    BarcodeScannerActivity.class
                            );

                    startActivity(intent);
                }
        );


        // =====================================================
        // CART
        // =====================================================

        btnCart.setOnClickListener(
                v -> {

                    if (!isInsideStore()) {

                        showStoreRequiredMessage();

                        return;
                    }


                    Intent intent =
                            new Intent(
                                    HomeActivity.this,
                                    CartActivity.class
                            );

                    startActivity(intent);
                }
        );


        // =====================================================
        // BILLING
        // =====================================================

        btnBilling.setOnClickListener(
                v -> {

                    if (!isInsideStore()) {

                        showStoreRequiredMessage();

                        return;
                    }


                    Toast.makeText(
                            HomeActivity.this,
                            "Billing module",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // PAYMENT
        // =====================================================

        btnPayment.setOnClickListener(
                v -> {

                    if (!isInsideStore()) {

                        showStoreRequiredMessage();

                        return;
                    }


                    Intent intent =
                            new Intent(
                                    HomeActivity.this,
                                    PaymentActivity.class
                            );

                    startActivity(intent);
                }
        );


        // =====================================================
        // STORE MAP
        // =====================================================

        btnMap.setOnClickListener(
                v -> {

                    if (!isInsideStore()) {

                        showStoreRequiredMessage();

                        return;
                    }


                    Toast.makeText(
                            HomeActivity.this,
                            "Store Map will open here.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // SHOPPING LIST
        // =====================================================

        cardShoppingList.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    HomeActivity.this,
                                    ShoppingListActivity.class
                            );

                    startActivity(intent);
                }
        );


        // =====================================================
        // PURCHASE HISTORY
        // =====================================================

        cardPurchaseHistory.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Purchase History",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // OFFERS
        // =====================================================

        cardOffers.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Offers and Deals",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // MONTHLY SUMMARY
        // =====================================================

        cardSummary.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Monthly Summary",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // VIEW OFFERS
        // =====================================================

        btnViewOffers.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Offers and Deals",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // HELP
        // =====================================================

        btnHelp.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Help & Support",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // EMERGENCY
        // =====================================================

        btnEmergency.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Emergency assistance",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // LOST & FOUND
        // =====================================================

        btnLostFound.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Lost & Found",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // REPORT ACTIVITY
        // =====================================================

        btnReport.setOnClickListener(
                v -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Report Activity",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );


        // =====================================================
        // LOGOUT
        // =====================================================

        btnLogout.setOnClickListener(
                v -> logout()
        );
    }


    // =========================================================
    // CHECK WHETHER USER IS INSIDE STORE
    // =========================================================

    private boolean isInsideStore() {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        return prefs.getBoolean(
                KEY_STORE_VERIFIED,
                false
        );
    }


    // =========================================================
    // STORE REQUIRED MESSAGE
    // =========================================================

    private void showStoreRequiredMessage() {

        Toast.makeText(
                HomeActivity.this,
                "Please scan the store QR code first.",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        // Clear session
        prefs.edit()
                .clear()
                .apply();


        Toast.makeText(
                HomeActivity.this,
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();


        // Go to Login
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
    }


    // =========================================================
    // WHEN RETURNING FROM OTHER ACTIVITIES
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();


        if (main != null) {

            // Reload logged-in user name
            loadUserAndStoreData();

            // Refresh store status
            checkStoreStatus();
        }
    }
}