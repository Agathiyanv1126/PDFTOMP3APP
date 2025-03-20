package com.example.app.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.Bus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectBuses extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SelectBusesAdapter adapter;
    List<Bus> busList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buses);


        recyclerView = findViewById(R.id.busRecyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fetchBusData();
    }

    private void fetchBusData() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        Call<List<Bus>> call = apiService.getAllBuses();
        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    busList = response.body();
                    adapter = new SelectBusesAdapter(busList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(SelectBuses.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(SelectBuses.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

