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

public class ReceiptActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvTransactionId;
    private TextView tvDate;
    private TextView tvItems;
    private TextView tvPaymentMethod;

    private TextView tvSubtotal;
    private TextView tvDiscount;
    private TextView tvGST;
    private TextView tvTotal;

    private Button btnVerifyReceipt;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_receipt);

        // =========================
        // SYSTEM BAR HANDLING
        // =========================

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

        // =========================
        // INITIALIZE VIEWS
        // =========================

        tvBack = findViewById(R.id.tvBack);

        tvTransactionId =
                findViewById(R.id.tvTransactionId);

        tvDate =
                findViewById(R.id.tvDate);

        tvItems =
                findViewById(R.id.tvItems);

        tvPaymentMethod =
                findViewById(R.id.tvPaymentMethod);

        tvSubtotal =
                findViewById(R.id.tvSubtotal);

        tvDiscount =
                findViewById(R.id.tvDiscount);

        tvGST =
                findViewById(R.id.tvGST);

        tvTotal =
                findViewById(R.id.tvTotal);

        btnVerifyReceipt =
                findViewById(R.id.btnVerifyReceipt);

        btnHome =
                findViewById(R.id.btnHome);


        // =========================
        // RECEIPT DETAILS
        // =========================

        tvTransactionId.setText(
                "Transaction ID: SM-" +
                        System.currentTimeMillis()
        );

        tvDate.setText("Today");

        tvItems.setText("0 items");

        tvPaymentMethod.setText("UPI");

        tvSubtotal.setText("₹0.00");

        tvDiscount.setText("- ₹0.00");

        tvGST.setText("₹0.00");

        tvTotal.setText("₹0.00");


        // =========================
        // BACK BUTTON
        // =========================

        tvBack.setOnClickListener(v -> {

            finish();

        });


        // =========================
        // VERIFY RECEIPT
        // =========================

        btnVerifyReceipt.setOnClickListener(v -> {

            Toast.makeText(
                    ReceiptActivity.this,
                    "Receipt QR verification will be added next",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =========================
        // BACK TO HOME
        // =========================

        btnHome.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ReceiptActivity.this,
                    HomeActivity.class
            );

            // Clear previous shopping screens
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            finish();

        });

    }
}