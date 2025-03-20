package com.example.app.admin.schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.ScheduleResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateScheduleActivity extends AppCompatActivity {

    private EditText edtBusName, edtBusNumber;
    private Button btnScheduleBus;
    private EditText edtDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        edtDate = findViewById(R.id.txtDate);
        edtBusName = findViewById(R.id.bus_name);
        edtBusNumber = findViewById(R.id.bus_number);
        btnScheduleBus = findViewById(R.id.add_more_schedule);

        btnScheduleBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleBus();
            }
        });
    }

    private void scheduleBus() {
        String date = edtDate.getText().toString().trim();
        String busName = edtBusName.getText().toString().trim();
        String busNumber = edtBusNumber.getText().toString().trim();

        if (date.isEmpty() || busName.isEmpty() || busNumber.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("date", date);
        requestBody.put("bus_name", busName);
        requestBody.put("bus_number", busNumber);

        Call<ScheduleResponse> call = apiService.scheduleBus(requestBody);
        call.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScheduleResponse scheduleResponse = response.body();
                    if (scheduleResponse.isSuccess()) {
                        Toast.makeText(CreateScheduleActivity.this, scheduleResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateScheduleActivity.this, "Failed: " + scheduleResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateScheduleActivity.this, "Request failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toast.makeText(CreateScheduleActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }

}