package com.example.geofencing.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.repository.PolygonRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PolygonViewModel extends ViewModel {

//    private MutableLiveData<List<LatLng>> polygonPoints;
//    private PolygonRepository polygonRepository;
//
//    public PolygonViewModel() {
//        polygonPoints = new MutableLiveData<>();
//        polygonRepository = new PolygonRepository();
//    }
//
//    public LiveData<List<LatLng>> getPolygonPoints() {
//        return polygonPoints;
//    }
//
//    public void fetchPolygonData() {
//        polygonRepository.fetchPolygonData(polygonPoints);
//    }
}
