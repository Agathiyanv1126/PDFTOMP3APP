package com.example.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;

public class AdminStart extends AppCompatActivity {
    private Button btnGetStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_start);
        btnGetStart= findViewById(R.id.btnGetstart);

        btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminStart.this, AdminLogin.class);
                startActivity(i);
            }
        });



    }
}