package com.example.smartmartplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

    private Button btnAddItem;
    private Button btnClearList;
    private Button btnBackHome;

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

        loadShoppingList();

        setupClickListeners();
    }


    // =========================================================
    // INITIALIZE VIEWS
    // =========================================================

    private void initializeViews() {

        etItemName = findViewById(R.id.etItemName);

        etQuantity = findViewById(R.id.etQuantity);

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
    // CLICK LISTENERS
    // =========================================================

    private void setupClickListeners() {

        // -----------------------------------------------------
        // ADD ITEM
        // -----------------------------------------------------

        btnAddItem.setOnClickListener(v -> addItem());


        // -----------------------------------------------------
        // CLEAR LIST
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // BACK HOME
        // -----------------------------------------------------

        btnBackHome.setOnClickListener(v -> finish());
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


        // -----------------------------------------------------
        // GET CURRENT LIST
        // -----------------------------------------------------

        JSONArray shoppingList =
                getShoppingList();


        try {

            boolean itemExists = false;


            // -------------------------------------------------
            // CHECK IF ITEM ALREADY EXISTS
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

                shoppingList.put(newItem);
            }


        } catch (JSONException e) {

            e.printStackTrace();

            Toast.makeText(
                    ShoppingListActivity.this,
                    "Unable to add item",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // -----------------------------------------------------
        // SAVE LIST
        // -----------------------------------------------------

        saveShoppingList(shoppingList);


        // -----------------------------------------------------
        // CLEAR INPUT
        // -----------------------------------------------------

        etItemName.setText("");

        etQuantity.setText("1");

        etItemName.requestFocus();


        // -----------------------------------------------------
        // REFRESH
        // -----------------------------------------------------

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

            e.printStackTrace();

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
        // CREATE ITEM ROWS
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

                e.printStackTrace();
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
                20,
                18,
                12,
                18
        );


        // -----------------------------------------------------
        // ROW BACKGROUND
        // -----------------------------------------------------

        row.setBackgroundResource(
                R.drawable.shopping_list_item_bg
        );


        // -----------------------------------------------------
        // ITEM NAME
        // -----------------------------------------------------

        TextView tvName =
                new TextView(this);

        tvName.setText(
                itemName
        );

        tvName.setTextSize(17);

        tvName.setTextColor(
                android.graphics.Color.parseColor(
                        "#101C25"
                )
        );

        tvName.setGravity(
                Gravity.CENTER_VERTICAL
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
                "Qty: " + quantity
        );

        tvQuantity.setTextSize(15);

        tvQuantity.setTextColor(
                android.graphics.Color.parseColor(
                        "#087F72"
                )
        );

        tvQuantity.setGravity(
                Gravity.CENTER
        );


        LinearLayout.LayoutParams quantityParams =
                new LinearLayout.LayoutParams(
                        75,
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

        btnDelete.setText("✕");

        btnDelete.setTextSize(16);

        btnDelete.setTextColor(
                android.graphics.Color.WHITE
        );

        btnDelete.setBackgroundResource(
                R.drawable.shopping_delete_bg
        );


        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        48,
                        48
                );

        row.addView(
                btnDelete,
                deleteParams
        );


        // -----------------------------------------------------
        // DELETE ACTION
        // -----------------------------------------------------

        btnDelete.setOnClickListener(v -> {

            deleteItem(position);

        });


        // -----------------------------------------------------
        // ADD ROW TO CONTAINER
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