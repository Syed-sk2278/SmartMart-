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

public class StoreQRScannerActivity extends AppCompatActivity {

    private static final String PREF_NAME = "SmartMartPrefs";

    private ActivityResultLauncher<String> cameraPermissionLauncher;


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

                                startStoreQRScanner();

                            } else {

                                Toast.makeText(
                                        StoreQRScannerActivity.this,
                                        "Camera permission is required",
                                        Toast.LENGTH_LONG
                                ).show();

                                finish();
                            }
                        }
                );


        // ==========================================
        // CHECK CAMERA PERMISSION
        // ==========================================

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {

            startStoreQRScanner();

        } else {

            cameraPermissionLauncher.launch(
                    Manifest.permission.CAMERA
            );
        }
    }


    // =================================================
    // START STORE QR SCANNER
    // =================================================

    private void startStoreQRScanner() {

        IntentIntegrator integrator =
                new IntentIntegrator(this);


        // QR CODE ONLY
        integrator.setDesiredBarcodeFormats(
                IntentIntegrator.QR_CODE
        );


        // Scanner message
        integrator.setPrompt(
                "Scan SmartMart+ Store Entrance QR"
        );


        // Rear camera
        integrator.setCameraId(0);


        // Beep
        integrator.setBeepEnabled(true);


        // Lock orientation
        integrator.setOrientationLocked(true);


        // Start scanner
        integrator.initiateScan();
    }


    // =================================================
    // RECEIVE QR RESULT
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
            // CANCELLED
            // ==========================================

            if (result.getContents() == null) {

                Toast.makeText(
                        this,
                        "QR scanning cancelled",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
                return;
            }


            // ==========================================
            // QR DETECTED
            // ==========================================

            String qrValue =
                    result.getContents().trim();


            if (qrValue.isEmpty()) {

                Toast.makeText(
                        this,
                        "Invalid QR code",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }


            Toast.makeText(
                    this,
                    "QR detected:\n" + qrValue,
                    Toast.LENGTH_LONG
            ).show();


            // ==========================================
            // VERIFY STORE
            // ==========================================

            verifyStoreQR(qrValue);

        } else {

            super.onActivityResult(
                    requestCode,
                    resultCode,
                    data
            );
        }
    }


    // =================================================
    // VERIFY STORE QR WITH SUPABASE
    // =================================================

    private void verifyStoreQR(String qrValue) {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        // PostgREST equality filter
        String filterValue =
                "eq." + qrValue;


        api.getStoreByQR(filterValue)
                .enqueue(
                        new Callback<List<Store>>() {

                            @Override
                            public void onResponse(
                                    Call<List<Store>> call,
                                    Response<List<Store>> response
                            ) {

                                // ==================================
                                // SUPABASE ERROR
                                // ==================================

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


                                // ==================================
                                // GET STORES
                                // ==================================

                                List<Store> stores =
                                        response.body();


                                // ==================================
                                // NO STORE
                                // ==================================

                                if (stores == null
                                        || stores.isEmpty()) {

                                    Toast.makeText(
                                            StoreQRScannerActivity.this,
                                            "No matching store found."
                                                    + "\nQR: "
                                                    + qrValue,
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                // ==================================
                                // STORE FOUND
                                // ==================================

                                Store store =
                                        stores.get(0);


                                Toast.makeText(
                                        StoreQRScannerActivity.this,
                                        "Store found:\n"
                                                + store.getName(),
                                        Toast.LENGTH_LONG
                                ).show();


                                // Save verified store
                                saveVerifiedStore(store);
                            }


                            // ==================================
                            // CONNECTION ERROR
                            // ==================================

                            @Override
                            public void onFailure(
                                    Call<List<Store>> call,
                                    Throwable t
                            ) {

                                String error =
                                        t.getMessage();


                                if (error == null
                                        || error.isEmpty()) {

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


    // =================================================
    // SAVE VERIFIED STORE
    // =================================================

    private void saveVerifiedStore(Store store) {

        getSharedPreferences(
                PREF_NAME,
                MODE_PRIVATE
        )
                .edit()


                // ======================================
                // STORE VERIFIED
                // ======================================

                .putBoolean(
                        "STORE_VERIFIED",
                        true
                )


                // ======================================
                // STORE ID
                // ======================================

                .putString(
                        "STORE_ID",
                        store.getId()
                )


                // ======================================
                // STORE NAME
                // ======================================

                .putString(
                        "STORE_NAME",
                        store.getName()
                )


                // ======================================
                // STORE ADDRESS
                // ======================================

                .putString(
                        "STORE_ADDRESS",
                        store.getAddress()
                )


                // ======================================
                // STORE QR
                // ======================================

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


        // ==========================================
        // RETURN TO HOME
        // ==========================================

        finish();
    }
}