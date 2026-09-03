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

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;

    private static final String PREF_NAME = "SmartMartPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Retrofit
        RetrofitClient.initialize(this);

        setContentView(R.layout.activity_login);

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        // Login Button
        btnLogin.setOnClickListener(v -> loginUser());

        // Sign Up
        tvSignup.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }


    // =====================================================
    // LOGIN USER
    // =====================================================

    private void loginUser() {

        String email =
                etEmail.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();


        // =================================================
        // VALIDATION
        // =================================================

        if (email.isEmpty()) {

            etEmail.setError(
                    "Please enter your email"
            );

            return;
        }


        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError(
                    "Enter a valid email address"
            );

            return;
        }


        if (password.isEmpty()) {

            etPassword.setError(
                    "Please enter your password"
            );

            return;
        }


        if (password.length() < 6) {

            etPassword.setError(
                    "Password must be at least 6 characters"
            );

            return;
        }


        // =================================================
        // LOGIN REQUEST
        // =================================================

        LoginRequest request =
                new LoginRequest(
                        email,
                        password
                );


        // =================================================
        // SUPABASE API
        // =================================================

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);


        api.loginUser(request)
                .enqueue(
                        new Callback<LoginResponse>() {

                            @Override
                            public void onResponse(
                                    Call<LoginResponse> call,
                                    Response<LoginResponse> response) {

                                // =====================================
                                // LOGIN SUCCESSFUL
                                // =====================================

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    LoginResponse loginResponse =
                                            response.body();


                                    String accessToken =
                                            loginResponse
                                                    .getAccessToken();


                                    String refreshToken =
                                            loginResponse
                                                    .getRefreshToken();


                                    // =================================
                                    // CHECK ACCESS TOKEN
                                    // =================================

                                    if (accessToken == null
                                            || accessToken.trim().isEmpty()) {

                                        Toast.makeText(
                                                LoginActivity.this,
                                                "Login succeeded but access token was not received",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        return;
                                    }


                                    // =================================
                                    // SAVE LOGIN SESSION
                                    // =================================

                                    getSharedPreferences(
                                            PREF_NAME,
                                            MODE_PRIVATE
                                    )
                                            .edit()

                                            // Save authentication token
                                            .putString(
                                                    "ACCESS_TOKEN",
                                                    accessToken
                                            )

                                            .putString(
                                                    "REFRESH_TOKEN",
                                                    refreshToken
                                            )

                                            .putBoolean(
                                                    "IS_LOGGED_IN",
                                                    true
                                            )

                                            // =================================
                                            // IMPORTANT
                                            // Every NEW login starts OUTSIDE
                                            // the store.
                                            // =================================

                                            .putBoolean(
                                                    "STORE_VERIFIED",
                                                    false
                                            )

                                            // Remove previous store session
                                            .remove("STORE_ID")
                                            .remove("STORE_NAME")
                                            .remove("STORE_ADDRESS")
                                            .remove("STORE_QR")

                                            .apply();


                                    // =================================
                                    // SUCCESS MESSAGE
                                    // =================================

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Login Successful",
                                            Toast.LENGTH_SHORT
                                    ).show();


                                    // =================================
                                    // OPEN HOME
                                    // =================================

                                    Intent intent =
                                            new Intent(
                                                    LoginActivity.this,
                                                    HomeActivity.class
                                            );

                                    startActivity(intent);

                                    finish();

                                }

                                // =====================================
                                // LOGIN FAILED
                                // =====================================

                                else {

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Invalid Email or Password",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }


                            // =========================================
                            // NETWORK ERROR
                            // =========================================

                            @Override
                            public void onFailure(
                                    Call<LoginResponse> call,
                                    Throwable t) {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Network Error: "
                                                + t.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }
}