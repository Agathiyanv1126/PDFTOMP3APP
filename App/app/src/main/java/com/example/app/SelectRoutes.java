package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.Bus;
import com.example.app.response.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRoutes extends AppCompatActivity {

    private RecyclerView routesRecyclerview;
    private RoutesAdapter routesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_routes);

        routesRecyclerview = findViewById(R.id.Routesrecyclerview);
        routesRecyclerview = findViewById(R.id.Routesrecyclerview);
        routesRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        fetchBusData();


    }

    private void fetchBusData() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);


      //  Call<LoginResponse> call = apiService.loginUser(requestBody);
        Call<List<Bus>> call = apiService.getAllBuses();

        call.enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Bus> busList = response.body();
                    routesAdapter = new RoutesAdapter(busList);
                    routesRecyclerview.setAdapter(routesAdapter);
                } else {
                    Log.e("API Error", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
            }
        });
    }

}




//        userlist = new ArrayList<>();
//        userlist.add(new ModelClass("wsedrfgt"));
//        userlist.add(new ModelClass("fdsf"));
//        userlist.add(new ModelClass("gfg"));
//        userlist.add(new ModelClass("dg"));
//        userlist.add(new ModelClass("wsedrfgt"));
//        userlist.add(new ModelClass("fdsf"));
//        userlist.add(new ModelClass("gfg"));
//        userlist.add(new ModelClass("dg"));userlist.add(new ModelClass("wsedrfgt"));
//        userlist.add(new ModelClass("fdsf"));
//        userlist.add(new ModelClass("gfg"));
//        userlist.add(new ModelClass("dg"));



//        adapter=new RoutesAdapter(userlist);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();