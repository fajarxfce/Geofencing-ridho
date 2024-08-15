package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.model.Child;
import com.example.geofencing.model.LocationHistory;
import com.example.geofencing.repository.ChildRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChildViewModel extends ViewModel {
    private final ChildRepository repository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Child> childLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pairCodeExists = new MutableLiveData<>();
    private final MutableLiveData<String> successSaveLiveData;
    private final MutableLiveData<String> errorSaveLiveData;
    private final MutableLiveData<List<Child>> childrenLiveData;
    private final MutableLiveData<List<LocationHistory>> locationHistoryLiveData;
    private final MutableLiveData<LatLng> coordinatesLiveData;


    public ChildViewModel() {
        repository = new ChildRepository();
        userLiveData = repository.getUserLiveData();
        errorLiveData = repository.getErrorLiveData();
        successSaveLiveData = repository.getSuccessSaveLiveData();
        errorSaveLiveData = repository.getErrorSaveLiveData();
        locationHistoryLiveData = repository.getLocationHistoryLiveData();
        coordinatesLiveData = repository.getCoordinatesLiveData();
        childrenLiveData = new MutableLiveData<>();
    }

    public void register(String email, String password) {
        repository.register(email, password);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void login(String email, String password) {
        repository.login(email, password);
    }

    public LiveData<Boolean> getPairCodeExists() {
        return pairCodeExists;
    }

    public LiveData<Child> getChildLiveData() {
        return childLiveData;
    }

    public void checkPairCode(String pairCode) {
        repository.checkPairCode(pairCode, new ChildRepository.PairCodeCallback() {
            @Override
            public void onExist(Child child) {
                pairCodeExists.postValue(true);
                childLiveData.postValue(child);
            }

            @Override
            public void onNotExist() {
                pairCodeExists.postValue(false);
            }
        });
    }

    public void saveChildToParent(String parentUid, String childUid, Child child) {
        repository.saveChildToParent(parentUid, childUid, child);
    }

    public void saveParentToChild(String fcmToken, Child child) {
        repository.saveParentToChild(fcmToken, child);
    }

    public LiveData<String> getSuccessSaveLiveData() {
        return successSaveLiveData;
    }

    public LiveData<String> getErrorSaveLiveData() {
        return errorSaveLiveData;
    }

    public LiveData<List<Child>> getChildrenLiveData() {
        return repository.getChildrenLiveData();
    }

    public void fetchChildren(String parentUid) {
        repository.fetchChildren(parentUid);
    }

    public LiveData<LatLng> getCoordinatesLiveData() {
        return coordinatesLiveData;
    }

    public void fetchChildCoordinates(String childUid) {
        repository.fetchChildCoordinates(childUid);
    }

    public void deleteChildFromParent(String parentUid, String childUid) {
        repository.deleteChildFromParent(parentUid, childUid, new ChildRepository.DeleteChildCallback() {
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

    public LiveData<List<LocationHistory>> getLocationHistoryLiveData() {
        return locationHistoryLiveData;
    }

    public void fetchLocationHistory(String childUid) {
        repository.fetchLocationHistory(childUid);
    }
}
