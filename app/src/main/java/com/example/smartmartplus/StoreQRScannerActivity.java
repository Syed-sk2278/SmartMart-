package com.example.smartmartplus;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreQRScannerActivity extends AppCompatActivity {

    private GmsBarcodeScanner scanner;

    // =====================================================
    // ON CREATE
    // =====================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupScanner();
    }


    // =====================================================
    // SETUP SCANNER
    // =====================================================

    private void setupScanner() {

        GmsBarcodeScannerOptions options =
                new GmsBarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE
                        )
                        .enableAutoZoom()
                        .build();

        scanner =
                GmsBarcodeScanning.getClient(
                        this,
                        options
                );

        ModuleInstallRequest request =
                ModuleInstallRequest.newBuilder()
                        .addApi(scanner)
                        .build();

        ModuleInstall.getClient(this)
                .installModules(request)
                .addOnSuccessListener(
                        unused -> startScanning()
                )
                .addOnFailureListener(
                        e -> {

                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "Unable to prepare QR scanner: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                            e.printStackTrace();

                            finish();
                        }
                );
    }


    // =====================================================
    // START SCANNING
    // =====================================================

    private void startScanning() {

        if (scanner == null) {

            Toast.makeText(
                    StoreQRScannerActivity.this,
                    "Scanner is not ready",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        scanner.startScan()

                // -----------------------------------------
                // SUCCESS
                // -----------------------------------------

                .addOnSuccessListener(
                        barcode -> {

                            String qrValue =
                                    barcode.getRawValue();

                            if (qrValue == null ||
                                    qrValue.trim().isEmpty()) {

                                Toast.makeText(
                                        StoreQRScannerActivity.this,
                                        "Invalid QR code",
                                        Toast.LENGTH_SHORT
                                ).show();

                                startScanning();

                                return;
                            }

                            // Remove spaces
                            qrValue = qrValue.trim();

                            // Show exactly what was scanned
                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "QR detected:\n" + qrValue,
                                    Toast.LENGTH_LONG
                            ).show();

                            // Verify with Supabase
                            verifyStoreQR(qrValue);
                        }
                )

                // -----------------------------------------
                // CANCELLED
                // -----------------------------------------

                .addOnCanceledListener(
                        () -> {

                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "QR scanning cancelled",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        }
                )

                // -----------------------------------------
                // SCAN FAILURE
                // -----------------------------------------

                .addOnFailureListener(
                        e -> {

                            String error =
                                    e.getMessage();

                            if (error == null ||
                                    error.isEmpty()) {

                                error =
                                        e.getClass()
                                                .getSimpleName();
                            }

                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "QR scan failed:\n"
                                            + error,
                                    Toast.LENGTH_LONG
                            ).show();

                            e.printStackTrace();
                        }
                );
    }


    // =====================================================
    // VERIFY STORE QR USING SUPABASE
    // =====================================================

    private void verifyStoreQR(String qrValue) {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);

        // Supabase PostgREST equality filter
        String filterValue =
                "eq." + qrValue;

        api.getStoreByQR(filterValue)
                .enqueue(
                        new Callback<List<Store>>() {

                            // ---------------------------------
                            // RESPONSE
                            // ---------------------------------

                            @Override
                            public void onResponse(
                                    Call<List<Store>> call,
                                    Response<List<Store>> response) {

                                // =============================
                                // SERVER ERROR
                                // =============================

                                if (!response.isSuccessful()) {

                                    String errorBody = "";

                                    try {

                                        if (response.errorBody()
                                                != null) {

                                            errorBody =
                                                    response.errorBody()
                                                            .string();
                                        }

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                    Toast.makeText(
                                            StoreQRScannerActivity.this,
                                            "Supabase Error: "
                                                    + response.code()
                                                    + "\n"
                                                    + errorBody,
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                // =============================
                                // GET RESPONSE
                                // =============================

                                List<Store> stores =
                                        response.body();


                                // =============================
                                // NO STORE FOUND
                                // =============================

                                if (stores == null ||
                                        stores.isEmpty()) {

                                    Toast.makeText(
                                            StoreQRScannerActivity.this,
                                            "No matching store found."
                                                    + "\nQR: "
                                                    + qrValue,
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                // =============================
                                // STORE FOUND
                                // =============================

                                Store store =
                                        stores.get(0);


                                // Show store name
                                Toast.makeText(
                                        StoreQRScannerActivity.this,
                                        "Store found:\n"
                                                + store.getName(),
                                        Toast.LENGTH_LONG
                                ).show();


                                // Save store
                                saveVerifiedStore(store);
                            }


                            // ---------------------------------
                            // CONNECTION FAILURE
                            // ---------------------------------

                            @Override
                            public void onFailure(
                                    Call<List<Store>> call,
                                    Throwable t) {

                                String error =
                                        t.getMessage();

                                if (error == null ||
                                        error.isEmpty()) {

                                    error =
                                            "Unknown connection error";
                                }

                                Toast.makeText(
                                        StoreQRScannerActivity.this,
                                        "Connection error:\n"
                                                + error,
                                        Toast.LENGTH_LONG
                                ).show();

                                t.printStackTrace();
                            }
                        }
                );
    }


    // =====================================================
    // SAVE VERIFIED STORE
    // =====================================================

    private void saveVerifiedStore(Store store) {

        getSharedPreferences(
                "SmartMartPrefs",
                MODE_PRIVATE
        )
                .edit()

                .putBoolean(
                        "STORE_VERIFIED",
                        true
                )

                .putString(
                        "STORE_ID",
                        store.getId()
                )

                .putString(
                        "STORE_NAME",
                        store.getName()
                )

                .putString(
                        "STORE_ADDRESS",
                        store.getAddress()
                )

                .putString(
                        "STORE_QR",
                        store.getEntrance_qr_code()
                )

                .apply();


        Toast.makeText(
                StoreQRScannerActivity.this,
                "Store verified successfully!",
                Toast.LENGTH_SHORT
        ).show();


        // Return to HomeActivity
        finish();
    }
}