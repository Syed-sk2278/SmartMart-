package com.example.smartmartplus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ReceiptActivity extends AppCompatActivity {

    private TextView tvReceiptTitle;
    private TextView tvReceiptNumber;
    private TextView tvReceiptDate;
    private TextView tvReceiptItems;
    private TextView tvReceiptSubtotal;
    private TextView tvReceiptDiscount;
    private TextView tvReceiptGST;
    private TextView tvReceiptTotal;
    private TextView tvReceiptPaymentMethod;
    private TextView tvTransactionStatus;

    private ImageView ivTransactionQR;

    private Button btnDone;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_receipt
        );

        // =================================================
        // FIND VIEWS
        // =================================================

        tvReceiptTitle =
                findViewById(
                        R.id.tvReceiptTitle
                );

        tvReceiptNumber =
                findViewById(
                        R.id.tvReceiptNumber
                );

        tvReceiptDate =
                findViewById(
                        R.id.tvReceiptDate
                );

        tvReceiptItems =
                findViewById(
                        R.id.tvReceiptItems
                );

        tvReceiptSubtotal =
                findViewById(
                        R.id.tvReceiptSubtotal
                );

        tvReceiptDiscount =
                findViewById(
                        R.id.tvReceiptDiscount
                );

        tvReceiptGST =
                findViewById(
                        R.id.tvReceiptGST
                );

        tvReceiptTotal =
                findViewById(
                        R.id.tvReceiptTotal
                );

        tvReceiptPaymentMethod =
                findViewById(
                        R.id.tvReceiptPaymentMethod
                );

        tvTransactionStatus =
                findViewById(
                        R.id.tvTransactionStatus
                );

        ivTransactionQR =
                findViewById(
                        R.id.ivTransactionQR
                );

        btnDone =
                findViewById(
                        R.id.btnDone
                );

        btnBackHome =
                findViewById(
                        R.id.btnBackHome
                );

        // =================================================
        // GET PAYMENT DATA
        // =================================================

        final int itemCount =
                getIntent().getIntExtra(
                        "ITEM_COUNT",
                        0
                );

        final double subtotal =
                getIntent().getDoubleExtra(
                        "SUBTOTAL",
                        0.0
                );

        final double discount =
                getIntent().getDoubleExtra(
                        "DISCOUNT",
                        0.0
                );

        final double gst =
                getIntent().getDoubleExtra(
                        "GST",
                        0.0
                );

        double receivedTotal =
                getIntent().getDoubleExtra(
                        "TOTAL",
                        -1.0
                );

        if (receivedTotal <= 0.0) {

            receivedTotal =
                    subtotal
                            - discount
                            + gst;
        }

        final double total =
                receivedTotal;

        String receivedPaymentMethod =
                getIntent().getStringExtra(
                        "PAYMENT_METHOD"
                );

        if (receivedPaymentMethod == null
                || receivedPaymentMethod
                .trim()
                .isEmpty()) {

            receivedPaymentMethod = "UPI";
        }

        final String paymentMethod =
                receivedPaymentMethod;

        // =================================================
        // GENERATE RECEIPT NUMBER
        // =================================================

        final String receiptNumber =
                "SM"
                        + UUID.randomUUID()
                        .toString()
                        .replace(
                                "-",
                                ""
                        )
                        .substring(
                                0,
                                8
                        )
                        .toUpperCase(
                                Locale.getDefault()
                        );

        tvReceiptNumber.setText(
                "Receipt No.  " + receiptNumber
        );

        // =================================================
        // DATE AND TIME
        // =================================================

        String currentDate =
                new SimpleDateFormat(
                        "dd MMM yyyy  •  hh:mm a",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        tvReceiptDate.setText(
                currentDate
        );

        // =================================================
        // SUCCESS STATUS
        // =================================================

        tvTransactionStatus.setText(
                "✓  PAYMENT SUCCESSFUL"
        );

        // =================================================
        // ITEMS
        // =================================================

        tvReceiptItems.setText(
                itemCount
                        + (
                        itemCount == 1
                                ? " item"
                                : " items"
                )
        );

        // =================================================
        // SUBTOTAL
        // =================================================

        tvReceiptSubtotal.setText(
                formatRupees(subtotal)
        );

        // =================================================
        // DISCOUNT
        // =================================================

        tvReceiptDiscount.setText(
                "- " + formatRupees(discount)
        );

        // =================================================
        // GST
        // =================================================

        tvReceiptGST.setText(
                formatRupees(gst)
        );

        // =================================================
        // TOTAL
        // =================================================

        tvReceiptTotal.setText(
                formatRupees(total)
        );

        // =================================================
        // PAYMENT METHOD
        // =================================================

        tvReceiptPaymentMethod.setText(
                "Paid via  •  "
                        + paymentMethod
        );

        // =================================================
        // TRANSACTION QR
        // =================================================

        String transactionQRData =
                "SMARTMART_TRANSACTION"
                        + "|RECEIPT="
                        + receiptNumber
                        + "|ITEMS="
                        + itemCount
                        + "|TOTAL="
                        + String.format(
                        Locale.US,
                        "%.2f",
                        total
                )
                        + "|PAYMENT="
                        + paymentMethod
                        + "|STATUS=PAID";

        generateTransactionQR(
                transactionQRData
        );

        // =================================================
        // SAVE PURCHASE HISTORY
        // =================================================

        PurchaseHistoryActivity.savePurchase(
                ReceiptActivity.this,
                receiptNumber,
                itemCount,
                total,
                paymentMethod
        );

        // =================================================
        // DONE
        // =================================================

        btnDone.setOnClickListener(v -> {

            CartManager.clearCart(
                    ReceiptActivity.this
            );

            goToHome();
        });

        // =================================================
        // BACK TO HOME
        // =================================================

        btnBackHome.setOnClickListener(v -> {

            CartManager.clearCart(
                    ReceiptActivity.this
            );

            goToHome();
        });
    }

    // =====================================================
    // FORMAT RUPEES
    // =====================================================

    private String formatRupees(
            double amount
    ) {

        return String.format(
                Locale.getDefault(),
                "₹%.2f",
                amount
        );
    }

    // =====================================================
    // GENERATE QR
    // =====================================================

    private void generateTransactionQR(
            String data
    ) {

        QRCodeWriter writer =
                new QRCodeWriter();

        try {

            BitMatrix bitMatrix =
                    writer.encode(
                            data,
                            BarcodeFormat.QR_CODE,
                            700,
                            700
                    );

            int width =
                    bitMatrix.getWidth();

            int height =
                    bitMatrix.getHeight();

            Bitmap bitmap =
                    Bitmap.createBitmap(
                            width,
                            height,
                            Bitmap.Config.ARGB_8888
                    );

            for (int x = 0; x < width; x++) {

                for (int y = 0; y < height; y++) {

                    bitmap.setPixel(
                            x,
                            y,
                            bitMatrix.get(
                                    x,
                                    y
                            )
                                    ? Color.BLACK
                                    : Color.WHITE
                    );
                }
            }

            ivTransactionQR.setImageBitmap(
                    bitmap
            );

        } catch (WriterException e) {

            ivTransactionQR.setImageDrawable(
                    null
            );
        }
    }

    // =====================================================
    // GO TO HOME
    // =====================================================

    private void goToHome() {

        Intent intent =
                new Intent(
                        ReceiptActivity.this,
                        HomeActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }
}