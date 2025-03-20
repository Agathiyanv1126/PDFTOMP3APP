package com.example.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;

public class BusDetails extends AppCompatActivity {

    private TextView busName, busNumber, driverName, driverNumber;

    private String bName, bNumber, dName, dNumber;
    private LinearLayout home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        busName = findViewById(R.id.txtBusName);
        busNumber = findViewById(R.id.txtBusNumber);
        driverName = findViewById(R.id.txtDriverName);
        driverNumber = findViewById(R.id.txtDriverPh);
        home = findViewById(R.id.homeLay);

        bName=getIntent().getStringExtra("busName");
        bNumber=getIntent().getStringExtra("busNumber");
        dName=getIntent().getStringExtra("driverName");
        dNumber=getIntent().getStringExtra("driverNumber");

        busName.setText(bName);
        busNumber.setText(bNumber);
        driverName.setText(dName);
        driverNumber.setText(dNumber);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusDetails.this, Home.class);
                startActivity(i);
            }
        });


    }
}