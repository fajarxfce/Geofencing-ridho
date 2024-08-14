package com.example.geofencing.repository;

import static com.example.geofencing.helper.StringHelper.usernameFromEmail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.geofencing.model.Child;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChildRepository {
    private static final String TAG = "ChildRepository";
    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<String> errorSaveLiveData;
    private final MutableLiveData<String> successSaveLiveData;
    private final MutableLiveData<List<Child>> childrenLiveData;
    private final DatabaseReference DBchilds;
    private final DatabaseReference DBpairCode;
    private final DatabaseReference DBparents;

    public ChildRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        errorSaveLiveData = new MutableLiveData<>();
        successSaveLiveData = new MutableLiveData<>();
        childrenLiveData = new MutableLiveData<>(new ArrayList<>());
        DBchilds = FirebaseDatabase.getInstance().getReference("childs");
        DBpairCode = FirebaseDatabase.getInstance().getReference("child_pair_code");
        DBparents = FirebaseDatabase.getInstance().getReference("parents");
    }

    public interface PairCodeCallback {
        void onExist(Child child);
        void onNotExist();
    }

    public interface ChildrenCallback {
        void onChildrenFetched(List<Child> children);
        void onError(String error);
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

    private void saveChildData(FirebaseUser user) {
        String uid = user.getUid();
        String email = user.getEmail();
        String username = usernameFromEmail(user.getEmail());
        int pairCode = generatePairCode();
        Child child = new Child(username, email, uid, String.valueOf(pairCode));
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

    public MutableLiveData<String> getErrorSaveLiveData() {
        return errorSaveLiveData;
    }

    public MutableLiveData<String> getSuccessSaveLiveData() {
        return successSaveLiveData;
    }

    private int generatePairCode(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }

    public void checkPairCode(String pairCode, PairCodeCallback callback) {
        DBpairCode.child(pairCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String childId = dataSnapshot.child("childId").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);

                    Child child = new Child(username, email, childId, pairCode);
                    callback.onExist(child);
                } else {
                    callback.onNotExist();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onNotExist();
            }
        });
    }

    public void saveChildToParent(String parentUid, String pairCode, Child child) {
        DBparents
                .child(parentUid)
                .child("childs")
                .child(pairCode)
                .setValue(child)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            successSaveLiveData.postValue("Anak berhasil disimpan");
                        } else {
                            errorSaveLiveData.postValue("Kode pairing tidak ditemukan");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorSaveLiveData.postValue("Kode pairing tidak ditemukan");
                    }
                });
    }

    public void fetchChildren(String parentUid) {
        DBparents.child(parentUid)
                .child("childs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Child> children = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Child child = snapshot.getValue(Child.class);
                    children.add(child);
                }
                childrenLiveData.postValue(children);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Child>> getChildrenLiveData() {
        return childrenLiveData;
    }

    public void deleteChildFromParent(String parentUid, String pairCode, DeleteChildCallback callback) {
        DBparents
                .child(parentUid)
                .child("childs")
                .child(pairCode)
                .removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface DeleteChildCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

}
