package com.example.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

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

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SecondActivity.this,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            apiService.loginUser(request)
                    .enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                               Response<ResponseBody> response) {

                            if (response.isSuccessful() && response.body() != null) {

                                try {
                                    String responseString = response.body().string();

                                    JSONObject jsonObject = new JSONObject(responseString);

                                    String message = jsonObject.getString("message");
                                    String token = jsonObject.getString("token");
                                    try {
                                        Log.d("API_RESPONSE", jsonObject.toString(4)); // formatted JSON
                                    } catch (Exception e) {
                                        Log.d("API_RESPONSE", jsonObject.toString()); // fallback plain JSON
                                    }

                                    SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
//                                    prefs.edit().putString("JWT_TOKEN", token).apply();
//                                    Log.d("JWT_TOKEN", "Token saved in SharedPreferences");


                                    JSONObject userObject =
                                            jsonObject.getJSONObject("user");

                                    String name = userObject.getString("name");
                                    String userEmail = userObject.getString("email");
                                    String id = userObject.getString("id");

                                    prefs.edit().putString("JWT_TOKEN", token)
                                            .putString("USER_NAME", name)
                                            .putString("USER_EMAIL", userEmail)
                                            .putString("USER_ID", id)
                                            .apply();




                                    Toast.makeText(SecondActivity.this,
                                            message,
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(
                                            SecondActivity.this,
                                            HomePage.class);

                                    intent.putExtra("name", name);
                                    intent.putExtra("email", userEmail);
                                    intent.putExtra("id", id);

                                    startActivity(intent);
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(SecondActivity.this,
                                            "Parsing Error",
                                            Toast.LENGTH_SHORT).show();
                                }

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