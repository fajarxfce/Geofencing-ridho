package com.mona.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mona.geofencing.repository.ParentRepository;
import com.google.firebase.auth.FirebaseUser;

public class ParentViewModel extends ViewModel {
    private final ParentRepository repository;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;

    public ParentViewModel() {
        repository = new ParentRepository();
        userLiveData = repository.getUserLiveData();
        errorLiveData = repository.getErrorLiveData();
    }

    public void register(String email, String password) {
        repository.register(email, password);
    }
    public void login(String email, String password) {
        repository.login(email, password);
    }
    public void logout() {
        repository.logout();
    }
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<String> getRegisterLiveData() {
        return repository.getRegisterLiveData();
    }
}
