package com.example.geofencing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geofencing.R;
import com.example.geofencing.model.Polygon;

import java.util.List;

public class PolygonAdapter extends RecyclerView.Adapter<PolygonAdapter.PolygonViewHolder> {

    private List<Polygon> polygonList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Polygon polygon);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public PolygonAdapter(List<Polygon> polygonList) {
        this.polygonList = polygonList;
    }

    @NonNull
    @Override
    public PolygonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_polygon, parent, false);
        return new PolygonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolygonViewHolder holder, int position) {
        Polygon polygon = polygonList.get(position);
        holder.nameTextView.setText(polygon.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position,polygon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return polygonList.size();
    }

    public void updateData(List<Polygon> newPolygonList) {
        polygonList.clear();
        polygonList.addAll(newPolygonList);
        notifyDataSetChanged();
    }

    public static class PolygonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public PolygonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
        }
    }
}
