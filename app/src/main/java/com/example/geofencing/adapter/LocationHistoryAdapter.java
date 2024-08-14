package com.example.geofencing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geofencing.databinding.LocationHistoryAdapterBinding;
import com.example.geofencing.model.LocationHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.ViewHolder>{

    List<LocationHistory> historyList = new ArrayList<>();
    OnItemClickListener listener;
    OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int i);
    }

    public void updateLocationHistoryList(List<LocationHistory> newLocationHistoryList) {
        Collections.reverse(newLocationHistoryList);
        this.historyList = newLocationHistoryList;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public LocationHistoryAdapter(List<LocationHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public LocationHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LocationHistoryAdapterBinding binding = LocationHistoryAdapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHistoryAdapter.ViewHolder holder, int position) {
        holder.binding.tvName.setText(historyList.get(position).getMessage());
        holder.binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LocationHistoryAdapterBinding binding;
        public ViewHolder(LocationHistoryAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
