package com.example.app.auth;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.user.Home;
import com.example.app.user.profile.Profile;

public class GetStartedActivity extends AppCompatActivity {

    private Button btnGetStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);


        if (isLoggedIn) {
            Intent intent = new Intent(GetStartedActivity.this, Home.class);
            startActivity(intent);
            finish();
        }
//        else{
//            Intent intent = new Intent(GetStartedActivity.this, Home.class);
//            startActivity(intent);
//            finish();
//        }

        btnGetStart = findViewById(R.id.btnGetStarted);

        btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetStartedActivity.this, Signup.class);
                startActivity(i);
            }
        });

    }
}