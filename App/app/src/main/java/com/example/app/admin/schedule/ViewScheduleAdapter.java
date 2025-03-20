package com.example.app.admin.schedule;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.api.ApiService;
import com.example.app.api.RetrofitClient;
import com.example.app.response.DeleteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewScheduleAdapter extends RecyclerView.Adapter<ViewScheduleAdapter.ViewHolder> {

    private List<ViewScheduleMC> scheduleList;
    private Context context;

    public ViewScheduleAdapter(List<ViewScheduleMC> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_schedule_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewScheduleMC schedule = scheduleList.get(position);
        holder.busName.setText(schedule.getBusName());

        holder.remove.setOnClickListener(view -> removeBus(schedule, position));
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busName;
        ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busName = itemView.findViewById(R.id.txtBusName);
            remove = itemView.findViewById(R.id.remove);
        }
    }

    private void removeBus(ViewScheduleMC schedule, int position) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<DeleteResponse> call = apiService.deleteSchedule(schedule.getId());

        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    scheduleList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, scheduleList.size());
                } else {
                    Toast.makeText(context, "Failed to delete schedule", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(context, "Error deleting schedule", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
