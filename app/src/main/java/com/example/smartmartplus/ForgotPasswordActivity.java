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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etForgotEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Retrofit
        RetrofitClient.initialize(this);

        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        etForgotEmail = findViewById(R.id.etForgotEmail);
        btnSendReset = findViewById(R.id.btnSendReset);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Send reset email
        btnSendReset.setOnClickListener(v -> sendResetEmail());

        // Back to login
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void sendResetEmail() {

        String email =
                etForgotEmail.getText()
                        .toString()
                        .trim();

        // Validation
        if (email.isEmpty()) {

            etForgotEmail.setError(
                    "Please enter your email"
            );

            etForgotEmail.requestFocus();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etForgotEmail.setError(
                    "Enter a valid email address"
            );

            etForgotEmail.requestFocus();

            return;
        }

        // Disable button while sending
        btnSendReset.setEnabled(false);
        btnSendReset.setText("Sending...");

        // Request
        ForgotPasswordRequest request =
                new ForgotPasswordRequest(email);

        SupabaseApi api =
                RetrofitClient
                        .getRetrofitInstance()
                        .create(SupabaseApi.class);

        api.resetPassword(request)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        btnSendReset.setEnabled(true);
                        btnSendReset.setText("SEND RESET LINK");

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Password reset email sent",
                                    Toast.LENGTH_LONG
                            ).show();

                            etForgotEmail.setText("");

                        } else {

                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Unable to send reset email",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        btnSendReset.setEnabled(true);
                        btnSendReset.setText("SEND RESET LINK");

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Network Error: "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}