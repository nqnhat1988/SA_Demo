package com.nhatdear.sademo;

import android.app.AlertDialog;
import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatdear.sademo.models.SA_User;

/**
 * Created by nhatdear on 4/24/17.
 */

public class StashAwayApp extends Application {
    public static final String TAG = StashAwayApp.class
            .getSimpleName();
    private static StashAwayApp mInstance;
    private boolean isConnectedToFirebase;
    private SA_User currentUser;
    private long estimatedServerTimeMs = 0;
    private long maximumTimeOffsetInMinutes = 10;

    public static synchronized StashAwayApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        //allow firebase works in offline mode
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //get firebase connection info
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.e("FIREBASE", "Connected");
                    isConnectedToFirebase = true;
                } else {
                    Log.e("FIREBASE", "Disconnected");
                    isConnectedToFirebase = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FIREBASE", "Listener was cancelled");
                isConnectedToFirebase = false;
            }
        });

        //calculate server time offset
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                double offset = snapshot.getValue(Double.class);
                if (offset > maximumTimeOffsetInMinutes * 60 * 1000) {
                    new AlertDialog.Builder(getApplicationContext())
                            .setCancelable(false)
                            .setTitle("ERROR")
                            .setMessage("Your device has above 10 minutes different from our server! Please check your device time.")
                            .setPositiveButton("OK", (dialogInterface, i) -> System.exit(0)).create().show();
                } else {
                    estimatedServerTimeMs = System.currentTimeMillis() + Double.valueOf(offset).longValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                estimatedServerTimeMs = System.currentTimeMillis();
            }
        });
    }

    public long getEstimatedServerTime() {
        if (checkConnectionState()) {
            return estimatedServerTimeMs / 1000;
        } else {
            return System.currentTimeMillis() / 1000;
        }
    }

    public boolean checkConnectionState() {
        return isConnectedToFirebase;
    }

    public SA_User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SA_User currentUser) {
        this.currentUser = currentUser;
    }
}
