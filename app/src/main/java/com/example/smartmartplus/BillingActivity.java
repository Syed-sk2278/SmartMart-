package com.example.smartmartplus;

import android.content.Intent;
import android.graphics.Color;
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

    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

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

    // =========================================================
    // LOAD BILLING DATA
    // =========================================================

    private void loadBillingData() {

        cart =
                CartManager.getCart(
                        BillingActivity.this
                );

        if (cart == null) {
            cart = new ArrayList<>();
        }

        displayItems();
        displaySummary();
    }

    // =========================================================
    // DISPLAY ITEMS
    // =========================================================

    private void displayItems() {

        billingItemsContainer.removeAllViews();

        int totalItems =
                CartManager.getTotalItems(
                        BillingActivity.this
                );

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

            emptyText.setTextSize(17);
            emptyText.setTextColor(
                    Color.parseColor("#66668C")
            );

            emptyText.setGravity(
                    Gravity.CENTER
            );

            emptyText.setPadding(
                    20,
                    35,
                    20,
                    35
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

    // =========================================================
    // CREATE BILLING ITEM ROW
    // =========================================================

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

        // -----------------------------------------------------
        // PRODUCT ICON
        // -----------------------------------------------------

        TextView icon =
                new TextView(this);

        icon.setText("🛍️");
        icon.setTextSize(28);
        icon.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        55,
                        55
                );

        row.addView(
                icon,
                iconParams
        );

        // -----------------------------------------------------
        // PRODUCT INFORMATION
        // -----------------------------------------------------

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

        name.setText(
                productName
        );

        name.setTextColor(
                Color.parseColor("#17176B")
        );

        name.setTextSize(16);
        name.setTypeface(
                null,
                android.graphics.Typeface.BOLD
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

        // -----------------------------------------------------
        // ITEM TOTAL
        // -----------------------------------------------------

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
                android.graphics.Typeface.BOLD
        );

        total.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.addView(
                total
        );

        // -----------------------------------------------------
        // DIVIDER
        // -----------------------------------------------------

        billingItemsContainer.addView(
                row
        );

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

    // =========================================================
    // BILL SUMMARY
    // =========================================================

    private void displaySummary() {

        double subtotal =
                CartManager.getSubtotal(
                        BillingActivity.this
                );

        double discount =
                CartManager.getDiscountTotal(
                        BillingActivity.this
                );

        double gst =
                CartManager.getGstTotal(
                        BillingActivity.this
                );

        double grandTotal =
                CartManager.getGrandTotal(
                        BillingActivity.this
                );

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

        tvSavings.setText(
                "💚  You saved "
                        + formatRupees(discount)
        );

        // -----------------------------------------------------
        // EMPTY CART
        // -----------------------------------------------------

        if (cart.isEmpty()
                || grandTotal <= 0) {

            btnProceedPayment.setEnabled(
                    false
            );

            btnProceedPayment.setAlpha(
                    0.5f
            );

        } else {

            btnProceedPayment.setEnabled(
                    true
            );

            btnProceedPayment.setAlpha(
                    1.0f
            );
        }
    }

    // =========================================================
    // FORMAT RUPEES
    // =========================================================

    private String formatRupees(
            double amount
    ) {

        return String.format(
                Locale.getDefault(),
                "₹%.2f",
                amount
        );
    }

    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {

        // -----------------------------------------------------
        // BACK BUTTON
        // -----------------------------------------------------

        findViewById(R.id.tvBack)
                .setOnClickListener(
                        v -> finish()
                );

        // -----------------------------------------------------
        // BACK TO CART
        // -----------------------------------------------------

        btnBackCart.setOnClickListener(
                v -> finish()
        );

        // -----------------------------------------------------
        // PROCEED TO PAYMENT
        // -----------------------------------------------------

        btnProceedPayment.setOnClickListener(
                v -> {

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

                    Intent intent =
                            new Intent(
                                    BillingActivity.this,
                                    PaymentActivity.class
                            );

                    intent.putExtra(
                            "BILL_TOTAL",
                            total
                    );

                    startActivity(intent);
                }
        );
    }

    // =========================================================
    // REFRESH WHEN RETURNING
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        loadBillingData();
    }
}