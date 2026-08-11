package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class BarcodeScannerActivity extends AppCompatActivity {

    private GmsBarcodeScanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startBarcodeScanner();
    }

    private void startBarcodeScanner() {

        // Scanner options
        GmsBarcodeScannerOptions options =
                new GmsBarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_ALL_FORMATS
                        )
                        .enableAutoZoom()
                        .build();

        // Create scanner
        scanner =
                GmsBarcodeScanning.getClient(
                        this,
                        options
                );

        // Start scanner
        scanner.startScan()
                .addOnSuccessListener(barcode -> {

                    String barcodeValue =
                            barcode.getRawValue();

                    if (barcodeValue != null &&
                            !barcodeValue.isEmpty()) {

                        Toast.makeText(
                                BarcodeScannerActivity.this,
                                "Barcode: " + barcodeValue,
                                Toast.LENGTH_LONG
                        ).show();

                        // Send barcode back
                        // to ScannerActivity
                        Intent intent =
                                new Intent(
                                        BarcodeScannerActivity.this,
                                        ScannerActivity.class
                                );

                        intent.putExtra(
                                "BARCODE",
                                barcodeValue
                        );

                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        );

                        startActivity(intent);

                        finish();

                    } else {

                        Toast.makeText(
                                BarcodeScannerActivity.this,
                                "Barcode could not be read",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    }
                })
                .addOnCanceledListener(() -> {

                    Toast.makeText(
                            BarcodeScannerActivity.this,
                            "Scan cancelled",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            BarcodeScannerActivity.this,
                            "Scanner error: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                    finish();
                });
    }
}