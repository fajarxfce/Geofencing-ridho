package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.repository.ChildRepository;
import com.google.firebase.auth.FirebaseUser;

public class ChildViewModel extends ViewModel {
    private final ChildRepository repository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Boolean> pairCodeExists = new MutableLiveData<>();

    public ChildViewModel() {
        repository = new ChildRepository();
        userLiveData = repository.getUserLiveData();
        errorLiveData = repository.getErrorLiveData();
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

    public void checkPairCode(String pairCode) {
        repository.checkPairCode(pairCode, pairCodeExists::setValue);
    }
}
