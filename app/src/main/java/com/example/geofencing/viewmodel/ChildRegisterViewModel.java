package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.repository.AuthChildRepository;
import com.example.geofencing.repository.AuthParentRepository;
import com.google.firebase.auth.FirebaseUser;

public class ChildRegisterViewModel extends ViewModel {
    private final AuthChildRepository repository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;

    public ChildRegisterViewModel() {
        repository = new AuthChildRepository();
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
}
