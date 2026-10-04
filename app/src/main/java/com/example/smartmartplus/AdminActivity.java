package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button btnExitVerification;
    private Button btnAdminBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        btnExitVerification =
                findViewById(R.id.btnExitVerification);

        btnAdminBack =
                findViewById(R.id.btnAdminBack);

        btnExitVerification.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AdminActivity.this,
                            ExitVerificationActivity.class
                    );

            startActivity(intent);
        });

        btnAdminBack.setOnClickListener(v -> finish());
    }
}