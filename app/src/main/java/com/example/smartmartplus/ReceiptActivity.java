package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ReceiptActivity extends AppCompatActivity {

    private TextView tvReceiptTitle;
    private TextView tvReceiptNumber;
    private TextView tvReceiptDate;
    private TextView tvReceiptItems;
    private TextView tvReceiptSubtotal;
    private TextView tvReceiptDiscount;
    private TextView tvReceiptGST;
    private TextView tvReceiptTotal;
    private TextView tvReceiptPaymentMethod;

    private Button btnDone;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receipt);

        // ==========================================
        // FIND VIEWS
        // ==========================================

        tvReceiptTitle =
                findViewById(R.id.tvReceiptTitle);

        tvReceiptNumber =
                findViewById(R.id.tvReceiptNumber);

        tvReceiptDate =
                findViewById(R.id.tvReceiptDate);

        tvReceiptItems =
                findViewById(R.id.tvReceiptItems);

        tvReceiptSubtotal =
                findViewById(R.id.tvReceiptSubtotal);

        tvReceiptDiscount =
                findViewById(R.id.tvReceiptDiscount);

        tvReceiptGST =
                findViewById(R.id.tvReceiptGST);

        tvReceiptTotal =
                findViewById(R.id.tvReceiptTotal);

        tvReceiptPaymentMethod =
                findViewById(R.id.tvReceiptPaymentMethod);

        btnDone =
                findViewById(R.id.btnDone);

        btnBackHome =
                findViewById(R.id.btnBackHome);

        // ==========================================
        // GET PAYMENT DATA
        // ==========================================

        int itemCount =
                getIntent().getIntExtra(
                        "ITEM_COUNT",
                        0
                );

        double subtotal =
                getIntent().getDoubleExtra(
                        "SUBTOTAL",
                        0.0
                );

        double discount =
                getIntent().getDoubleExtra(
                        "DISCOUNT",
                        0.0
                );

        double gst =
                getIntent().getDoubleExtra(
                        "GST",
                        0.0
                );

        double total =
                getIntent().getDoubleExtra(
                        "TOTAL",
                        subtotal - discount + gst
                );

        String paymentMethod =
                getIntent().getStringExtra(
                        "PAYMENT_METHOD"
                );

        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            paymentMethod = "UPI";
        }

        // ==========================================
        // RECEIPT NUMBER
        // ==========================================

        String receiptNumber =
                "SM"
                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        tvReceiptNumber.setText(
                "Receipt No: " + receiptNumber
        );

        // ==========================================
        // DATE
        // ==========================================

        String currentDate =
                new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                ).format(new Date());

        tvReceiptDate.setText(
                "Date: " + currentDate
        );

        // ==========================================
        // DISPLAY ITEMS
        // ==========================================

        tvReceiptItems.setText(
                "Items: " + itemCount
        );

        // ==========================================
        // DISPLAY SUBTOTAL
        // ==========================================

        tvReceiptSubtotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        subtotal
                )
        );

        // ==========================================
        // DISPLAY DISCOUNT
        // ==========================================

        tvReceiptDiscount.setText(
                String.format(
                        Locale.getDefault(),
                        "- ₹%.2f",
                        discount
                )
        );

        // ==========================================
        // DISPLAY GST
        // ==========================================

        tvReceiptGST.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        gst
                )
        );

        // ==========================================
        // DISPLAY TOTAL
        // ==========================================

        tvReceiptTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        total
                )
        );

        // ==========================================
        // PAYMENT METHOD
        // ==========================================

        tvReceiptPaymentMethod.setText(
                "Payment Method: "
                        + paymentMethod
        );

        // ==========================================
        // SAVE PURCHASE HISTORY
        // ==========================================

        PurchaseHistoryActivity.savePurchase(
                ReceiptActivity.this,
                receiptNumber,
                itemCount,
                total,
                paymentMethod
        );

        // ==========================================
        // DONE
        // ==========================================

        btnDone.setOnClickListener(v -> {

            // Clear cart after successful payment
            CartManager.clearCart(
                    ReceiptActivity.this
            );

            goToHome();
        });

        // ==========================================
        // BACK TO HOME
        // ==========================================

        btnBackHome.setOnClickListener(v -> {

            // Clear cart after successful payment
            CartManager.clearCart(
                    ReceiptActivity.this
            );

            goToHome();
        });
    }

    // ==============================================
    // GO TO HOME
    // ==============================================

    private void goToHome() {

        Intent intent =
                new Intent(
                        ReceiptActivity.this,
                        HomeActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }
}