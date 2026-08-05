package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        // =====================================================
        // INITIALIZE VIEWS
        // =====================================================

        tvUserName = findViewById(R.id.tvUserName);
        tvPoints = findViewById(R.id.tvPoints);
        tvStoreName = findViewById(R.id.tvStoreName);

        // Before entering store
        cardShoppingList = findViewById(R.id.cardShoppingList);
        cardPurchaseHistory = findViewById(R.id.cardPurchaseHistory);
        cardOffers = findViewById(R.id.cardOffers);
        cardSummary = findViewById(R.id.cardSummary);

        // Inside store
        btnScanner = findViewById(R.id.btnScanner);
        btnCart = findViewById(R.id.btnCart);
        btnMap = findViewById(R.id.btnMap);
        btnBilling = findViewById(R.id.btnBilling);
        btnPayment = findViewById(R.id.btnPayment);

        // Other buttons
        btnStoreQR = findViewById(R.id.btnStoreQR);
        btnHelp = findViewById(R.id.btnHelp);
        btnEmergency = findViewById(R.id.btnEmergency);
        btnLostFound = findViewById(R.id.btnLostFound);
        btnReport = findViewById(R.id.btnReport);
        btnViewOffers = findViewById(R.id.btnViewOffers);
        btnLogout = findViewById(R.id.btnLogout);


        // =====================================================
        // MY SHOPPING LIST
        // WHITE THEME SCREEN
        // =====================================================

        cardShoppingList.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    PlannerActivity.class
            );

            startActivity(intent);
        });


        // =====================================================
        // PURCHASE HISTORY
        // WHITE THEME SCREEN
        // =====================================================

        cardPurchaseHistory.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    ReceiptActivity.class
            );

            startActivity(intent);
        });


        // =====================================================
        // OFFERS
        // =====================================================

        cardOffers.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Offers & Deals",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // MONTHLY SUMMARY
        // =====================================================

        cardSummary.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Monthly Summary",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // SCAN STORE QR
        // =====================================================

        btnStoreQR.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    ScannerActivity.class
            );

            startActivity(intent);
        });


        // =====================================================
        // SCAN PRODUCT
        // BLUE / IN-STORE SCREEN
        // =====================================================

        btnScanner.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    ScannerActivity.class
            );

            startActivity(intent);
        });


        // =====================================================
        // MY CART
        // BLUE / IN-STORE SCREEN
        // =====================================================

        btnCart.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    CartActivity.class
            );

            startActivity(intent);
        });


        // =====================================================
        // STORE MAP
        // =====================================================

        btnMap.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Store Map Coming Soon",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // BILLING
        // =====================================================

        btnBilling.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Billing",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // PAYMENT
        // BLUE / IN-STORE SCREEN
        // =====================================================

        btnPayment.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    PaymentActivity.class
            );

            startActivity(intent);
        });


        // =====================================================
        // VIEW OFFERS
        // =====================================================

        btnViewOffers.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Offers & Deals",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // HELP & SUPPORT
        // =====================================================

        btnHelp.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Help & Support",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // EMERGENCY
        // =====================================================

        btnEmergency.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Emergency Assistance",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // LOST & FOUND
        // =====================================================

        btnLostFound.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Lost & Found",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // REPORT ACTIVITY
        // =====================================================

        btnReport.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "Report Suspicious Activity",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // LOGOUT
        // =====================================================

        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    LoginActivity.class
            );

            // Remove HomeActivity from back stack
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();

        });

    }
}