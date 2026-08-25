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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupScanner();
    }


    // =====================================================
    // SETUP QR SCANNER
    // =====================================================

    private void setupScanner() {

        GmsBarcodeScannerOptions options =
                new GmsBarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE
                        )
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
                                    "Unable to prepare QR scanner",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();
                        }
                );
    }


    // =====================================================
    // START QR SCANNING
    // =====================================================

    private void startScanning() {

        scanner.startScan()

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


                            // Remove unnecessary spaces
                            qrValue = qrValue.trim();


                            // Send QR to Supabase
                            verifyStoreQR(qrValue);
                        }
                )

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

                .addOnFailureListener(
                        e -> {

                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "QR scan failed",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();
                        }
                );
    }


    // =====================================================
    // VERIFY STORE USING SUPABASE
    // =====================================================

    private void verifyStoreQR(String qrValue) {

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.getStoreByQR(
                "eq." + qrValue
        ).enqueue(
                new Callback<List<Store>>() {

                    @Override
                    public void onResponse(
                            Call<List<Store>> call,
                            Response<List<Store>> response) {

                        // ---------------------------------
                        // SERVER ERROR
                        // ---------------------------------

                        if (!response.isSuccessful()) {

                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "Unable to verify store",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }


                        // ---------------------------------
                        // GET STORES
                        // ---------------------------------

                        List<Store> stores =
                                response.body();


                        // ---------------------------------
                        // NO STORE FOUND
                        // ---------------------------------

                        if (stores == null ||
                                stores.isEmpty()) {

                            Toast.makeText(
                                    StoreQRScannerActivity.this,
                                    "Invalid SmartMart+ store QR",
                                    Toast.LENGTH_LONG
                            ).show();

                            startScanning();
                            return;
                        }


                        // ---------------------------------
                        // STORE FOUND
                        // ---------------------------------

                        Store store =
                                stores.get(0);


                        saveVerifiedStore(store);
                    }


                    @Override
                    public void onFailure(
                            Call<List<Store>> call,
                            Throwable t) {

                        Toast.makeText(
                                StoreQRScannerActivity.this,
                                "Connection error. Please try again.",
                                Toast.LENGTH_LONG
                        ).show();
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

                .apply();


        Toast.makeText(
                StoreQRScannerActivity.this,
                "Store verified successfully!",
                Toast.LENGTH_SHORT
        ).show();


        // Return to the existing HomeActivity
        finish();
    }
}