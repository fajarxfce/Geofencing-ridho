package com.mona.geofencing.repository;

import static com.mona.geofencing.helper.StringHelper.usernameFromEmail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mona.geofencing.model.Child;
import com.mona.geofencing.model.LocationHistory;
import com.google.android.gms.maps.model.LatLng;
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

import org.apache.commons.logging.LogFactory;

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
    private final MutableLiveData<String> deleteLiveData;
    private final MutableLiveData<List<Child>> childrenLiveData;
    private final MutableLiveData<Child> childLiveData;
    private final MutableLiveData<List<LocationHistory>> locationHistoryLiveData;
    private final MutableLiveData<LatLng> coordinatesLiveData;
    private final DatabaseReference DBchilds;
    private final DatabaseReference DBpairCode;
    private final DatabaseReference DBparents;
    private final DatabaseReference DBHistory;

    public ChildRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        errorSaveLiveData = new MutableLiveData<>();
        successSaveLiveData = new MutableLiveData<>();
        deleteLiveData = new MutableLiveData<>();
        childrenLiveData = new MutableLiveData<>();
        locationHistoryLiveData = new MutableLiveData<>();
        coordinatesLiveData = new MutableLiveData<>();
        childLiveData = new MutableLiveData<>();
        DBchilds = FirebaseDatabase.getInstance().getReference("childs");
        DBpairCode = FirebaseDatabase.getInstance().getReference("child_pair_code");
        DBparents = FirebaseDatabase.getInstance().getReference("parents");
        DBHistory = FirebaseDatabase.getInstance().getReference("location_history");
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
        return deleteLiveData;
    }

    public MutableLiveData<String> getSuccessSaveLiveData() {
        return successSaveLiveData;
    }

    private int generatePairCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }

    public MutableLiveData<Child> getChildLiveData() {
        return childLiveData;
    }

    public void checkPairCode(String pairCode) {
        DBpairCode.child(pairCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String childId = dataSnapshot.child("childId").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);

                    Child child = new Child(username, email, childId, pairCode);
                    childLiveData.postValue(child);
                    Log.d(TAG, "checkchild: childexist");
                } else {
                    childLiveData.postValue(null);
                    Log.d(TAG, "checkchild: childnotexist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                childLiveData.postValue(null);
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

    public void saveParentToChild(String fcmToken, Child child) {
        DBchilds
                .child(child.getChildId())
                .child("parent_fcm_tokens")
                .child(firebaseAuth.getUid())
                .setValue(fcmToken)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            successSaveLiveData.postValue("Anak berhasil disimpan");
                        } else {
                            errorSaveLiveData.postValue("Anak gagal disimpan");
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

    public void fetchParentFcmTokens(String childUid, MutableLiveData<List<String>> parentFcmTokensLiveData) {
        DBchilds.child(childUid)
                .child("parent_fcm_tokens")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> parentFcmTokens = new ArrayList<>();
                for (DataSnapshot tokenSnapshot : snapshot.getChildren()) {
                    String token = tokenSnapshot.getValue(String.class);
                    Log.d(TAG, "fetch-token: "+token);
                    if (token != null) {
                        parentFcmTokens.add(token);
                    }
                }
                parentFcmTokensLiveData.postValue(parentFcmTokens);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    public void fetchChildren(String parentUid) {
        DBparents
                .child(parentUid)
                .child("childs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Child> children = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Child child = snapshot.getValue(Child.class);
                            children.add(child);
                            Log.d(TAG, "onDataChange: "+ snapshot.getValue(Child.class).getPairCode());
                        }

                        childrenLiveData.postValue(children);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                    }
                });
    }

    public void fetchLocationHistory(String childUid) {
        DBHistory
                .child(childUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<LocationHistory> historyList = new ArrayList<>();
                        for (DataSnapshot historySnapshot : snapshot.getChildren()) {
                            String message = historySnapshot.getValue(String.class);
                            historyList.add(new LocationHistory(message));

                        }
                        locationHistoryLiveData.postValue(historyList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
    }

    public void getChildrenData(String uuid){
        DBchilds
                .child(uuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String childId = snapshot.child("childId").getValue(String.class);
                String pairCode = snapshot.child("pairCode").getValue(String.class);
                Child child = new Child(username, email, childId, pairCode);
                Log.d(TAG, "onDataChange: "+pairCode);
                childLiveData.postValue(child);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    public MutableLiveData<List<LocationHistory>> getLocationHistoryLiveData() {
        return locationHistoryLiveData;
    }

    public MutableLiveData<List<Child>> getChildrenLiveData() {
        return childrenLiveData;
    }

    public void fetchChildCoordinates(String childUid) {
        DBchilds
                .child(childUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Double latitude = snapshot.child("latitude").getValue(Double.class);
                        Double longitude = snapshot.child("longitude").getValue(Double.class);
                        if (latitude != null && longitude != null) {
                            coordinatesLiveData.postValue(new LatLng(latitude, longitude));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors.
                    }
                });
    }

    public MutableLiveData<LatLng> getCoordinatesLiveData() {
        return coordinatesLiveData;
    }

    public void deleteChildFromParent(String parentUid, String pairCode) {
        DBparents
                .child(parentUid)
                .child("childs")
                .child(pairCode)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "deleteChildFromParent: success");
                        deleteLiveData.postValue("Anak berhasil dihapus");
                    } else {
                        Log.d(TAG, "deleteChildFromParent: failed");
                        deleteLiveData.postValue("Anak gagal dihapus");
                    }
                })
                .addOnFailureListener(e -> {
                    deleteLiveData.postValue("Anak gagal dihapus");
                });
        ;
    }

    public MutableLiveData<String> getDeleteLiveData() {
        return deleteLiveData;
    }

    public void updateFcmToken(String parentUid, List<Child> child, String fcmToken) {
        for (Child c : child) {
            DBchilds
                    .child(c.getChildId())
                    .child("parent_fcm_tokens")
                    .child(parentUid)
                    .setValue(fcmToken);
        }
    }
}
