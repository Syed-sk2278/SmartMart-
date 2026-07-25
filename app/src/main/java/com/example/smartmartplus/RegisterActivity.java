package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etPhone, etPassword;
    Button btnRegister, btnPhone;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnPhone = findViewById(R.id.btnPhone);
        tvLogin = findViewById(R.id.tvLogin);


        // Register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Validation

                if (TextUtils.isEmpty(name)) {
                    etName.setError("Enter your name");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    etPhone.setError("Enter phone number");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Enter password");
                    return;
                }

                Toast.makeText(RegisterActivity.this,
                        "Registration Successful!",
                        Toast.LENGTH_SHORT).show();

                // Later you will connect this to Supabase
            }
        });


        // Login Text

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(RegisterActivity.this,
                                LoginActivity.class);

                startActivity(intent);
                finish();
            }
        });


        // Continue with Phone Button

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RegisterActivity.this,
                        "Phone Login Coming Soon!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}