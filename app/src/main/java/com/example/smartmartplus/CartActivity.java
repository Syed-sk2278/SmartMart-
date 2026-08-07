package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CartActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvTotal;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_cart);

        // System bar handling
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
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

        // Initialize views
        tvBack = findViewById(R.id.tvBack);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);


        // =========================
        // BACK BUTTON
        // =========================

        tvBack.setOnClickListener(v -> {

            finish();

        });


        // =========================
        // CHECKOUT
        // =========================

        btnCheckout.setOnClickListener(v -> {

            Toast.makeText(
                    CartActivity.this,
                    "Proceeding to billing...",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    CartActivity.this,
                    PaymentActivity.class
            );

            startActivity(intent);

        });


        // =========================
        // INITIAL TOTAL
        // =========================

        tvTotal.setText("₹0.00");

    }
}