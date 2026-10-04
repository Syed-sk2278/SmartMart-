package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvPaymentTitle;
    private TextView tvPaymentItems;
    private TextView tvPaymentSubtotal;
    private TextView tvPaymentDiscount;
    private TextView tvPaymentGST;
    private TextView tvPaymentTotal;

    private RadioGroup rbPaymentMethodGroup;
    private RadioButton rbUPI;
    private RadioButton rbCard;
    private RadioButton rbCash;

    private Button btnBackToCart;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);

        // =================================================
        // FIND VIEWS
        // =================================================

        tvPaymentTitle =
                findViewById(R.id.tvPaymentTitle);

        tvPaymentItems =
                findViewById(R.id.tvPaymentItems);

        tvPaymentSubtotal =
                findViewById(R.id.tvPaymentSubtotal);

        tvPaymentDiscount =
                findViewById(R.id.tvPaymentDiscount);

        tvPaymentGST =
                findViewById(R.id.tvPaymentGST);

        tvPaymentTotal =
                findViewById(R.id.tvPaymentTotal);

        rbPaymentMethodGroup =
                findViewById(R.id.rbPaymentMethodGroup);

        rbUPI =
                findViewById(R.id.rbUPI);

        rbCard =
                findViewById(R.id.rbCard);

        rbCash =
                findViewById(R.id.rbCash);

        btnBackToCart =
                findViewById(R.id.btnBackToCart);

        btnPay =
                findViewById(R.id.btnPay);

        // =================================================
        // GET BILLING DATA
        // =================================================

        final int itemCount =
                getIntent().getIntExtra(
                        "ITEM_COUNT",
                        0
                );

        final double subtotal =
                getIntent().getDoubleExtra(
                        "SUBTOTAL",
                        0.0
                );

        final double discount =
                getIntent().getDoubleExtra(
                        "DISCOUNT",
                        0.0
                );

        final double gst =
                getIntent().getDoubleExtra(
                        "GST",
                        0.0
                );

        // BillingActivity sends BILL_TOTAL.
        // Keep TOTAL as fallback for compatibility.
        double receivedBillTotal =
                getIntent().getDoubleExtra(
                        "BILL_TOTAL",
                        -1.0
                );

        if (receivedBillTotal <= 0.0) {

            receivedBillTotal =
                    getIntent().getDoubleExtra(
                            "TOTAL",
                            subtotal - discount + gst
                    );
        }

        final double total =
                receivedBillTotal;

        // =================================================
        // DISPLAY BILLING INFORMATION
        // =================================================

        tvPaymentItems.setText(
                "Items: " + itemCount
        );

        tvPaymentSubtotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        subtotal
                )
        );

        tvPaymentDiscount.setText(
                String.format(
                        Locale.getDefault(),
                        "- ₹%.2f",
                        discount
                )
        );

        tvPaymentGST.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        gst
                )
        );

        tvPaymentTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        total
                )
        );

        // =================================================
        // DEFAULT PAYMENT METHOD
        // =================================================

        rbUPI.setChecked(true);

        // =================================================
        // CHECK PAYMENT ALLOWED
        // =================================================

        boolean paymentAllowed =
                itemCount > 0
                        && total > 0.0;

        if (!paymentAllowed) {

            btnPay.setEnabled(false);
            btnPay.setAlpha(0.45f);

        } else {

            btnPay.setEnabled(true);
            btnPay.setAlpha(1.0f);
        }

        // =================================================
        // BACK TO CART
        // =================================================

        btnBackToCart.setOnClickListener(v -> {

            finish();

        });

        // =================================================
        // PAY NOW
        // =================================================

        btnPay.setOnClickListener(v -> {

            // -------------------------------------------------
            // SAFETY CHECK
            // -------------------------------------------------

            if (itemCount <= 0 || total <= 0.0) {

                Toast.makeText(
                        PaymentActivity.this,
                        "Your cart is empty. Add products before making payment.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            // -------------------------------------------------
            // CHECK PAYMENT METHOD
            // -------------------------------------------------

            int selectedId =
                    rbPaymentMethodGroup
                            .getCheckedRadioButtonId();

            if (selectedId == -1) {

                Toast.makeText(
                        PaymentActivity.this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            RadioButton selectedButton =
                    findViewById(selectedId);

            final String paymentMethod =
                    selectedButton
                            .getText()
                            .toString();

            // -------------------------------------------------
            // OPEN RECEIPT
            // -------------------------------------------------

            Intent intent =
                    new Intent(
                            PaymentActivity.this,
                            ReceiptActivity.class
                    );

            intent.putExtra(
                    "ITEM_COUNT",
                    itemCount
            );

            intent.putExtra(
                    "SUBTOTAL",
                    subtotal
            );

            intent.putExtra(
                    "DISCOUNT",
                    discount
            );

            intent.putExtra(
                    "GST",
                    gst
            );

            intent.putExtra(
                    "TOTAL",
                    total
            );

            intent.putExtra(
                    "PAYMENT_METHOD",
                    paymentMethod
            );

            startActivity(intent);

            finish();
        });
    }
}