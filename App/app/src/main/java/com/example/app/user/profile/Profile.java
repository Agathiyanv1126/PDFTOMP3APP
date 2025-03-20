package com.example.app.user.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.auth.Login;
import com.example.app.response.ProfileById;
import com.example.app.user.Home;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    private TextView nameTextView, registerTextView;

    private SharedPreferences sharedPreferences;

    private String userId;
    private LinearLayout homeLay;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        homeLay = findViewById(R.id.homeLay);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);


        if (!isLoggedIn) {
            Intent intent = new Intent(Profile.this, Login.class);
            startActivity(intent);
            finish();
        }

        homeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Home.class);
                startActivity(i);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(Profile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Profile.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        nameTextView = findViewById(R.id.txtName);
        registerTextView = findViewById(R.id.txtRegNo);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", null);

        if (userId != null) {
            fetchUserById(userId);
        } else {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchUserById(String userId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ProfileById> call = apiService.getUserById(userId);

        call.enqueue(new Callback<ProfileById>() {
            @Override
            public void onResponse(Call<ProfileById> call, Response<ProfileById> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProfileById user = response.body();
                    nameTextView.setText(user.getName());
                    registerTextView.setText(user.getRegisterNumber());
                } else {
                    Toast.makeText(Profile.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileById> call, Throwable t) {
                Log.e("API_ERROR", "Error fetching user", t);
                Toast.makeText(Profile.this, "Failed to load user", Toast.LENGTH_SHORT).show();
            }
        });
    }

}