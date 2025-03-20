package com.example.app.admin.removeBus;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.admin.RemoveRoutesAdapter;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.Bus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoveBus extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Bus>userlist;
    RemoveRoutesAdapter adapter;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_remove_bus);
        searchView = findViewById(R.id.searchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.Removerecyclerview);
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
                    userlist = response.body();
                    adapter = new RemoveRoutesAdapter(userlist);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(RemoveBus.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(RemoveBus.this, "API call failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}