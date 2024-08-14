package com.example.geofencing.repository;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
//        String areaId = databaseReference.child("areas").push().getKey();

        Map<String, Object> areaData = new HashMap<>();
        areaData.put("name", polygonName);
        areaData.put("points", points);

//        databaseReference.child("users")
//                .child(userId)
//                .child("areas")
//                .child(areaId)
//                .setValue(areaData)
//
//                .addOnSuccessListener(aVoid -> callback.onSuccess())
//                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));

        databaseReference.child("polygons")
                .child(polygonName)
//                .child(areaId)
                .setValue(points)

                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface SaveAreaCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
