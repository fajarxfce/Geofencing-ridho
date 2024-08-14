package com.example.geofencing.repository;

import static com.example.geofencing.helper.StringHelper.usernameFromEmail;

import androidx.lifecycle.MutableLiveData;

import com.example.geofencing.model.Child;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AuthChildRepository {
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final DatabaseReference DBchilds;
    private final DatabaseReference DBpairCode;

    public AuthChildRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        DBchilds = FirebaseDatabase.getInstance().getReference("childs");
        DBpairCode = FirebaseDatabase.getInstance().getReference("child_pair_code");
    }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                        saveChildData(user);
                        firebaseAuth.signOut();
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    private void saveChildData(FirebaseUser user) {
        String uid = user.getUid();
        String email = user.getEmail();
        String username = usernameFromEmail(user.getEmail());
        int pairCode = generatePairCode();
        Child child = new Child(username, email, uid);
        DBchilds.child(user.getUid()).setValue(child);
        DBpairCode.child(String.valueOf(pairCode))
                .setValue(child);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    private int generatePairCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }
}
