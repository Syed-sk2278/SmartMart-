package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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


        // FIND VIEWS

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


        // GET CART DATA

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


        // DISPLAY DATA

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


        // DEFAULT UPI

        rbUPI.setChecked(true);


        // BACK TO CART

        btnBackToCart.setOnClickListener(v -> {
            finish();
        });


        // PAY NOW

        btnPay.setOnClickListener(v -> {

            int selectedId =
                    rbPaymentMethodGroup
                            .getCheckedRadioButtonId();

            if (selectedId == -1) {
                return;
            }

            RadioButton selectedButton =
                    findViewById(selectedId);

            String paymentMethod =
                    selectedButton
                            .getText()
                            .toString();


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