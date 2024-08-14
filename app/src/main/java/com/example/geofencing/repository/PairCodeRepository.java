package com.example.geofencing.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PairCodeRepository {
    private final DatabaseReference databaseReference;

    public PairCodeRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("pair_codes");
    }

    public interface PairCodeCallback {
        void onPairCodeChecked(boolean exists);
    }

    public void checkPairCode(String pairCode, PairCodeCallback callback) {
        databaseReference.child(pairCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onPairCodeChecked(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onPairCodeChecked(false);
            }
        });
    }
}
