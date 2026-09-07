package com.example.smartmartplus;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartManager {

    private static final String PREF_NAME =
            "SmartMartCart";

    private static final String CART_KEY =
            "CART_ITEMS";


    // =====================================================
    // GET PREFERENCES
    // =====================================================

    private static SharedPreferences getPreferences(
            Context context
    ) {

        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }


    // =====================================================
    // ADD PRODUCT
    // =====================================================

    public static void addToCart(
            Context context,
            CartItem newItem
    ) {

        ArrayList<CartItem> cart =
                getCart(context);


        boolean found = false;


        for (CartItem item : cart) {

            if (item.getProductId() != null
                    && item.getProductId().equals(
                    newItem.getProductId()
            )) {

                int newQuantity =
                        item.getQuantity()
                                + newItem.getQuantity();


                if (newQuantity > item.getStock()) {

                    newQuantity =
                            item.getStock();
                }


                item.setQuantity(
                        newQuantity
                );

                found = true;

                break;
            }
        }


        if (!found) {

            cart.add(newItem);
        }


        saveCart(
                context,
                cart
        );
    }


    // =====================================================
    // GET CART
    // =====================================================

    public static ArrayList<CartItem> getCart(
            Context context
    ) {

        ArrayList<CartItem> cart =
                new ArrayList<>();


        String json =
                getPreferences(context)
                        .getString(
                                CART_KEY,
                                "[]"
                        );


        try {

            JSONArray array =
                    new JSONArray(json);


            for (int i = 0;
                 i < array.length();
                 i++) {

                JSONObject object =
                        array.getJSONObject(i);


                CartItem item =
                        new CartItem(

                                object.optString(
                                        "productId"
                                ),

                                object.optString(
                                        "productName"
                                ),

                                object.optString(
                                        "brand"
                                ),

                                object.optString(
                                        "barcode"
                                ),

                                object.optDouble(
                                        "price",
                                        0
                                ),

                                object.optDouble(
                                        "gst",
                                        0
                                ),

                                object.optDouble(
                                        "discount",
                                        0
                                ),

                                object.optInt(
                                        "stock",
                                        0
                                ),

                                object.optString(
                                        "shelf"
                                ),

                                object.optString(
                                        "description"
                                ),

                                object.optString(
                                        "imageUrl"
                                ),

                                object.optInt(
                                        "quantity",
                                        1
                                )
                        );


                cart.add(item);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return cart;
    }


    // =====================================================
    // SAVE CART
    // =====================================================

    private static void saveCart(
            Context context,
            ArrayList<CartItem> cart
    ) {

        JSONArray array =
                new JSONArray();


        try {

            for (CartItem item : cart) {

                JSONObject object =
                        new JSONObject();


                object.put(
                        "productId",
                        item.getProductId()
                );

                object.put(
                        "productName",
                        item.getProductName()
                );

                object.put(
                        "brand",
                        item.getBrand()
                );

                object.put(
                        "barcode",
                        item.getBarcode()
                );

                object.put(
                        "price",
                        item.getPrice()
                );

                object.put(
                        "gst",
                        item.getGst()
                );

                object.put(
                        "discount",
                        item.getDiscount()
                );

                object.put(
                        "stock",
                        item.getStock()
                );

                object.put(
                        "shelf",
                        item.getShelf()
                );

                object.put(
                        "description",
                        item.getDescription()
                );

                object.put(
                        "imageUrl",
                        item.getImageUrl()
                );

                object.put(
                        "quantity",
                        item.getQuantity()
                );


                array.put(object);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        getPreferences(context)
                .edit()
                .putString(
                        CART_KEY,
                        array.toString()
                )
                .apply();
    }


    // =====================================================
    // UPDATE QUANTITY
    // =====================================================

    public static void updateQuantity(
            Context context,
            String productId,
            int quantity
    ) {

        ArrayList<CartItem> cart =
                getCart(context);


        for (CartItem item : cart) {

            if (item.getProductId() != null
                    && item.getProductId().equals(
                    productId
            )) {

                if (quantity < 1) {

                    quantity = 1;
                }


                if (quantity > item.getStock()) {

                    quantity =
                            item.getStock();
                }


                item.setQuantity(
                        quantity
                );

                break;
            }
        }


        saveCart(
                context,
                cart
        );
    }


    // =====================================================
    // REMOVE PRODUCT
    // =====================================================

    public static void removeFromCart(
            Context context,
            String productId
    ) {

        ArrayList<CartItem> cart =
                getCart(context);


        for (int i = cart.size() - 1;
             i >= 0;
             i--) {

            CartItem item =
                    cart.get(i);


            if (item.getProductId() != null
                    && item.getProductId().equals(
                    productId
            )) {

                cart.remove(i);
            }
        }


        saveCart(
                context,
                cart
        );
    }


    // =====================================================
    // CLEAR CART
    // =====================================================

    public static void clearCart(
            Context context
    ) {

        getPreferences(context)
                .edit()
                .remove(CART_KEY)
                .apply();
    }


    // =====================================================
    // TOTAL ITEMS
    // =====================================================

    public static int getTotalItems(
            Context context
    ) {

        int total = 0;


        ArrayList<CartItem> cart =
                getCart(context);


        for (CartItem item : cart) {

            total += item.getQuantity();
        }


        return total;
    }


    // =====================================================
    // SUBTOTAL
    // =====================================================

    public static double getSubtotal(
            Context context
    ) {

        double subtotal = 0;


        ArrayList<CartItem> cart =
                getCart(context);


        for (CartItem item : cart) {

            subtotal +=
                    item.getPrice()
                            * item.getQuantity();
        }


        return subtotal;
    }


    // =====================================================
    // GST TOTAL
    // =====================================================

    public static double getGstTotal(
            Context context
    ) {

        double total = 0;


        ArrayList<CartItem> cart =
                getCart(context);


        for (CartItem item : cart) {

            double itemAmount =
                    item.getPrice()
                            * item.getQuantity();


            double gstAmount =
                    itemAmount
                            * item.getGst()
                            / 100.0;


            total += gstAmount;
        }


        return total;
    }


    // =====================================================
    // DISCOUNT TOTAL
    // =====================================================

    public static double getDiscountTotal(
            Context context
    ) {

        double total = 0;


        ArrayList<CartItem> cart =
                getCart(context);


        for (CartItem item : cart) {

            double itemAmount =
                    item.getPrice()
                            * item.getQuantity();


            double discountAmount =
                    itemAmount
                            * item.getDiscount()
                            / 100.0;


            total += discountAmount;
        }


        return total;
    }


    // =====================================================
    // GRAND TOTAL
    // =====================================================

    public static double getGrandTotal(
            Context context
    ) {

        double subtotal =
                getSubtotal(context);


        double gst =
                getGstTotal(context);


        double discount =
                getDiscountTotal(context);


        return subtotal
                + gst
                - discount;
    }
}