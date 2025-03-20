package com.example.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;

public class AdminLogin extends AppCompatActivity {

    private Button button;
    private EditText edtEmail, edtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        button = findViewById(R.id.btnAdminlogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();

                if (isValidCredentials(email, password)) {
                    startActivity(new Intent(AdminLogin.this, AdminUpdates.class));
                } else {
                    Toast.makeText(AdminLogin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            };

        });
}

    private boolean isValidCredentials(String email, String password) {
        return email.equals("admin@gmail.com") && password.equals("admin");
    }
}