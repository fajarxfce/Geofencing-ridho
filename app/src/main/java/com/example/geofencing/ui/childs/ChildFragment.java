package com.example.geofencing.ui.childs;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.geofencing.R;
import com.example.geofencing.databinding.ActivityMainBinding;
import com.example.geofencing.databinding.FragmentChildBinding;
import com.example.geofencing.model.CustomLatLng;
import com.example.geofencing.model.Polygon;
import com.example.geofencing.services.LocationService;
import com.example.geofencing.utils.Contstants;
import com.example.geofencing.viewmodel.AreaViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChildFragment extends AppCompatActivity {

    private FragmentChildBinding binding;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private AreaViewModel viewModel;
    private FirebaseAuth mAuth;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            enableUserLocation();

            String childUid = mAuth.getCurrentUser().getUid();
            viewModel.fetchChildPolygons(childUid);
            viewModel.getChildPolygonsLiveData().observe(ChildFragment.this, polygons -> {
                if (polygons != null) {
                    for (Polygon polygon : polygons) {
                        List<LatLng> points = new ArrayList<>();
                        for (CustomLatLng customLatLng : polygon.getPoints()) {
                            points.add(new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude()));
                        }
                        mMap.addPolygon(new PolygonOptions().addAll(points).strokeColor(Color.RED).fillColor(Color.argb(128, 255, 0, 0)));
                    }
                }
            });

        }
    };

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(this, LocationService.class);
            intent.setAction(Contstants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location service is already running", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getLastLocation();
            startLocationService();
        } else {
            //Ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    currentLocation = location;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(AreaViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
}