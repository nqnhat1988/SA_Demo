package com.nhatdear.sademo.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nhatdear.sademo.R;
import com.google.firebase.auth.FirebaseAuth;

public class SA_BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;
    
    protected FirebaseAuth mAuth;
    
    protected FirebaseAuth.AuthStateListener mAuthListener;
    
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.loading));
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        } else if (mProgressDialog.isShowing()) {

        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void initFirebaseBasic() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
        };
    }

    //update text on dialog
    public void updateText(String text) {
        mProgressDialog.setMessage(text);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebaseBasic();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
