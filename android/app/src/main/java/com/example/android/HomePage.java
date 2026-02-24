package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    TextView nameTv, emailTv, idTv;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        nameTv = findViewById(R.id.nameTextView);
        emailTv = findViewById(R.id.emailTextView);
        idTv = findViewById(R.id.idTextView);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Receive data from Intent
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String id = getIntent().getStringExtra("id");

        nameTv.setText("Name: " + name);
        emailTv.setText("Email: " + email);
        idTv.setText("User ID: " + id);

        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, SecondActivity.class);
            startActivity(intent);
            finish();
        });
    }
}