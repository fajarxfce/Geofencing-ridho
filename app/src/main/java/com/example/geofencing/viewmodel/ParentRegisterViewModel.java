package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.repository.AuthParentRepository;
import com.google.firebase.auth.FirebaseUser;

public class ParentRegisterViewModel extends ViewModel {
    private final AuthParentRepository authParentRepository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;

    public ParentRegisterViewModel() {
        authParentRepository = new AuthParentRepository();
        userLiveData = authParentRepository.getUserLiveData();
        errorLiveData = authParentRepository.getErrorLiveData();
    }

    public void register(String email, String password) {
        authParentRepository.register(email, password);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
