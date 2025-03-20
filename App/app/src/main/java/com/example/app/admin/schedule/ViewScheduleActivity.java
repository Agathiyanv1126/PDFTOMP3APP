package com.example.app.admin.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.admin.AdminUpdates;
import com.example.app.admin.ChangeDetails;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.ViewScheduleResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewScheduleActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ViewScheduleAdapter adapter;
    LinearLayoutManager layoutManager;

    List<ViewScheduleMC> userlist;
    Button btnCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        recyclerView = findViewById(R.id.viewSchedulerecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnCreate = findViewById(R.id.btnCreateSchedule);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewScheduleActivity.this, CreateScheduleActivity.class);
                startActivity(i);
            }
        });

        fetchSchedule();

    }

    private void fetchSchedule() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ViewScheduleResponse> call = apiService.getSchedule();

        call.enqueue(new Callback<ViewScheduleResponse>() {
            @Override
            public void onResponse(Call<ViewScheduleResponse> call, Response<ViewScheduleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userlist = response.body().getBuses();
                    adapter = new ViewScheduleAdapter(userlist,ViewScheduleActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ViewScheduleActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewScheduleResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(ViewScheduleActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}