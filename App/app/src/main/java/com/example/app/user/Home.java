package com.example.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.user.track.MapActivity;
import com.example.app.R;
import com.example.app.user.schedule.UserViewSchedule;
import com.example.app.user.bus.ViewBus;
import com.example.app.user.profile.Profile;

public class Home extends AppCompatActivity {

    private ImageView trackImg, studImg, scheduleImg, busImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        trackImg = findViewById(R.id.imgTrack);
        studImg = findViewById(R.id.studImg);
        scheduleImg = findViewById(R.id.scheduleImg);
        busImg = findViewById(R.id.busImg);

        trackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, MapActivity.class);
                startActivity(i);
            }
        });

        studImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Profile.class);
                startActivity(i);
            }
        });

        scheduleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, UserViewSchedule.class);
                startActivity(i);
            }
        });

        busImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ViewBus.class);
                startActivity(i);
            }
        });


    }
}