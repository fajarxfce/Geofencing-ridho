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
    private final MutableLiveData<List<Polygon>> unassignedPolygonsLiveData;
    private final MutableLiveData<List<Polygon>> childPolygonsLiveData;


    public AreaViewModel() {
        repository = new AreaRepository();
        areasLiveData = new MutableLiveData<>();
        saveSuccessLiveData = new MutableLiveData<>();
        unassignedPolygonsLiveData = new MutableLiveData<>();
        childPolygonsLiveData = new MutableLiveData<>();
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

    public void assignPolygonToChild(String childUid, String polygonName, List<CustomLatLng> points) {
        repository.assignPolygonToChild(childUid, polygonName, points, new AreaRepository.AssignPolygonCallback() {
            @Override
            public void onSuccess() {
                // Handle success
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure
            }
        });
    }

    public void fetchUnassignedPolygons(String parentUid, String childUid) {
        repository.fetchUnassignedPolygons(parentUid, childUid, new AreaRepository.FetchUnassignedPolygonsCallback() {
            @Override
            public void onUnassignedPolygonsFetched(List<Polygon> polygons) {
                unassignedPolygonsLiveData.postValue(polygons);
            }

            @Override
            public void onError(String errorMessage) {
                unassignedPolygonsLiveData.postValue(null);
            }
        });
    }

    public LiveData<List<Polygon>> getUnassignedPolygonsLiveData() {
        return unassignedPolygonsLiveData;
    }

    public LiveData<List<Polygon>> getChildPolygonsLiveData() {
        return childPolygonsLiveData;
    }

    public void fetchChildPolygons(String childUid) {
        repository.fetchChildPolygons(childUid, new AreaRepository.FetchChildPolygonsCallback() {
            @Override
            public void onChildPolygonsFetched(List<Polygon> polygons) {
                childPolygonsLiveData.postValue(polygons);
            }

            @Override
            public void onError(String errorMessage) {
                childPolygonsLiveData.postValue(null);
            }
        });
    }

    public void deletePolygonFromChild(String childUid, String polygonName) {
        repository.deletePolygonFromChild(childUid, polygonName, new AreaRepository.DeletePolygonCallback() {
            @Override
            public void onSuccess() {
                // Handle success if needed
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure if needed
            }
        });
    }
}
