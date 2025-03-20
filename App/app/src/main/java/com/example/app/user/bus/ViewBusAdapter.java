package com.example.app.user.bus;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.admin.AdminLogin;
import com.example.app.auth.Login;
import com.example.app.response.Bus;
import com.example.app.user.BusDetails;

import java.util.ArrayList;
import java.util.List;

public class ViewBusAdapter extends RecyclerView.Adapter<ViewBusAdapter.ViewHolder> implements Filterable {

    private List<Bus> busList;
    private List<Bus> filteredBusList;

    private Context context;

    public ViewBusAdapter(List<Bus> busList, Context context){
        this.busList=busList;
        this.filteredBusList = new ArrayList<>(busList); // Create a copy for filtering

        this.context = context;
    }


    @NonNull
    @Override
    public ViewBusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_buses2_items,parent, false);
        return new ViewBusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBusAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Bus bus = filteredBusList.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BusDetails.class);
                i.putExtra("busName",bus.getBusName());
                i.putExtra("busNumber",bus.getBusNumber());
                i.putExtra("driverName",bus.getDriverName());
                i.putExtra("driverNumber",bus.getDriverPhNumber());

                context.startActivity(i);
            }
        });

        holder.textView.setText(bus.getBusName());

    }

    @Override
    public int getItemCount() {
        return filteredBusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.txtBus2name);

        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Bus> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(busList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Bus bus : busList) {
                        if (bus.getBusName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(bus);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBusList.clear();
                filteredBusList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
