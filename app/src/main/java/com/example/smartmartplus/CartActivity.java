package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CartActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvCartCount;
    private TextView tvEmptyCart;

    private TextView tvSubtotal;
    private TextView tvDiscount;
    private TextView tvGST;
    private TextView tvTotal;

    private Button btnCheckout;
    private TextView tvContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge
        EdgeToEdge.enable(this);

        // Load Cart screen
        setContentView(R.layout.activity_cart);

        // =========================
        // SYSTEM BAR HANDLING
        // =========================

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
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

        // =========================
        // INITIALIZE VIEWS
        // =========================

        tvBack = findViewById(R.id.tvBack);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);

        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvGST = findViewById(R.id.tvGST);
        tvTotal = findViewById(R.id.tvTotal);

        btnCheckout = findViewById(R.id.btnCheckout);
        tvContinueShopping = findViewById(R.id.tvContinueShopping);

        // =========================
        // BACK BUTTON
        // =========================

        tvBack.setOnClickListener(v -> {
            finish();
        });

        // =========================
        // CONTINUE SHOPPING
        // =========================

        tvContinueShopping.setOnClickListener(v -> {

            Intent intent = new Intent(
                    CartActivity.this,
                    ScannerActivity.class
            );

            startActivity(intent);
            finish();
        });

        // =========================
        // PROCEED TO BILLING
        // =========================

        btnCheckout.setOnClickListener(v -> {

            Intent intent = new Intent(
                    CartActivity.this,
                    PaymentActivity.class
            );

            startActivity(intent);
        });

        // =========================
        // INITIAL CART VALUES
        // =========================

        tvCartCount.setText("0");
        tvEmptyCart.setText("Your cart is empty");

        tvSubtotal.setText("₹0.00");
        tvDiscount.setText("- ₹0.00");
        tvGST.setText("₹0.00");
        tvTotal.setText("₹0.00");
    }
}