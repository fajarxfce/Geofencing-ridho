package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.model.CustomLatLng;
import com.example.geofencing.model.Polygon;
import com.example.geofencing.repository.AreaRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class AreaViewModel extends ViewModel {
    private final AreaRepository repository;
    private final MutableLiveData<List<Polygon>> areasLiveData;
    private final MutableLiveData<Boolean> saveSuccessLiveData;


    public AreaViewModel() {
        repository = new AreaRepository();
        areasLiveData = new MutableLiveData<>();
        saveSuccessLiveData = new MutableLiveData<>();
    }

    public LiveData<Boolean> getSaveSuccessLiveData() {
        return saveSuccessLiveData;
    }

    public LiveData<List<Polygon>> getAreasLiveData() {
        return areasLiveData;
    }

    public void fetchAreasForParent(String parentUid) {
        repository.getAreasForParent(parentUid, new AreaRepository.GetAreasCallback() {
            @Override
            public void onAreasFetched(List<Polygon> areas) {
                areasLiveData.postValue(areas);
            }

            @Override
            public void onError(String errorMessage) {
                areasLiveData.postValue(null);
            }
        });
    }

    public void saveArea(String polygonName, List<LatLng> points) {
        repository.saveArea(polygonName, points, new AreaRepository.SaveAreaCallback() {
            @Override
            public void onSuccess() {
                saveSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                saveSuccessLiveData.postValue(false);
            }
        });
    }
}
