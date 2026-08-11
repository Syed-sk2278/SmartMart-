package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvItemCount;
    private TextView tvAmount;

    private TextView tvUPI;
    private TextView tvCard;
    private TextView tvCash;

    private LinearLayout layoutUPI;
    private LinearLayout layoutCard;
    private LinearLayout layoutCash;

    private Button btnPayNow;

    private String selectedPayment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_payment);

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

        tvItemCount = findViewById(R.id.tvItemCount);
        tvAmount = findViewById(R.id.tvAmount);

        tvUPI = findViewById(R.id.tvUPI);
        tvCard = findViewById(R.id.tvCard);
        tvCash = findViewById(R.id.tvCash);

        layoutUPI = findViewById(R.id.layoutUPI);
        layoutCard = findViewById(R.id.layoutCard);
        layoutCash = findViewById(R.id.layoutCash);

        btnPayNow = findViewById(R.id.btnPayNow);

        // =========================
        // INITIAL VALUES
        // =========================

        tvItemCount.setText("0 items");
        tvAmount.setText("₹0.00");

        // =========================
        // BACK
        // =========================

        tvBack.setOnClickListener(v -> {
            finish();
        });

        // =========================
        // UPI
        // =========================

        layoutUPI.setOnClickListener(v -> {

            selectedPayment = "UPI";

            tvUPI.setText("Selected");
            tvCard.setText("Select");
            tvCash.setText("Select");

            Toast.makeText(
                    PaymentActivity.this,
                    "UPI selected",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =========================
        // CARD
        // =========================

        layoutCard.setOnClickListener(v -> {

            selectedPayment = "Card";

            tvUPI.setText("Select");
            tvCard.setText("Selected");
            tvCash.setText("Select");

            Toast.makeText(
                    PaymentActivity.this,
                    "Card selected",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =========================
        // CASH
        // =========================

        layoutCash.setOnClickListener(v -> {

            selectedPayment = "Cash";

            tvUPI.setText("Select");
            tvCard.setText("Select");
            tvCash.setText("Selected");

            Toast.makeText(
                    PaymentActivity.this,
                    "Cash at Counter selected",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // =========================
        // PAY NOW
        // =========================

        btnPayNow.setOnClickListener(v -> {

            if (selectedPayment.isEmpty()) {

                Toast.makeText(
                        PaymentActivity.this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    PaymentActivity.this,
                    "Payment successful",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent = new Intent(
                    PaymentActivity.this,
                    ReceiptActivity.class
            );

            startActivity(intent);

            finish();
        });
    }
}