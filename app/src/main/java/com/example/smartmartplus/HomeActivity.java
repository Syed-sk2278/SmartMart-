package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnPlanner, btnScanner, btnCart, btnOffers, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnPlanner = findViewById(R.id.btnPlanner);
        btnScanner = findViewById(R.id.btnScanner);
        btnCart = findViewById(R.id.btnCart);
        btnOffers = findViewById(R.id.btnOffers);
        btnLogout = findViewById(R.id.btnLogout);

        // Logout
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    HomeActivity.this,
                    LoginActivity.class
            );
            startActivity(intent);
            finish();
        });

        // Remaining buttons will be connected later
    }
}