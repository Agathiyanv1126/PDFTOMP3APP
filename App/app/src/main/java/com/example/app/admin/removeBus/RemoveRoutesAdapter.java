package com.example.app.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.response.Bus;

import java.util.ArrayList;
import java.util.List;

public class RemoveRoutesAdapter extends RecyclerView.Adapter<RemoveRoutesAdapter.ViewHolder> implements Filterable {

    private List<Bus> busList;
    private List<Bus> filteredBusList;

    public RemoveRoutesAdapter(List<Bus>busList){
        this.busList=busList;
        this.filteredBusList = new ArrayList<>(busList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.remove_bus_items,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bus bus = filteredBusList.get(position);

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

            textView=itemView.findViewById(R.id.myTextView);

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
