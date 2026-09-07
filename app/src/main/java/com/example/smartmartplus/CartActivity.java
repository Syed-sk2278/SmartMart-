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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private LinearLayout cartContainer;

    private TextView tvCartCount;
    private TextView tvSubtotal;
    private TextView tvDiscount;
    private TextView tvGST;
    private TextView tvTotal;

    private Button btnCheckout;
    private Button btnContinueShopping;
    private Button btnClearCart;

    private final int GREEN = Color.rgb(8, 127, 115);
    private final int DARK_TEXT = Color.rgb(16, 28, 37);
    private final int GREY_TEXT = Color.rgb(82, 101, 108);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        // ==========================================
        // FIND VIEWS
        // ==========================================

        cartContainer = findViewById(R.id.cartContainer);

        tvCartCount = findViewById(R.id.tvCartCount);

        tvSubtotal = findViewById(R.id.tvSubtotal);

        tvDiscount = findViewById(R.id.tvDiscount);

        tvGST = findViewById(R.id.tvGST);

        tvTotal = findViewById(R.id.tvTotal);

        btnCheckout = findViewById(R.id.btnCheckout);

        btnContinueShopping =
                findViewById(R.id.btnContinueShopping);

        btnClearCart =
                findViewById(R.id.btnClearCart);

        // ==========================================
        // LOAD CART
        // ==========================================

        loadCart();

        // ==========================================
        // CONTINUE SHOPPING
        // ==========================================

        btnContinueShopping.setOnClickListener(v -> {
            finish();
        });

        // ==========================================
        // CLEAR CART
        // ==========================================

        btnClearCart.setOnClickListener(v -> {

            CartManager.clearCart(
                    CartActivity.this
            );

            loadCart();
        });

        // ==========================================
        // PROCEED TO CHECKOUT
        // ==========================================

        btnCheckout.setOnClickListener(v -> {

            int itemCount =
                    CartManager.getTotalItems(
                            CartActivity.this
                    );

            if (itemCount <= 0) {

                return;
            }

            // Get actual cart values
            double subtotal =
                    CartManager.getSubtotal(
                            CartActivity.this
                    );

            double discount =
                    CartManager.getDiscountTotal(
                            CartActivity.this
                    );

            double gst =
                    CartManager.getGstTotal(
                            CartActivity.this
                    );

            double total =
                    CartManager.getGrandTotal(
                            CartActivity.this
                    );

            // ======================================
            // SEND DATA TO PAYMENT
            // ======================================

            Intent intent =
                    new Intent(
                            CartActivity.this,
                            PaymentActivity.class
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

            startActivity(intent);
        });
    }

    // =================================================
    // LOAD CART
    // =================================================

    private void loadCart() {

        cartContainer.removeAllViews();

        ArrayList<CartItem> cart =
                CartManager.getCart(this);

        int totalItems =
                CartManager.getTotalItems(this);

        // ==========================================
        // CART COUNT
        // ==========================================

        tvCartCount.setText(
                totalItems
                        + (totalItems == 1
                        ? " item"
                        : " items")
        );

        // ==========================================
        // EMPTY CART
        // ==========================================

        if (cart.isEmpty()) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "Your cart is empty\n\nScan a product to add it."
            );

            empty.setTextSize(20);

            empty.setTextColor(
                    GREY_TEXT
            );

            empty.setGravity(
                    Gravity.CENTER
            );

            empty.setPadding(
                    20,
                    80,
                    20,
                    80
            );

            cartContainer.addView(empty);

            btnCheckout.setEnabled(false);

        } else {

            btnCheckout.setEnabled(true);

            // ======================================
            // ADD PRODUCTS
            // ======================================

            for (CartItem item : cart) {

                addCartItemView(item);
            }
        }

        // ==========================================
        // CALCULATE TOTALS
        // ==========================================

        double subtotal =
                CartManager.getSubtotal(this);

        double discount =
                CartManager.getDiscountTotal(this);

        double gst =
                CartManager.getGstTotal(this);

        double total =
                CartManager.getGrandTotal(this);

        // ==========================================
        // DISPLAY SUBTOTAL
        // ==========================================

        tvSubtotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        subtotal
                )
        );

        // ==========================================
        // DISPLAY DISCOUNT
        // ==========================================

        tvDiscount.setText(
                String.format(
                        Locale.getDefault(),
                        "- ₹%.2f",
                        discount
                )
        );

        // ==========================================
        // DISPLAY GST
        // ==========================================

        tvGST.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        gst
                )
        );

        // ==========================================
        // DISPLAY TOTAL
        // ==========================================

        tvTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        total
                )
        );
    }

    // =================================================
    // ADD CART ITEM VIEW
    // =================================================

    private void addCartItemView(CartItem item) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                20,
                20,
                20,
                20
        );

        card.setBackgroundColor(
                Color.WHITE
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                18
        );

        card.setLayoutParams(cardParams);

        // ==========================================
        // PRODUCT NAME
        // ==========================================

        TextView name =
                new TextView(this);

        name.setText(
                item.getProductName()
        );

        name.setTextSize(21);

        name.setTextColor(
                DARK_TEXT
        );

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(name);

        // ==========================================
        // PRICE
        // ==========================================

        TextView price =
                new TextView(this);

        price.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f each",
                        item.getPrice()
                )
        );

        price.setTextSize(17);

        price.setTextColor(
                GREEN
        );

        LinearLayout.LayoutParams priceParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        priceParams.setMargins(
                0,
                8,
                0,
                12
        );

        price.setLayoutParams(priceParams);

        card.addView(price);

        // ==========================================
        // QUANTITY ROW
        // ==========================================

        LinearLayout quantityRow =
                new LinearLayout(this);

        quantityRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        quantityRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        // ==========================================
        // MINUS BUTTON
        // ==========================================

        Button minus =
                new Button(this);

        minus.setText("−");

        minus.setTextSize(22);

        quantityRow.addView(
                minus,
                new LinearLayout.LayoutParams(
                        60,
                        55
                )
        );

        // ==========================================
        // QUANTITY
        // ==========================================

        TextView quantity =
                new TextView(this);

        quantity.setText(
                String.valueOf(
                        item.getQuantity()
                )
        );

        quantity.setTextSize(20);

        quantity.setGravity(
                Gravity.CENTER
        );

        quantity.setTextColor(
                Color.BLACK
        );

        quantityRow.addView(
                quantity,
                new LinearLayout.LayoutParams(
                        70,
                        55
                )
        );

        // ==========================================
        // PLUS BUTTON
        // ==========================================

        Button plus =
                new Button(this);

        plus.setText("+");

        plus.setTextSize(22);

        quantityRow.addView(
                plus,
                new LinearLayout.LayoutParams(
                        60,
                        55
                )
        );

        // ==========================================
        // REMOVE BUTTON
        // ==========================================

        Button remove =
                new Button(this);

        remove.setText("Remove");

        remove.setTextColor(
                Color.RED
        );

        LinearLayout.LayoutParams removeParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        55
                );

        removeParams.setMargins(
                15,
                0,
                0,
                0
        );

        quantityRow.addView(
                remove,
                removeParams
        );

        card.addView(quantityRow);

        // ==========================================
        // ITEM TOTAL
        // ==========================================

        TextView itemTotal =
                new TextView(this);

        double itemAmount =
                item.getPrice()
                        * item.getQuantity();

        itemTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "Item total: ₹%.2f",
                        itemAmount
                )
        );

        itemTotal.setTextSize(17);

        itemTotal.setTextColor(
                GREEN
        );

        itemTotal.setTypeface(
                null,
                Typeface.BOLD
        );

        LinearLayout.LayoutParams totalParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        totalParams.setMargins(
                0,
                12,
                0,
                0
        );

        itemTotal.setLayoutParams(
                totalParams
        );

        card.addView(itemTotal);

        // ==========================================
        // PLUS CLICK
        // ==========================================

        plus.setOnClickListener(v -> {

            int currentQuantity =
                    item.getQuantity();

            if (currentQuantity < item.getStock()) {

                CartManager.updateQuantity(
                        CartActivity.this,
                        item.getProductId(),
                        currentQuantity + 1
                );

                loadCart();
            }
        });

        // ==========================================
        // MINUS CLICK
        // ==========================================

        minus.setOnClickListener(v -> {

            int currentQuantity =
                    item.getQuantity();

            if (currentQuantity > 1) {

                CartManager.updateQuantity(
                        CartActivity.this,
                        item.getProductId(),
                        currentQuantity - 1
                );

                loadCart();
            }
        });

        // ==========================================
        // REMOVE CLICK
        // ==========================================

        remove.setOnClickListener(v -> {

            CartManager.removeFromCart(
                    CartActivity.this,
                    item.getProductId()
            );

            loadCart();
        });

        // ==========================================
        // ADD CARD TO CONTAINER
        // ==========================================

        cartContainer.addView(card);
    }

    // =================================================
    // REFRESH WHEN RETURNING TO CART
    // =================================================

    @Override
    protected void onResume() {
        super.onResume();

        if (cartContainer != null) {
            loadCart();
        }
    }
}