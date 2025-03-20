package com.example.app.user.schedule;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.admin.schedule.ViewScheduleMC;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.ViewScheduleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewSchedule extends AppCompatActivity {

    RecyclerView recyclerView;
    UserViewScheduleAdapter adapter;
    LinearLayoutManager layoutManager;

    List<ViewScheduleMC> userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_schedule);

        recyclerView = findViewById(R.id.userviewSchedulerecycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                    adapter = new UserViewScheduleAdapter(userlist, UserViewSchedule.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(UserViewSchedule.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewScheduleResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(UserViewSchedule.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}