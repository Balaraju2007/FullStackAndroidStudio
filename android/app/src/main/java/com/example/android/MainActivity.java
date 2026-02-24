package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_main);

        nameEt = findViewById(R.id.name);
        emailEt = findViewById(R.id.emailRegistration);
        passwordEt = findViewById(R.id.passwordRegistration);
        registerBtn = findViewById(R.id.buttonNavigate);
        login = findViewById(R.id.signining);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(i);
            }
        });

        registerBtn.setOnClickListener(v -> {

            String name = nameEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            RegisterRequest request =
                    new RegisterRequest(name, email, password);

            ApiService apiService =
                    ApiClient.getClient().create(ApiService.class);

            apiService.registerUser(request)
                    .enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                               Response<ResponseBody> response) {

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
                        public void onFailure(Call<ResponseBody> call,
                                              Throwable t) {

                            Toast.makeText(MainActivity.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        });
    }
}