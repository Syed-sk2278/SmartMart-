package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Login Button Click
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        // Get User Input
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation

        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter your password");
            return;
        }

        // Create Login Request Object

        LoginRequest request = new LoginRequest(email, password);

        // Create API Instance

        SupabaseApi api = RetrofitClient
                .getRetrofitInstance()
                .create(SupabaseApi.class);

        // Call Login API

        Call<LoginResponse> call = api.loginUser(request);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {

                // Login Successful

                if (response.isSuccessful()) {

                    Toast.makeText(
                            LoginActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Navigate to Home Screen

                    Intent intent = new Intent(
                            LoginActivity.this,
                            HomeActivity.class
                    );

                    startActivity(intent);
                    finish();

                }

                // Invalid Credentials

                else {

                    Toast.makeText(
                            LoginActivity.this,
                            "Invalid Email or Password",
                            Toast.LENGTH_SHORT
                    ).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call,
                                  Throwable t) {

                Toast.makeText(
                        LoginActivity.this,
                        "Network Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

            }

        });

    }
}