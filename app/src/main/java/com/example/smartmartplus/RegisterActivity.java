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

        // Already have an account? Login
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

        // Get user input
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword =
                etConfirmPassword.getText().toString().trim();


        // ==========================
        // VALIDATION
        // ==========================

        // Full Name
        if (name.isEmpty()) {
            etName.setError("Enter your name");
            etName.requestFocus();
            return;
        }

        // Email empty
        if (email.isEmpty()) {
            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        // Email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }

        // Phone empty
        if (phone.isEmpty()) {
            etPhone.setError("Enter your phone number");
            etPhone.requestFocus();
            return;
        }

        // Phone length
        if (phone.length() != 10) {
            etPhone.setError(
                    "Enter a valid 10 digit phone number"
            );
            etPhone.requestFocus();
            return;
        }

        // Password empty
        if (password.isEmpty()) {
            etPassword.setError("Enter your password");
            etPassword.requestFocus();
            return;
        }

        // Password length
        if (password.length() < 6) {
            etPassword.setError(
                    "Password must be at least 6 characters"
            );
            etPassword.requestFocus();
            return;
        }

        // Confirm password empty
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError(
                    "Please confirm your password"
            );
            etConfirmPassword.requestFocus();
            return;
        }

        // Password matching
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(
                    "Passwords do not match"
            );
            etConfirmPassword.requestFocus();
            return;
        }


        // ==========================
        // CREATE REQUEST
        // ==========================

        RegisterRequest request = new RegisterRequest(
                name,
                email,
                phone,
                password
        );


        // ==========================
        // CREATE API
        // ==========================

        SupabaseApi api = RetrofitClient
                .getRetrofitInstance()
                .create(SupabaseApi.class);


        // ==========================
        // REGISTER USER
        // ==========================

        Call<RegisterResponse> call =
                api.registerUser(request);


        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(
                    Call<RegisterResponse> call,
                    Response<RegisterResponse> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(
                            RegisterActivity.this,
                            "Registration Successful!",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Go to Login
                    Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    // Get actual Supabase error
                    String errorMessage;

                    try {

                        if (response.errorBody() != null) {

                            errorMessage =
                                    response.errorBody().string();

                        } else {

                            errorMessage =
                                    "Unknown Supabase error";

                        }

                    } catch (Exception e) {

                        errorMessage =
                                "Unable to read error";
                    }


                    Toast.makeText(
                            RegisterActivity.this,
                            "Registration Failed:\n" +
                                    errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }


            @Override
            public void onFailure(
                    Call<RegisterResponse> call,
                    Throwable t) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Network Error:\n" +
                                t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}