package com.example.app.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.auth.Login;
import com.example.app.R;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.BusResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  AddBus extends AppCompatActivity {

    private EditText edtBusName, edtBusNo, edtDriverName, edtDriverNo;
    private Button addbus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bus);

        edtBusName = findViewById(R.id.Bname);
        edtBusNo = findViewById(R.id.Bnum);
        edtDriverName = findViewById(R.id.Dname);
        edtDriverNo = findViewById(R.id.Dnum);

        addbus = findViewById(R.id.addbus);

        addbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(AddBus.this, DriverDetails.class);
//                startActivity(i);

                addBus();
        };



    });
}

    private void addBus() {
        // Validate input fields
        String busName = edtBusName.getText().toString().trim();
        String busNumber = edtBusNo.getText().toString().trim();
        String driverName = edtDriverName.getText().toString().trim();
        String driverPhoneNumber = edtDriverNo.getText().toString().trim();

        if (busName.isEmpty() || busNumber.isEmpty() || driverName.isEmpty() || driverPhoneNumber.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare API client
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Prepare request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("bus_name", busName);
        requestBody.put("bus_number", busNumber);
        requestBody.put("driver_name", driverName);
        requestBody.put("driver_ph_number", driverPhoneNumber);

        Call<BusResponse> call = apiService.createBus(requestBody);
        call.enqueue(new Callback<BusResponse>() {
            @Override
            public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent i = new Intent(AddBus.this, AdminUpdates.class);
                    startActivity(i);

                    Toast.makeText(AddBus.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddBus.this, "bus created failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BusResponse> call, Throwable t) {
                Toast.makeText(AddBus.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });


    }
}

