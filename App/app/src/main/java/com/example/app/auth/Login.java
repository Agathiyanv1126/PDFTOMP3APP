package com.example.app.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.admin.AdminLogin;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.LoginResponse;
import com.example.app.user.Home;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText edtName, edtRegNo, edtPassword;
    private Button btnlogin;
    private TextView admin;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        edtName = findViewById(R.id.username);
        edtRegNo = findViewById(R.id.regnum);
        edtPassword = findViewById(R.id.pass2);
        btnlogin = findViewById(R.id.btnlogin);
        admin = findViewById(R.id.txtAdmin);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, AdminLogin.class);
                startActivity(i);
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

        private void login() {
            String regNo = edtRegNo.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (regNo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("register_number", regNo);
            requestBody.put("password", password);

            Call<LoginResponse> call = apiService.loginUser(requestBody);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse.isSuccess()) {
                            Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("USER_ID", loginResponse.getStudent().getId());
                            editor.putBoolean("IS_LOGGED_IN", true);
                            editor.apply();

                            Intent i = new Intent(Login.this, Home.class);
                            startActivity(i);

                            Log.d("API", "User Name: " + loginResponse.getStudent().getName());
                        } else {
                            Toast.makeText(Login.this, "Login failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "Login failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(Login.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", t.getMessage(), t);
                }
            });
        }


}