package com.example.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText nameEt, emailEt, passwordEt;
    Button registerBtn;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1️⃣ Check JWT token in SharedPreferences
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String jwtToken = prefs.getString("JWT_TOKEN", null);

        boolean tokenValid = false;

        if (jwtToken != null && !jwtToken.isEmpty()) {
            try {
                // 2️⃣ Decode JWT payload
                String[] parts = jwtToken.split("\\."); // header.payload.signature
                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));

                JSONObject json = new JSONObject(payload);
                long exp = json.getLong("exp"); // expiry timestamp in seconds
                long now = System.currentTimeMillis() / 1000; // current time in seconds

                if (now < exp) {
                    tokenValid = true; // token is still valid
                } else {
                    // Token expired → remove from prefs
                    prefs.edit().remove("JWT_TOKEN").apply();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Malformed token → remove from prefs
                prefs.edit().remove("JWT_TOKEN").apply();
            }
        }

        if (tokenValid) {
            // Token valid → skip MainActivity
            String name = prefs.getString("USER_NAME", "");
            String email = prefs.getString("USER_EMAIL", "");
            String id = prefs.getString("USER_ID", "");

            Intent intent = new Intent(MainActivity.this, HomePage.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
            return; // stop further execution
        }

        // If token missing or expired → normal registration/login flow
        setContentView(R.layout.activity_main);

        nameEt = findViewById(R.id.name);
        emailEt = findViewById(R.id.emailRegistration);
        passwordEt = findViewById(R.id.passwordRegistration);
        registerBtn = findViewById(R.id.buttonNavigate);
        login = findViewById(R.id.signining);

        login.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
        });

        registerBtn.setOnClickListener(v -> {

            String name = nameEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            RegisterRequest request = new RegisterRequest(name, email, password);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            apiService.registerUser(request)
                    .enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.isSuccessful()) {
                                Toast.makeText(MainActivity.this,
                                        "Registered Successfully",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(
                                        MainActivity.this,
                                        SecondActivity.class));

                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Email already exists",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(MainActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        });
    }
}