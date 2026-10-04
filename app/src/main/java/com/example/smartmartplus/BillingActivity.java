package com.example.smartmartplus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class BillingActivity extends AppCompatActivity {

    private LinearLayout billingItemsContainer;

    private TextView tvItemCount;
    private TextView tvSubtotal;
    private TextView tvDiscount;
    private TextView tvGst;
    private TextView tvTotal;
    private TextView tvSavings;

    private Button btnProceedPayment;
    private Button btnBackCart;

    private ArrayList<CartItem> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_billing);

        initializeViews();
        setupClickListeners();
        loadBillingData();
    }

    private void initializeViews() {

        billingItemsContainer =
                findViewById(R.id.billingItemsContainer);

        tvItemCount =
                findViewById(R.id.tvItemCount);

        tvSubtotal =
                findViewById(R.id.tvSubtotal);

        tvDiscount =
                findViewById(R.id.tvDiscount);

        tvGst =
                findViewById(R.id.tvGst);

        tvTotal =
                findViewById(R.id.tvTotal);

        tvSavings =
                findViewById(R.id.tvSavings);

        btnProceedPayment =
                findViewById(R.id.btnProceedPayment);

        btnBackCart =
                findViewById(R.id.btnBackCart);
    }

    private void loadBillingData() {

        cart = CartManager.getCart(this);

        if (cart == null) {
            cart = new ArrayList<>();
        }

        displayItems();
        displaySummary();
    }

    private void displayItems() {

        billingItemsContainer.removeAllViews();

        int totalItems =
                CartManager.getTotalItems(this);

        tvItemCount.setText(
                totalItems
                        + (totalItems == 1
                        ? " item"
                        : " items")
        );

        if (cart.isEmpty()) {

            TextView emptyText =
                    new TextView(this);

            emptyText.setText(
                    "🛒  Your cart is empty"
            );

            emptyText.setTextSize(18);

            emptyText.setTextColor(
                    Color.parseColor("#66668C")
            );

            emptyText.setGravity(
                    Gravity.CENTER
            );

            emptyText.setPadding(
                    20,
                    40,
                    20,
                    40
            );

            billingItemsContainer.addView(
                    emptyText
            );

            return;
        }

        for (CartItem item : cart) {

            createBillingItemRow(item);
        }
    }

    private void createBillingItemRow(
            CartItem item
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setPadding(
                8,
                14,
                8,
                14
        );

        // =========================================
        // ICON
        // =========================================

        TextView icon =
                new TextView(this);

        icon.setText("🛍️");

        icon.setTextSize(28);

        icon.setGravity(
                Gravity.CENTER
        );

        row.addView(
                icon,
                new LinearLayout.LayoutParams(
                        55,
                        55
                )
        );

        // =========================================
        // PRODUCT INFORMATION
        // =========================================

        LinearLayout info =
                new LinearLayout(this);

        info.setOrientation(
                LinearLayout.VERTICAL
        );

        info.setPadding(
                12,
                0,
                8,
                0
        );

        TextView name =
                new TextView(this);

        String productName =
                item.getProductName();

        if (productName == null
                || productName.trim().isEmpty()) {

            productName = "Product";
        }

        name.setText(productName);

        name.setTextColor(
                Color.parseColor("#17176B")
        );

        name.setTextSize(16);

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        info.addView(name);

        TextView priceQuantity =
                new TextView(this);

        priceQuantity.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f  •  %d pcs",
                        item.getPrice(),
                        item.getQuantity()
                )
        );

        priceQuantity.setTextColor(
                Color.parseColor("#77779A")
        );

        priceQuantity.setTextSize(14);

        info.addView(
                priceQuantity
        );

        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        row.addView(
                info,
                infoParams
        );

        // =========================================
        // ITEM TOTAL
        // =========================================

        double itemTotal =
                item.getPrice()
                        * item.getQuantity();

        TextView total =
                new TextView(this);

        total.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        itemTotal
                )
        );

        total.setTextColor(
                Color.parseColor("#17176B")
        );

        total.setTextSize(17);

        total.setTypeface(
                null,
                Typeface.BOLD
        );

        total.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.addView(total);

        billingItemsContainer.addView(row);

        // =========================================
        // DIVIDER
        // =========================================

        View divider =
                new View(this);

        divider.setBackgroundColor(
                Color.parseColor("#EEEEF7")
        );

        billingItemsContainer.addView(
                divider,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                )
        );
    }

    private void displaySummary() {

        double subtotal =
                CartManager.getSubtotal(this);

        double discount =
                CartManager.getDiscountTotal(this);

        double gst =
                CartManager.getGstTotal(this);

        double grandTotal =
                CartManager.getGrandTotal(this);

        tvSubtotal.setText(
                formatRupees(subtotal)
        );

        tvDiscount.setText(
                "- " + formatRupees(discount)
        );

        tvGst.setText(
                formatRupees(gst)
        );

        tvTotal.setText(
                formatRupees(grandTotal)
        );

        if (discount > 0) {

            tvSavings.setText(
                    "💚  You saved "
                            + formatRupees(discount)
            );

        } else {

            tvSavings.setText(
                    "💚  No discount applied"
            );
        }

        // =========================================
        // PAYMENT BUTTON
        // =========================================

        if (cart.isEmpty()
                || grandTotal <= 0) {

            btnProceedPayment.setEnabled(false);

            btnProceedPayment.setAlpha(
                    0.5f
            );

        } else {

            btnProceedPayment.setEnabled(true);

            btnProceedPayment.setAlpha(
                    1.0f
            );
        }
    }

    private String formatRupees(
            double amount
    ) {

        return String.format(
                Locale.getDefault(),
                "₹%.2f",
                amount
        );
    }

    private void setupClickListeners() {

        // =========================================
        // BACK ICON
        // =========================================

        findViewById(R.id.tvBack)
                .setOnClickListener(v -> {

                    finish();
                });

        // =========================================
        // BACK TO CART
        // =========================================

        btnBackCart.setOnClickListener(v -> {

            finish();
        });

        // =========================================
        // PROCEED TO PAYMENT
        // =========================================

        btnProceedPayment.setOnClickListener(v -> {

            double total =
                    CartManager.getGrandTotal(
                            BillingActivity.this
                    );

            if (total <= 0) {

                Toast.makeText(
                        BillingActivity.this,
                        "Your cart is empty",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // =====================================
            // OPEN PAYMENT
            // =====================================

            Intent intent =
                    new Intent(
                            BillingActivity.this,
                            PaymentActivity.class
                    );

            intent.putExtra(
                    "BILL_TOTAL",
                    total
            );

            intent.putExtra(
                    "ITEM_COUNT",
                    CartManager.getTotalItems(
                            BillingActivity.this
                    )
            );

            intent.putExtra(
                    "SUBTOTAL",
                    CartManager.getSubtotal(
                            BillingActivity.this
                    )
            );

            intent.putExtra(
                    "DISCOUNT",
                    CartManager.getDiscountTotal(
                            BillingActivity.this
                    )
            );

            intent.putExtra(
                    "GST",
                    CartManager.getGstTotal(
                            BillingActivity.this
                    )
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (billingItemsContainer != null) {

            loadBillingData();
        }
    }
}