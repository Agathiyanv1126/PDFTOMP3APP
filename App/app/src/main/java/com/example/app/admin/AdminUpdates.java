package com.example.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.admin.removeBus.RemoveBus;
import com.example.app.admin.schedule.CreateScheduleActivity;
import com.example.app.admin.schedule.ViewScheduleActivity;

public class AdminUpdates extends AppCompatActivity {
    private Button addBus, removeBus, changeDet, updateSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_updates);

        addBus = findViewById(R.id.btnAddBus);
        removeBus = findViewById(R.id.btnRemoveBus);
        changeDet = findViewById(R.id.btnChangeDetails);
        updateSchedule = findViewById(R.id.btnUpdateSchedule);


        addBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminUpdates.this, AddBus.class);
                startActivity(i);
            }
        });

        removeBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminUpdates.this, RemoveBus.class);
                startActivity(i);
            }
        });

        changeDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminUpdates.this, ChangeDetails.class);
                startActivity(i);
            }
        });

        updateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminUpdates.this, ViewScheduleActivity.class);
                startActivity(i);
            }
        });
    }
}
