package com.example.smartmartplus;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        // ==========================================
        // FIND VIEWS
        // ==========================================

        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);

        // ==========================================
        // RESET PASSWORD
        // ==========================================

        btnResetPassword.setOnClickListener(v -> resetPassword());

        // ==========================================
        // BACK TO LOGIN
        // ==========================================

        tvBackToLogin.setOnClickListener(v -> finish());
    }


    // ==========================================
    // RESET PASSWORD
    // ==========================================

    private void resetPassword() {

        String email = etEmail.getText()
                .toString()
                .trim();

        // ==========================================
        // VALIDATION
        // ==========================================

        if (email.isEmpty()) {

            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }


        // ==========================================
        // SHOW LOADING
        // ==========================================

        setLoading(true);


        try {

            // ==========================================
            // JSON BODY
            // ==========================================

            JSONObject json = new JSONObject();

            json.put("email", email);


            // ==========================================
            // REQUEST BODY
            // ==========================================

            MediaType mediaType =
                    MediaType.parse("application/json");

            RequestBody body =
                    RequestBody.create(
                            json.toString(),
                            mediaType
                    );


            // ==========================================
            // SUPABASE RECOVERY URL
            // ==========================================

            String baseUrl = Constants.SUPABASE_URL;

            if (!baseUrl.endsWith("/")) {
                baseUrl = baseUrl + "/";
            }

            String recoverUrl =
                    baseUrl + "auth/v1/recover";


            // ==========================================
            // REQUEST
            // ==========================================

            Request request =
                    new Request.Builder()
                            .url(recoverUrl)
                            .post(body)
                            .addHeader(
                                    "apikey",
                                    Constants.SUPABASE_KEY
                            )
                            .addHeader(
                                    "Content-Type",
                                    "application/json"
                            )
                            .build();


            // ==========================================
            // SEND REQUEST
            // ==========================================

            client.newCall(request).enqueue(
                    new Callback() {

                        @Override
                        public void onFailure(
                                Call call,
                                IOException e) {

                            runOnUiThread(() -> {

                                setLoading(false);

                                Toast.makeText(
                                        ForgotPasswordActivity.this,
                                        "Network error. Please try again.",
                                        Toast.LENGTH_LONG
                                ).show();
                            });
                        }


                        @Override
                        public void onResponse(
                                Call call,
                                Response response)
                                throws IOException {

                            final int code =
                                    response.code();

                            final String responseBody =
                                    response.body() != null
                                            ? response.body().string()
                                            : "";


                            runOnUiThread(() -> {

                                setLoading(false);


                                // ==================================
                                // SUCCESS
                                // ==================================

                                if (code >= 200 && code < 300) {

                                    Toast.makeText(
                                            ForgotPasswordActivity.this,
                                            "Password reset email sent. Check your inbox.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    etEmail.setText("");


                                    // Stay on page so user can
                                    // read the message.

                                }

                                // ==================================
                                // ERROR
                                // ==================================

                                else {

                                    String message =
                                            "Unable to send reset email.";

                                    try {

                                        JSONObject errorJson =
                                                new JSONObject(responseBody);

                                        if (errorJson.has("msg")) {

                                            message =
                                                    errorJson.getString("msg");

                                        } else if (
                                                errorJson.has("message")) {

                                            message =
                                                    errorJson.getString("message");

                                        } else if (
                                                errorJson.has("error_description")) {

                                            message =
                                                    errorJson.getString(
                                                            "error_description"
                                                    );
                                        }

                                    } catch (Exception ignored) {
                                    }


                                    Toast.makeText(
                                            ForgotPasswordActivity.this,
                                            message,
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            });
                        }
                    }
            );

        } catch (Exception e) {

            setLoading(false);

            Toast.makeText(
                    this,
                    "Something went wrong. Please try again.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // ==========================================
    // LOADING
    // ==========================================

    private void setLoading(boolean loading) {

        if (loading) {

            btnResetPassword.setEnabled(false);
            btnResetPassword.setText("Sending...");

            progressBar.setVisibility(View.VISIBLE);

        } else {

            btnResetPassword.setEnabled(true);
            btnResetPassword.setText("Send Reset Link");

            progressBar.setVisibility(View.GONE);
        }
    }
}