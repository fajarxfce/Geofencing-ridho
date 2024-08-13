package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class ParentRegisterViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;

    public ParentRegisterViewModel() {
        authRepository = new AuthRepository();
        userLiveData = authRepository.getUserLiveData();
        errorLiveData = authRepository.getErrorLiveData();
    }

    public void register(String email, String password) {
        authRepository.register(email, password);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
