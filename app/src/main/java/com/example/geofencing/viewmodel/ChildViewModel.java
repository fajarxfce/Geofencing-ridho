package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.model.Child;
import com.example.geofencing.repository.ChildRepository;
import com.google.firebase.auth.FirebaseUser;

public class ChildViewModel extends ViewModel {
    private final ChildRepository repository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Child> childLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> pairCodeExists = new MutableLiveData<>();
    private final MutableLiveData<String> successSaveLiveData;
    private final MutableLiveData<String> errorSaveLiveData;

    public ChildViewModel() {
        repository = new ChildRepository();
        userLiveData = repository.getUserLiveData();
        errorLiveData = repository.getErrorLiveData();
        successSaveLiveData = repository.getSuccessSaveLiveData();
        errorSaveLiveData = repository.getErrorSaveLiveData();
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

    public LiveData<String> getSuccessSaveLiveData() {
        return successSaveLiveData;
    }

    public LiveData<String> getErrorSaveLiveData() {
        return errorSaveLiveData;
    }

}
