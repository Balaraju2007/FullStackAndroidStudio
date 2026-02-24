package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.signin);

        loginBtn.setOnClickListener(v -> {

            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            LoginRequest request =
                    new LoginRequest(email, password);

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            apiService.loginUser(request)
                    .enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                               Response<ResponseBody> response) {
                            if (response.isSuccessful()) {

                                Toast.makeText(SecondActivity.this,
                                        "Login Successful",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(
                                        SecondActivity.this,
                                        HomePage.class));

                                finish();   // optional

                            } else {

                                Toast.makeText(SecondActivity.this,
                                        "Invalid Email or Password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call,
                                              Throwable t) {

                            Toast.makeText(SecondActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        });
    }
}