package com.example.app.user.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.ScheduleModelClass;
import com.example.app.admin.schedule.ViewScheduleMC;

import java.util.List;

public class UserViewScheduleAdapter extends RecyclerView.Adapter<UserViewScheduleAdapter.ViewHolder>{

    private List<ViewScheduleMC> scheduleList;
    private Context context;

    public UserViewScheduleAdapter(List<ViewScheduleMC> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view_schedule_items, parent, false);
        return new UserViewScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewScheduleAdapter.ViewHolder holder, int position) {
        ViewScheduleMC schedule = scheduleList.get(position);
        holder.busName.setText(schedule.getBusName());

    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView busName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busName = itemView.findViewById(R.id.txtBusName2);
        }
    }

}
