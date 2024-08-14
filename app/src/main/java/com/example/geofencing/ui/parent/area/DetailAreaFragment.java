package com.example.geofencing.ui.parent.area;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geofencing.R;
import com.example.geofencing.databinding.FragmentDetailAreaBinding;
import com.example.geofencing.model.CustomLatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class DetailAreaFragment extends Fragment {

    private static final String TAG = "DetailAreaFragment";
    private FragmentDetailAreaBinding binding;
    private GoogleMap mMap;
    private List<LatLng> points = new ArrayList<>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            ArrayList<CustomLatLng> customLatlng = getArguments().getParcelableArrayList("points");

            if (customLatlng != null) {
                for (CustomLatLng customLatLng : customLatlng) {
                    LatLng latLng = new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude());
                    points.add(latLng);
                }
            }

            drawPolygon();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailAreaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void drawPolygon() {
        mMap.clear();
        PolygonOptions polygon = new PolygonOptions();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            mMap.addMarker(new MarkerOptions().position(point));
            polygon.add(point);
            boundsBuilder.include(point);
        }
        polygon.fillColor(R.color.purple_700);
        mMap.addPolygon(polygon);

        // Move and zoom the camera to the polygon area
        LatLngBounds bounds = boundsBuilder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}