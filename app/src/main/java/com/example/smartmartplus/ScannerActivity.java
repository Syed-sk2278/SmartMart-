package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScannerActivity extends AppCompatActivity {

    private Button btnStartScan;

    private TextView tvManual;
    private TextView tvBack;
    private TextView tvCartIcon;
    private TextView tvCartItems;

    private View cartInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_scanner);

        // ==========================================
        // SYSTEM BAR HANDLING
        // ==========================================

        View mainView = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(
                mainView,
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // ==========================================
        // INITIALIZE VIEWS
        // ==========================================

        btnStartScan =
                findViewById(R.id.btnStartScan);

        tvManual =
                findViewById(R.id.tvManual);

        tvBack =
                findViewById(R.id.tvBack);

        tvCartIcon =
                findViewById(R.id.tvCartIcon);

        tvCartItems =
                findViewById(R.id.tvCartItems);

        cartInfo =
                findViewById(R.id.cartInfo);

        // ==========================================
        // CHECK RETURNED BARCODE
        // ==========================================

        String barcode =
                getIntent().getStringExtra("BARCODE");

        if (barcode != null &&
                !barcode.isEmpty()) {

            tvCartItems.setText(
                    "Scanned: " + barcode
            );

            Toast.makeText(
                    ScannerActivity.this,
                    "Product scanned: " + barcode,
                    Toast.LENGTH_LONG
            ).show();
        }

        // ==========================================
        // BACK
        // ==========================================

        tvBack.setOnClickListener(v -> {
            finish();
        });

        // ==========================================
        // SCAN PRODUCT
        // ==========================================

        btnStartScan.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ScannerActivity.this,
                            BarcodeScannerActivity.class
                    );

            startActivity(intent);
        });

        // ==========================================
        // MANUAL BARCODE
        // ==========================================

        tvManual.setOnClickListener(v -> {

            Toast.makeText(
                    ScannerActivity.this,
                    "Manual barcode entry will be added next",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // ==========================================
        // CART ICON
        // ==========================================

        tvCartIcon.setOnClickListener(v -> {
            openCart();
        });

        // ==========================================
        // CART INFORMATION
        // ==========================================

        cartInfo.setOnClickListener(v -> {
            openCart();
        });
    }

    // ==========================================
    // OPEN CART
    // ==========================================

    private void openCart() {

        Intent intent =
                new Intent(
                        ScannerActivity.this,
                        CartActivity.class
                );

        startActivity(intent);
    }
}