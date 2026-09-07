package com.example.smartmartplus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarcodeScannerActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> cameraPermissionLauncher;

    private static final String PREF_NAME =
            "SmartMartPrefs";

    private static final String KEY_STORE_VERIFIED =
            "STORE_VERIFIED";

    private static final String KEY_STORE_ID =
            "STORE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // ==========================================
        // CAMERA PERMISSION
        // ==========================================

        cameraPermissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        granted -> {

                            if (granted) {

                                startBarcodeScanner();

                            } else {

                                Toast.makeText(
                                        BarcodeScannerActivity.this,
                                        "Camera permission is required to scan products",
                                        Toast.LENGTH_LONG
                                ).show();

                                finish();
                            }
                        }
                );


        // ==========================================
        // CHECK STORE VERIFICATION
        // ==========================================

        boolean storeVerified =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                ).getBoolean(
                        KEY_STORE_VERIFIED,
                        false
                );


        if (!storeVerified) {

            Toast.makeText(
                    this,
                    "Please scan the Store Entrance QR first",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }


        // ==========================================
        // CHECK STORE ID
        // ==========================================

        String storeId =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                ).getString(
                        KEY_STORE_ID,
                        null
                );


        if (storeId == null
                || storeId.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Store information not found. Please scan Store QR again.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }


        // ==========================================
        // CAMERA CHECK
        // ==========================================

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {

            startBarcodeScanner();

        } else {

            cameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
            );
        }
    }


    // =================================================
    // START BARCODE SCANNER
    // =================================================

    private void startBarcodeScanner() {

        IntentIntegrator integrator =
                new IntentIntegrator(this);


        integrator.setDesiredBarcodeFormats(
                IntentIntegrator.ALL_CODE_TYPES
        );


        integrator.setPrompt(
                "Scan product barcode"
        );


        integrator.setCameraId(0);


        integrator.setBeepEnabled(true);


        integrator.setOrientationLocked(true);


        integrator.initiateScan();
    }


    // =================================================
    // RECEIVE BARCODE
    // =================================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        IntentResult result =
                IntentIntegrator.parseActivityResult(
                        requestCode,
                        resultCode,
                        data
                );


        if (result != null) {

            // ==========================================
            // SCAN CANCELLED
            // ==========================================

            if (result.getContents() == null) {

                Toast.makeText(
                        this,
                        "Scan cancelled",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

                return;
            }


            // ==========================================
            // BARCODE FOUND
            // ==========================================

            String barcodeValue =
                    result.getContents().trim();


            if (barcodeValue.isEmpty()) {

                Toast.makeText(
                        this,
                        "Could not read barcode",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }


            Toast.makeText(
                    this,
                    "Barcode detected: "
                            + barcodeValue,
                    Toast.LENGTH_SHORT
            ).show();


            // ==========================================
            // SEARCH PRODUCT
            // ==========================================

            searchProduct(barcodeValue);

        } else {

            super.onActivityResult(
                    requestCode,
                    resultCode,
                    data
            );
        }
    }


    // =================================================
    // SEARCH PRODUCT
    // =================================================

    private void searchProduct(String barcode) {

        String storeId =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                ).getString(
                        KEY_STORE_ID,
                        null
                );


        // ==========================================
        // CHECK STORE ID
        // ==========================================

        if (storeId == null
                || storeId.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Store information missing. Please scan Store QR again.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }


        // ==========================================
        // CREATE API
        // ==========================================

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        // ==========================================
        // POSTGREST FILTER
        // ==========================================

        String barcodeFilter =
                "eq." + barcode;


        String storeFilter =
                "eq." + storeId;


        // ==========================================
        // CALL SUPABASE
        // ==========================================

        api.getProductByBarcode(
                        barcodeFilter,
                        storeFilter
                )
                .enqueue(
                        new Callback<List<Product>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Product>> call,
                                    Response<List<Product>> response
                            ) {

                                // ==================================
                                // SUCCESS
                                // ==================================

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    List<Product> products =
                                            response.body();


                                    // ==============================
                                    // PRODUCT FOUND
                                    // ==============================

                                    if (!products.isEmpty()) {

                                        Product product =
                                                products.get(0);


                                        openProductDetails(
                                                product
                                        );

                                    } else {

                                        Toast.makeText(
                                                BarcodeScannerActivity.this,
                                                "Product not found for barcode: "
                                                        + barcode,
                                                Toast.LENGTH_LONG
                                        ).show();

                                        finish();
                                    }

                                } else {

                                    Toast.makeText(
                                            BarcodeScannerActivity.this,
                                            "Product search failed. Code: "
                                                    + response.code(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                    finish();
                                }
                            }


                            // ==================================
                            // NETWORK ERROR
                            // ==================================

                            @Override
                            public void onFailure(
                                    Call<List<Product>> call,
                                    Throwable t
                            ) {

                                Toast.makeText(
                                        BarcodeScannerActivity.this,
                                        "Network Error: "
                                                + t.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();

                                finish();
                            }
                        }
                );
    }


    // =================================================
    // OPEN PRODUCT DETAILS
    // =================================================

    private void openProductDetails(
            Product product
    ) {

        Intent intent =
                new Intent(
                        BarcodeScannerActivity.this,
                        ProductDetailsActivity.class
                );


        // ==========================================
        // PRODUCT ID
        // ==========================================

        intent.putExtra(
                "PRODUCT_ID",
                product.getId()
        );


        // ==========================================
        // PRODUCT NAME
        // ==========================================

        intent.putExtra(
                "PRODUCT_NAME",
                product.getName()
        );


        // ==========================================
        // BRAND
        // ==========================================

        intent.putExtra(
                "PRODUCT_BRAND",
                product.getBrand()
        );


        // ==========================================
        // DESCRIPTION
        // ==========================================

        intent.putExtra(
                "PRODUCT_DESCRIPTION",
                product.getDescription()
        );


        // ==========================================
        // BARCODE
        // ==========================================

        intent.putExtra(
                "PRODUCT_BARCODE",
                product.getBarcode()
        );


        // ==========================================
        // PRICE
        // ==========================================

        intent.putExtra(
                "PRODUCT_PRICE",
                product.getPrice()
        );


        // ==========================================
        // GST
        // ==========================================

        intent.putExtra(
                "PRODUCT_GST",
                product.getGst()
        );


        // ==========================================
        // DISCOUNT
        // ==========================================

        intent.putExtra(
                "PRODUCT_DISCOUNT",
                product.getDiscount()
        );


        // ==========================================
        // STOCK
        // ==========================================

        intent.putExtra(
                "PRODUCT_STOCK",
                product.getStock_quantity()
        );


        // ==========================================
        // SHELF
        // ==========================================

        intent.putExtra(
                "PRODUCT_SHELF",
                product.getShelf_id()
        );


        // ==========================================
        // IMAGE URL
        // ==========================================

        intent.putExtra(
                "IMAGE_URL",
                product.getImage_url()
        );


        // ==========================================
        // OPEN DETAILS
        // ==========================================

        startActivity(intent);

        finish();
    }
}