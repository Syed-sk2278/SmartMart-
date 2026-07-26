package com.example.smartmartplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etPassword;
    private Button btnRegister, btnPhone;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

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

        // Get User Input
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        // Validation

        if (name.isEmpty()) {
            etName.setError("Enter your name");
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Enter your phone number");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Enter your password");
            return;
        }


        // Create Register Request Object

        RegisterRequest request = new RegisterRequest(
                name,
                phone,
                password
        );


        // Create API Instance

        SupabaseApi api = RetrofitClient
                .getRetrofitInstance()
                .create(SupabaseApi.class);


        // Call Register API

        Call<RegisterResponse> call =
                api.registerUser(request);


        // Send Request

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