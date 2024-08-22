package com.mona.geofencing.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolygonRepository {
    private static final String TAG = "PolygonRepository";
    private DatabaseReference childRef, historyRef;
    private FirebaseAuth mAuth;

    public PolygonRepository() {
        childRef = FirebaseDatabase.getInstance().getReference("childs");
        historyRef = FirebaseDatabase.getInstance().getReference("location_history");
        mAuth = FirebaseAuth.getInstance();
    }

    public void fetchPolygonData(MutableLiveData<Map<String, List<LatLng>>> polygonPoints) {
        String childUid = mAuth.getCurrentUser().getUid();
        childRef
                .child(childUid)
                .child("polygons")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, List<LatLng>> polygons = new HashMap<>();
                        for (DataSnapshot polygonSnapshot : snapshot.getChildren()) {
                            List<LatLng> points = new ArrayList<>();
                            for (DataSnapshot pointSnapshot : polygonSnapshot.getChildren()) {
                                double latitude = pointSnapshot.child("latitude").getValue(Double.class);
                                double longitude = pointSnapshot.child("longitude").getValue(Double.class);
                                points.add(new LatLng(latitude, longitude));
                            }
                            polygons.put(polygonSnapshot.getKey(), points);
                        }
                        Log.d(TAG, "onDataChange: " + polygons.size());
                        polygonPoints.setValue(polygons);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
    }

    public void saveLocationHistory(String message) {
        String childUid = mAuth.getCurrentUser().getUid();
        historyRef
                .child(childUid)
                .push().setValue(message);
    }

    public void saveCoordinates(String childId, double latitude, double longitude) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("latitude", latitude);
        updates.put("longitude", longitude);

        childRef
                .child(childId)
                .updateChildren(updates);
    }
}
