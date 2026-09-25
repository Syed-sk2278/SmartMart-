package com.example.smartmartplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final String PREF_NAME = "SmartMartPrefs";

    private SharedPreferences prefs;

    private TextView tvUserName;
    private TextView tvProfileCircle;
    private TextView tvPoints;
    private TextView tvStoreName;

    private TextView tvShoppingListCount;
    private TextView tvPurchaseHistoryCount;

    private LinearLayout layoutBeforeStore;
    private LinearLayout layoutStoreQR;
    private LinearLayout layoutInsideStore;
    private LinearLayout layoutQuickAccess;

    private Button btnStoreQR;
    private Button btnScanner;
    private Button btnCart;
    private Button btnMap;
    private Button btnBilling;
    private Button btnPayment;

    private Button btnHelp;
    private Button btnEmergency;
    private Button btnLostFound;
    private Button btnReport;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        prefs = getSharedPreferences(
                PREF_NAME,
                MODE_PRIVATE
        );

        initializeViews();

        loadUserData();
        loadStoreData();
        updateShoppingListCount();
        updatePurchaseHistoryCount();
        updateStoreState();

        setupClickListeners();
    }

    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        tvUserName =
                findViewById(R.id.tvUserName);

        tvProfileCircle =
                findViewById(R.id.tvProfileCircle);

        tvPoints =
                findViewById(R.id.tvPoints);

        tvStoreName =
                findViewById(R.id.tvStoreName);

        tvShoppingListCount =
                findViewById(R.id.tvShoppingListCount);

        tvPurchaseHistoryCount =
                findViewById(R.id.tvPurchaseHistoryCount);

        layoutBeforeStore =
                findViewById(R.id.layoutBeforeStore);

        layoutStoreQR =
                findViewById(R.id.layoutStoreQR);

        layoutInsideStore =
                findViewById(R.id.layoutInsideStore);

        layoutQuickAccess =
                findViewById(R.id.layoutQuickAccess);

        btnStoreQR =
                findViewById(R.id.btnStoreQR);

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

        btnHelp =
                findViewById(R.id.btnHelp);

        btnEmergency =
                findViewById(R.id.btnEmergency);

        btnLostFound =
                findViewById(R.id.btnLostFound);

        btnReport =
                findViewById(R.id.btnReport);

        btnLogout =
                findViewById(R.id.btnLogout);
    }

    // =========================================================
    // USER DATA
    // =========================================================

    private void loadUserData() {

        String userName =
                prefs.getString(
                        "USER_NAME",
                        "SmartMart User"
                );

        if (userName == null ||
                userName.trim().isEmpty()) {

            userName = "SmartMart User";
        }

        tvUserName.setText(
                "Welcome back, " + userName
        );

        tvProfileCircle.setText(
                getInitials(userName)
        );

        tvPoints.setText(
                "⭐ Smart Points 120"
        );
    }

    private String getInitials(String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            return "SM";
        }

        String[] parts =
                name.trim().split("\\s+");

        if (parts.length == 1) {

            String first =
                    parts[0];

            if (first.length() >= 2) {

                return first
                        .substring(0, 2)
                        .toUpperCase(
                                Locale.getDefault()
                        );
            }

            return first.toUpperCase(
                    Locale.getDefault()
            );
        }

        return (
                parts[0].substring(0, 1)
                        +
                        parts[parts.length - 1]
                                .substring(0, 1)
        ).toUpperCase(
                Locale.getDefault()
        );
    }

    // =========================================================
    // STORE DATA
    // =========================================================

    private void loadStoreData() {

        String storeName =
                prefs.getString(
                        "STORE_NAME",
                        "SmartMart+ Main Store"
                );

        if (storeName == null ||
                storeName.trim().isEmpty()) {

            storeName =
                    "SmartMart+ Main Store";
        }

        tvStoreName.setText(storeName);
    }

    // =========================================================
    // SHOPPING LIST COUNT
    // =========================================================

    private void updateShoppingListCount() {

        if (tvShoppingListCount == null) {
            return;
        }

        String json =
                prefs.getString(
                        "SHOPPING_LIST",
                        "[]"
                );

        try {

            JSONArray list =
                    new JSONArray(json);

            int totalItems = 0;

            for (int i = 0;
                 i < list.length();
                 i++) {

                JSONObject item =
                        list.getJSONObject(i);

                int quantity =
                        item.optInt(
                                "quantity",
                                1
                        );

                totalItems += quantity;
            }

            if (totalItems == 1) {

                tvShoppingListCount.setText(
                        "1 item"
                );

            } else {

                tvShoppingListCount.setText(
                        totalItems + " items"
                );
            }

        } catch (Exception e) {

            tvShoppingListCount.setText(
                    "0 items"
            );
        }
    }

    // =========================================================
    // PURCHASE HISTORY COUNT
    // =========================================================

    private void updatePurchaseHistoryCount() {

        if (tvPurchaseHistoryCount == null) {
            return;
        }

        String json =
                prefs.getString(
                        "PURCHASE_HISTORY",
                        "[]"
                );

        try {

            JSONArray history =
                    new JSONArray(json);

            int count =
                    history.length();

            if (count == 1) {

                tvPurchaseHistoryCount.setText(
                        "1 Order"
                );

            } else {

                tvPurchaseHistoryCount.setText(
                        count + " Orders"
                );
            }

        } catch (Exception e) {

            tvPurchaseHistoryCount.setText(
                    "0 Orders"
            );
        }
    }

    // =========================================================
    // STORE STATE
    // =========================================================

    private void updateStoreState() {

        boolean storeVerified =
                prefs.getBoolean(
                        "STORE_VERIFIED",
                        false
                );

        if (storeVerified) {

            layoutBeforeStore.setVisibility(
                    View.GONE
            );

            layoutStoreQR.setVisibility(
                    View.GONE
            );

            layoutInsideStore.setVisibility(
                    View.VISIBLE
            );

            layoutQuickAccess.setVisibility(
                    View.VISIBLE
            );

        } else {

            layoutBeforeStore.setVisibility(
                    View.VISIBLE
            );

            layoutStoreQR.setVisibility(
                    View.VISIBLE
            );

            layoutInsideStore.setVisibility(
                    View.GONE
            );

            layoutQuickAccess.setVisibility(
                    View.GONE
            );
        }
    }

    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {

        // Store QR
        btnStoreQR.setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            StoreQRScannerActivity.class
                    );

            startActivity(intent);
        });

        // Shopping List
        findViewById(
                R.id.cardShoppingList
        ).setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            ShoppingListActivity.class
                    );

            startActivity(intent);
        });

        // Purchase History
        findViewById(
                R.id.cardPurchaseHistory
        ).setOnClickListener(view -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            PurchaseHistoryActivity.class
                    );

            startActivity(intent);
        });

        // Offers
        findViewById(
                R.id.cardOffers
        ).setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Offers coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Monthly Summary
        findViewById(
                R.id.cardSummary
        ).setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Monthly Summary coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Product Scanner
        btnScanner.setOnClickListener(view -> {

            if (!isInsideStore()) {

                showStoreQRMessage();

                return;
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            BarcodeScannerActivity.class
                    );

            startActivity(intent);
        });

        // Cart
        btnCart.setOnClickListener(view -> {

            if (!isInsideStore()) {

                showStoreQRMessage();

                return;
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            CartActivity.class
                    );

            startActivity(intent);
        });

        // Store Map
        btnMap.setOnClickListener(view -> {

            if (!isInsideStore()) {

                showStoreQRMessage();

                return;
            }

            Toast.makeText(
                    HomeActivity.this,
                    "Store Map coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Billing
        btnBilling.setOnClickListener(view -> {

            if (!isInsideStore()) {

                showStoreQRMessage();

                return;
            }

            Toast.makeText(
                    HomeActivity.this,
                    "Billing is available after adding products to cart.",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Payment
        btnPayment.setOnClickListener(view -> {

            if (!isInsideStore()) {

                showStoreQRMessage();

                return;
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            PaymentActivity.class
                    );

            startActivity(intent);
        });

        // Help
        btnHelp.setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Help & Support coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Emergency
        btnEmergency.setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Emergency assistance coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Lost & Found
        btnLostFound.setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Lost & Found coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Report
        btnReport.setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Report Activity coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Change Store
        findViewById(
                R.id.tvChangeStore
        ).setOnClickListener(view -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Store selection coming soon",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // Logout
        btnLogout.setOnClickListener(
                view -> logout()
        );
    }

    // =========================================================
    // STORE VERIFICATION
    // =========================================================

    private boolean isInsideStore() {

        return prefs.getBoolean(
                "STORE_VERIFIED",
                false
        );
    }

    private void showStoreQRMessage() {

        Toast.makeText(
                HomeActivity.this,
                "Please scan the Store Entrance QR first.",
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        /*
         * IMPORTANT:
         * Do NOT use prefs.edit().clear()
         * because that deletes:
         *
         * PURCHASE_HISTORY
         * SHOPPING_LIST
         *
         * We only remove login/session information.
         */

        prefs.edit()
                .remove("ACCESS_TOKEN")
                .remove("REFRESH_TOKEN")
                .remove("IS_LOGGED_IN")
                .remove("USER_NAME")
                .remove("STORE_VERIFIED")
                .remove("STORE_ID")
                .remove("STORE_NAME")
                .apply();

        Intent intent =
                new Intent(
                        HomeActivity.this,
                        LoginActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        |
                        Intent.FLAG_ACTIVITY_NEW_TASK
                        |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // RESUME
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        if (prefs == null) {

            prefs =
                    getSharedPreferences(
                            PREF_NAME,
                            MODE_PRIVATE
                    );
        }

        loadUserData();

        loadStoreData();

        updateShoppingListCount();

        updatePurchaseHistoryCount();

        updateStoreState();
    }
}