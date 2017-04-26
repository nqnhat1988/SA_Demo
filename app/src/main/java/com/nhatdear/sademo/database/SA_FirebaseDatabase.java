package com.nhatdear.sademo.database;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatdear.sademo.models.SA_Portfolio;
import com.nhatdear.sademo.models.SA_User;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;

import static com.nhatdear.sademo.models.SA_Portfolio.convertDataSnapshotToPortfolios;

/**
 * Created by NhatNguyen on 11/24/2016.
 */

public class SA_FirebaseDatabase {
    private static final String TAG = SA_FirebaseDatabase.class.getSimpleName();
    private static final int MAX_PER_SEARCH = 1000;
    private FirebaseDatabase mDatabase;
    private String usersNode = "users";
    private String porfolioNode = "portfolio";
    private String dataNode = "data";

    public SA_FirebaseDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    public Task<Void> saveUser(SA_User user) {
        DatabaseReference usersRef = mDatabase.getReference(usersNode).child(user.uuid);
        return usersRef.setValue(user);
    }

    public Observable<ArrayList<SA_Portfolio>> getPortfolios(int currentSearchYear) {
        return Observable.create(subscriber->{
            if (subscriber.isDisposed()) {
                return;
            }

            DatabaseReference reference = mDatabase.getReference(dataNode).child(porfolioNode).child(String.valueOf(currentSearchYear));
            reference.keepSynced(true);
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    reference.keepSynced(false);
                    if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                        subscriber.onNext(new ArrayList<>());
                    } else {

                        ArrayList<SA_Portfolio> arrayList = convertDataSnapshotToPortfolios(dataSnapshot);

                        subscriber.onNext(arrayList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            };
            reference.orderByKey().addListenerForSingleValueEvent(postListener);
        });
    }

    public void savePortfolios(ArrayList<SA_Portfolio> arrayList) {
        DatabaseReference reference = mDatabase.getReference(dataNode).child(porfolioNode  + "Test");
        reference.setValue(arrayList);
    }

    public void getUserInformation(String uuid,ValueEventListener postListener) {
        DatabaseReference reference = mDatabase.getReference(usersNode).child(uuid);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(postListener);
    }
}
