package com.example.geofencing.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.geofencing.model.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.geofencing.helper.StringHelper.usernameFromEmail;

public class ParentRepository {
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final DatabaseReference databaseReference;

    public ParentRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("parents");
    }

    public void register(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userLiveData.postValue(firebaseAuth.getCurrentUser());
                        saveParentData(user);
                        firebaseAuth.signOut();
                    } else {
                        String error = task.getException().getMessage();
                        if (error.contains("email address is already in use")) {
                            error = "Email sudah digunakan";
                        }else if (error.contains("The email address is badly formatted.")) {
                            error = "Format email salah";
                        }
                        errorLiveData.postValue(error);
                    }
                });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        userLiveData.postValue(user);
                    } else {
                        errorLiveData.postValue(task.getException().getMessage());
                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
        userLiveData.postValue(null);
    }

    private void saveParentData(FirebaseUser user) {
        String uid = user.getUid();
        String email = user.getEmail();
        String username = usernameFromEmail(user.getEmail());
        Parent parent = new Parent(uid, email, username);
        databaseReference.child(user.getUid()).setValue(parent);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }


}
