package com.example.smartmartplus;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingListActivity extends AppCompatActivity {

    // =========================================================
    // VIEWS
    // =========================================================

    private EditText etItemName;
    private EditText etQuantity;

    private TextView btnMinus;
    private TextView btnPlus;

    private TextView btnAddItem;
    private TextView btnClearList;
    private TextView btnBackHome;

    private LinearLayout shoppingListContainer;

    private TextView tvListCount;
    private TextView tvEmptyMessage;


    // =========================================================
    // STORAGE
    // =========================================================

    private static final String PREF_NAME = "SmartMartPrefs";
    private static final String KEY_SHOPPING_LIST = "SHOPPING_LIST";


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_list);

        initializeViews();

        setupQuantityButtons();

        setupClickListeners();

        loadShoppingList();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        etItemName = findViewById(R.id.etItemName);

        etQuantity = findViewById(R.id.etQuantity);

        btnMinus = findViewById(R.id.btnMinus);

        btnPlus = findViewById(R.id.btnPlus);

        btnAddItem = findViewById(R.id.btnAddItem);

        btnClearList = findViewById(R.id.btnClearList);

        btnBackHome = findViewById(R.id.btnBackHome);

        shoppingListContainer =
                findViewById(R.id.shoppingListContainer);

        tvListCount =
                findViewById(R.id.tvListCount);

        tvEmptyMessage =
                findViewById(R.id.tvEmptyMessage);
    }


    // =========================================================
    // QUANTITY BUTTONS
    // =========================================================

    private void setupQuantityButtons() {

        // MINUS

        btnMinus.setOnClickListener(v -> {

            int quantity = getCurrentQuantity();

            if (quantity > 1) {
                quantity--;
            }

            etQuantity.setText(
                    String.valueOf(quantity)
            );
        });


        // PLUS

        btnPlus.setOnClickListener(v -> {

            int quantity = getCurrentQuantity();

            quantity++;

            etQuantity.setText(
                    String.valueOf(quantity)
            );
        });
    }


    // =========================================================
    // GET CURRENT QUANTITY
    // =========================================================

    private int getCurrentQuantity() {

        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(quantityText)) {
            return 1;
        }

        try {

            int quantity =
                    Integer.parseInt(quantityText);

            return Math.max(quantity, 1);

        } catch (NumberFormatException e) {

            return 1;
        }
    }


    // =========================================================
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {

        // ADD ITEM

        btnAddItem.setOnClickListener(v -> addItem());


        // CLEAR LIST

        btnClearList.setOnClickListener(v -> {

            SharedPreferences prefs =
                    getSharedPreferences(
                            PREF_NAME,
                            MODE_PRIVATE
                    );

            prefs.edit()
                    .remove(KEY_SHOPPING_LIST)
                    .apply();

            loadShoppingList();

            Toast.makeText(
                    ShoppingListActivity.this,
                    "Shopping list cleared",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // BACK HOME

        btnBackHome.setOnClickListener(v -> {

            finish();

        });
    }


    // =========================================================
    // ADD ITEM
    // =========================================================

    private void addItem() {

        String itemName =
                etItemName.getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();


        // -----------------------------------------------------
        // VALIDATE ITEM NAME
        // -----------------------------------------------------

        if (TextUtils.isEmpty(itemName)) {

            etItemName.setError(
                    "Enter item name"
            );

            etItemName.requestFocus();

            return;
        }


        // -----------------------------------------------------
        // VALIDATE QUANTITY
        // -----------------------------------------------------

        int quantity = 1;

        if (!TextUtils.isEmpty(quantityText)) {

            try {

                quantity =
                        Integer.parseInt(quantityText);

            } catch (NumberFormatException e) {

                etQuantity.setError(
                        "Enter a valid quantity"
                );

                etQuantity.requestFocus();

                return;
            }
        }


        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than 0"
            );

            etQuantity.requestFocus();

            return;
        }


        // =====================================================
        // GET CURRENT LIST
        // =====================================================

        JSONArray shoppingList =
                getShoppingList();


        try {

            boolean itemExists = false;


            // -------------------------------------------------
            // CHECK EXISTING ITEM
            // -------------------------------------------------

            for (int i = 0;
                 i < shoppingList.length();
                 i++) {

                JSONObject item =
                        shoppingList.getJSONObject(i);

                String existingName =
                        item.getString("name");


                if (existingName.equalsIgnoreCase(itemName)) {

                    int oldQuantity =
                            item.getInt("quantity");

                    item.put(
                            "quantity",
                            oldQuantity + quantity
                    );

                    itemExists = true;

                    break;
                }
            }


            // -------------------------------------------------
            // ADD NEW ITEM
            // -------------------------------------------------

            if (!itemExists) {

                JSONObject newItem =
                        new JSONObject();

                newItem.put(
                        "name",
                        itemName
                );

                newItem.put(
                        "quantity",
                        quantity
                );

                shoppingList.put(
                        newItem
                );
            }


        } catch (JSONException e) {

            Log.e(
                    "ShoppingListActivity",
                    "Error adding shopping item",
                    e
            );

            Toast.makeText(
                    ShoppingListActivity.this,
                    "Unable to add item",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================================
        // SAVE
        // =====================================================

        saveShoppingList(
                shoppingList
        );


        // =====================================================
        // CLEAR INPUT
        // =====================================================

        etItemName.setText("");

        etQuantity.setText("1");

        etItemName.requestFocus();


        // =====================================================
        // REFRESH
        // =====================================================

        loadShoppingList();


        Toast.makeText(
                ShoppingListActivity.this,
                "Item added to shopping list",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================================
    // GET SHOPPING LIST
    // =========================================================

    private JSONArray getShoppingList() {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );

        String json =
                prefs.getString(
                        KEY_SHOPPING_LIST,
                        "[]"
                );


        try {

            return new JSONArray(json);

        } catch (JSONException e) {

            Log.e(
                    "ShoppingListActivity",
                    "Error reading shopping list",
                    e
            );

            return new JSONArray();
        }
    }


    // =========================================================
    // SAVE SHOPPING LIST
    // =========================================================

    private void saveShoppingList(
            JSONArray shoppingList
    ) {

        SharedPreferences prefs =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        prefs.edit()
                .putString(
                        KEY_SHOPPING_LIST,
                        shoppingList.toString()
                )
                .apply();
    }


    // =========================================================
    // LOAD SHOPPING LIST
    // =========================================================

    private void loadShoppingList() {

        shoppingListContainer.removeAllViews();


        JSONArray shoppingList =
                getShoppingList();


        int itemCount =
                shoppingList.length();


        // -----------------------------------------------------
        // UPDATE COUNT
        // -----------------------------------------------------

        tvListCount.setText(
                itemCount + " item" +
                        (itemCount == 1 ? "" : "s")
        );


        // -----------------------------------------------------
        // EMPTY LIST
        // -----------------------------------------------------

        if (itemCount == 0) {

            tvEmptyMessage.setVisibility(
                    View.VISIBLE
            );

            btnClearList.setVisibility(
                    View.GONE
            );

            return;
        }


        // -----------------------------------------------------
        // LIST HAS ITEMS
        // -----------------------------------------------------

        tvEmptyMessage.setVisibility(
                View.GONE
        );

        btnClearList.setVisibility(
                View.VISIBLE
        );


        // -----------------------------------------------------
        // CREATE ITEMS
        // -----------------------------------------------------

        for (int i = 0;
             i < shoppingList.length();
             i++) {

            try {

                JSONObject item =
                        shoppingList.getJSONObject(i);

                String itemName =
                        item.getString("name");

                int quantity =
                        item.getInt("quantity");


                createItemRow(
                        itemName,
                        quantity,
                        i
                );


            } catch (JSONException e) {

                Log.e(
                        "ShoppingListActivity",
                        "Error displaying shopping item",
                        e
                );
            }
        }
    }


    // =========================================================
    // CREATE ITEM ROW
    // =========================================================

    private void createItemRow(
            String itemName,
            int quantity,
            int position
    ) {

        // -----------------------------------------------------
        // MAIN ROW
        // -----------------------------------------------------

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setPadding(
                18,
                14,
                10,
                14
        );


        // -----------------------------------------------------
        // BACKGROUND
        // -----------------------------------------------------

        row.setBackgroundResource(
                R.drawable.shopping_list_item_bg
        );


        // -----------------------------------------------------
        // PRODUCT ICON
        // -----------------------------------------------------

        TextView tvIcon =
                new TextView(this);

        tvIcon.setText(
                getProductIcon(itemName)
        );

        tvIcon.setTextSize(32);

        tvIcon.setGravity(
                Gravity.CENTER
        );


        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        65,
                        65
                );

        iconParams.setMargins(
                0,
                0,
                10,
                0
        );


        row.addView(
                tvIcon,
                iconParams
        );


        // -----------------------------------------------------
        // ITEM NAME
        // -----------------------------------------------------

        TextView tvName =
                new TextView(this);

        tvName.setText(
                itemName
        );

        tvName.setTextSize(19);

        tvName.setTextColor(
                Color.parseColor("#174D3C")
        );

        tvName.setGravity(
                Gravity.CENTER_VERTICAL
        );

        tvName.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );


        LinearLayout.LayoutParams nameParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );


        row.addView(
                tvName,
                nameParams
        );


        // -----------------------------------------------------
        // QUANTITY
        // -----------------------------------------------------

        TextView tvQuantity =
                new TextView(this);

        tvQuantity.setText(
                "Qty:\n" + quantity
        );

        tvQuantity.setTextSize(15);

        tvQuantity.setTextColor(
                Color.parseColor("#087F72")
        );

        tvQuantity.setGravity(
                Gravity.CENTER
        );

        tvQuantity.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );


        LinearLayout.LayoutParams quantityParams =
                new LinearLayout.LayoutParams(
                        65,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        row.addView(
                tvQuantity,
                quantityParams
        );


        // -----------------------------------------------------
        // DELETE BUTTON
        // -----------------------------------------------------

        Button btnDelete =
                new Button(this);

        btnDelete.setText("🗑");

        btnDelete.setTextSize(18);

        btnDelete.setTextColor(
                Color.WHITE
        );

        btnDelete.setAllCaps(false);

        btnDelete.setBackgroundResource(
                R.drawable.shopping_delete_bg
        );


        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        52,
                        52
                );


        row.addView(
                btnDelete,
                deleteParams
        );


        // -----------------------------------------------------
        // DELETE CLICK
        // -----------------------------------------------------

        btnDelete.setOnClickListener(v -> {

            deleteItem(position);

        });


        // -----------------------------------------------------
        // ADD ROW
        // -----------------------------------------------------

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        rowParams.setMargins(
                0,
                0,
                0,
                12
        );


        shoppingListContainer.addView(
                row,
                rowParams
        );
    }


    // =========================================================
    // PRODUCT ICON
    // =========================================================

    private String getProductIcon(
            String itemName
    ) {

        String name =
                itemName.toLowerCase().trim();


        if (name.contains("milk")) {
            return "🥛";
        }

        if (name.contains("bread")) {
            return "🍞";
        }

        if (name.contains("apple")) {
            return "🍎";
        }

        if (name.contains("banana")) {
            return "🍌";
        }

        if (name.contains("orange")) {
            return "🍊";
        }

        if (name.contains("tomato")) {
            return "🍅";
        }

        if (name.contains("potato")) {
            return "🥔";
        }

        if (name.contains("carrot")) {
            return "🥕";
        }

        if (name.contains("egg")) {
            return "🥚";
        }

        if (name.contains("rice")) {
            return "🍚";
        }

        if (name.contains("chips")) {
            return "🍟";
        }

        if (name.contains("juice")) {
            return "🧃";
        }

        if (name.contains("water")) {
            return "💧";
        }

        if (name.contains("soap")) {
            return "🧼";
        }

        if (name.contains("cake")) {
            return "🍰";
        }

        return "🛍️";
    }


    // =========================================================
    // DELETE ITEM
    // =========================================================

    private void deleteItem(int position) {

        JSONArray shoppingList =
                getShoppingList();


        if (position < 0 ||
                position >= shoppingList.length()) {

            return;
        }


        shoppingList.remove(position);


        saveShoppingList(
                shoppingList
        );


        loadShoppingList();


        Toast.makeText(
                ShoppingListActivity.this,
                "Item removed",
                Toast.LENGTH_SHORT
        ).show();
    }
}
