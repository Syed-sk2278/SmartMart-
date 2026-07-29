package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Button btnRegister;
    private Button btnPhone;

    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnPhone = findViewById(R.id.btnPhone);
        tvLogin = findViewById(R.id.tvLogin);

        // Register Button
        btnRegister.setOnClickListener(v -> registerUser());

        // Navigate to Login Screen
        tvLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();

        });

        // Continue with Phone
        btnPhone.setOnClickListener(v -> {

            Toast.makeText(
                    RegisterActivity.this,
                    "Phone Registration Coming Soon!",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }


    private void registerUser() {

        // Get User Input

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();


        // ==========================
        // VALIDATIONS
        // ==========================

        // Full Name Validation
        if (name.isEmpty()) {
            etName.setError("Enter your name");
            return;
        }

        // Email Validation
        if (email.isEmpty()) {
            etEmail.setError("Enter your email");
            return;
        }

        // Valid Email Format Validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            return;
        }

        // Phone Number Validation
        if (phone.isEmpty()) {
            etPhone.setError("Enter your phone number");
            return;
        }

        // Valid Phone Number Validation
        if (phone.length() != 10) {
            etPhone.setError("Enter a valid 10 digit phone number");
            return;
        }

        // Password Validation
        if (password.isEmpty()) {
            etPassword.setError("Enter your password");
            return;
        }

        // Password Length Validation
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Confirm Password Validation
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm your password");
            return;
        }

        // Password Match Validation
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }


        // ==========================
        // CREATE REGISTER REQUEST
        // ==========================

        RegisterRequest request = new RegisterRequest(
                name,
                email,
                phone,
                password
        );


        // ==========================
        // CREATE API INSTANCE
        // ==========================

        SupabaseApi api = RetrofitClient
                .getRetrofitInstance()
                .create(SupabaseApi.class);


        // ==========================
        // CALL REGISTER API
        // ==========================

        Call<RegisterResponse> call =
                api.registerUser(request);


        // ==========================
        // SEND REQUEST TO SUPABASE
        // ==========================

        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call,
                                   Response<RegisterResponse> response) {

                // Registration Successful
                if (response.isSuccessful()) {

                    Toast.makeText(
                            RegisterActivity.this,
                            "Registration Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Navigate to Login Screen
                    Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );

                    startActivity(intent);
                    finish();

                }

                // Registration Failed
                else {

                    Toast.makeText(
                            RegisterActivity.this,
                            "Registration Failed",
                            Toast.LENGTH_SHORT
                    ).show();

                }

            }


            @Override
            public void onFailure(Call<RegisterResponse> call,
                                  Throwable t) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Network Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

            }

        });

    }
}