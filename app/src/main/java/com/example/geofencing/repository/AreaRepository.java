package com.example.geofencing.repository;

import com.example.geofencing.model.CustomLatLng;
import com.example.geofencing.model.Polygon;
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

public class AreaRepository {
    private final DatabaseReference databaseReference;

    public AreaRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void saveArea(String polygonName, List<LatLng> points, SaveAreaCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("parents")
                .child(userId)
                .child("polygons")
                .child(polygonName)
                .setValue(points)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));

        databaseReference.child("polygons")
                .child(polygonName)
                .setValue(points)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getAreasForParent(String parentUid, GetAreasCallback callback) {
        databaseReference
                .child("parents")
                .child(parentUid)
                .child("polygons")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Polygon> polygons = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getKey();
                    List<CustomLatLng> points = new ArrayList<>();
                    for (DataSnapshot pointSnapshot : snapshot.getChildren()) {
                        CustomLatLng point = pointSnapshot.getValue(CustomLatLng.class);
                        points.add(point);
                    }
                    polygons.add(new Polygon(name, points));
                }
                callback.onAreasFetched(polygons);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public interface GetAreasCallback {
        void onAreasFetched(List<Polygon> areas);

        void onError(String errorMessage);
    }

    public interface SaveAreaCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}
