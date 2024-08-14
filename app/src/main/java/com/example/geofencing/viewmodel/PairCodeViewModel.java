package com.example.geofencing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geofencing.repository.PairCodeRepository;

public class PairCodeViewModel extends ViewModel {

    private final PairCodeRepository repository;
    private final MutableLiveData<Boolean> pairCodeExists = new MutableLiveData<>();

    public PairCodeViewModel() {
        repository = new PairCodeRepository();
    }

    public LiveData<Boolean> getPairCodeExists() {
        return pairCodeExists;
    }

    public void checkPairCode(String pairCode) {
        repository.checkPairCode(pairCode, pairCodeExists::setValue);
    }

}
