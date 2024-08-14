package com.example.geofencing.ui.parent.area;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentAddAreaBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class AddAreaFragment extends Fragment {

    private FragmentAddAreaBinding binding;
    private GoogleMap mMap;
    private List<LatLng> points = new ArrayList<>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            setupMap();
        }
    };

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void setupMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapClickListener(this::onMapClick);
        binding.fabDelete.setOnClickListener(this::onDeleteClick);
        binding.ibSave.setOnClickListener(this::onSaveClick);
    }

    private void onMapClick(LatLng latLng) {
        binding.fabDelete.setVisibility(View.VISIBLE);
        binding.ibSave.setVisibility(View.VISIBLE);
        points.add(latLng);
        drawPolygon();
    }

    private void onDeleteClick(View v) {
        if (points.size() <= 1) {
            points.clear();
            mMap.clear();
            binding.fabDelete.setVisibility(View.GONE);
            binding.ibSave.setVisibility(View.GONE);
        } else {
            points.remove(points.size() - 1);
            drawPolygon();
        }
    }

    private void onSaveClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("points", (ArrayList<? extends Parcelable>) points);
         EnterAreaNameDialog dialog = new EnterAreaNameDialog(requireContext());
         dialog.setArguments(bundle);
         dialog.show(getParentFragmentManager(), "EnterAreaNameDialog");
    }

    private void drawPolygon() {
        mMap.clear();
        PolygonOptions polygon = new PolygonOptions();
        for (LatLng point : points) {
            mMap.addMarker(new MarkerOptions().position(point));
            polygon.add(point);
        }
        polygon.fillColor(R.color.purple_700);
        mMap.addPolygon(polygon);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMap();
    }
}