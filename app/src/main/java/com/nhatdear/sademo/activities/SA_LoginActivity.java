package com.nhatdear.sademo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.nhatdear.sademo.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nhatdear.sademo.StashAwayApp;
import com.nhatdear.sademo.events.SA_ErrorEvent;
import com.nhatdear.sademo.fragments.SA_LoginFragment;
import com.nhatdear.sademo.fragments.SA_ResetPasswordFragment;
import com.nhatdear.sademo.fragments.SA_SignUpFragment;
import com.nhatdear.sademo.fragments.SA_SimpleDialogFragment;
import com.nhatdear.sademo.fragments.SA_WelcomeFragment;
import com.nhatdear.sademo.helpers.SA_Helper;
import com.nhatdear.sademo.models.SA_User;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import static com.nhatdear.sademo.helpers.SA_Helper.LAST_ACCOUNT;
import static com.nhatdear.sademo.helpers.SA_Helper.LAST_USERNAME;

public class SA_LoginActivity extends SA_BaseActivity implements
        SA_LoginFragment.OnFragmentInteractionListener,
        SA_SignUpFragment.OnFragmentInteractionListener,
        SA_WelcomeFragment.OnFragmentInteractionListener,
        SA_ResetPasswordFragment.OnFragmentInteractionListener
{
    private static final String TAG = SA_LoginActivity.class.getSimpleName();

    private CallbackManager callbackManager;

    private boolean firstInit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @SuppressLint("CommitTransaction")
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getSupportFragmentManager().beginTransaction();
                        SA_SimpleDialogFragment fragment = SA_SimpleDialogFragment.newInstance();

                        fragment.addOnBtnOkClickListener(s -> {
                            handleFacebookAccessToken(loginResult.getAccessToken(), s);
                            fragment.dismiss();
                        });

                        fragment.addOnBtnCancelClickListener(() -> {
                            Log.d(TAG, "FB Sign out!!!");
                            LoginManager.getInstance().logOut();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, SA_WelcomeFragment.newInstance()).addToBackStack("WELCOME");
                            fragmentTransaction.commit();
                            fragment.dismiss();
                            hideProgressDialog();
                        });
                        fragment.show(getSupportFragmentManager(), "DIALOG");
                    }

                    @Override
                    public void onCancel() {
                        hideProgressDialog();
                        Log.d(TAG, "facebook cancel");
                        handleFacebookError(new Exception("Cancel by User !!!"));
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        hideProgressDialog();
                        handleFacebookError(exception);
                        Log.d(TAG, "facebook error " + exception.toString());
                    }
                });
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                if (firstInit) {
                    goToMain();
                }
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            firstInit = false;
        };
        // [END auth_state_listener]

        // Check last account exist
        SharedPreferences sharedPreferences = getSharedPreferences(SA_Helper.USER_PREF, MODE_PRIVATE);
        String lastAccount = sharedPreferences.getString(LAST_ACCOUNT, "");
        if (SA_Helper.softCheckValidString(lastAccount)) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction().replace(R.id.fragment_container, SA_WelcomeFragment.newInstance()).addToBackStack("WELCOME");
            fragmentTransaction2.commit();
            fragmentManager.executePendingTransactions();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, SA_LoginFragment.newInstance(lastAccount)).addToBackStack("LOGIN");
            fragmentTransaction.commit();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction().replace(R.id.fragment_container, SA_WelcomeFragment.newInstance()).addToBackStack("WELCOME");
            fragmentTransaction2.commit();
            fragmentManager.executePendingTransactions();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void goToMain() {
        showProgressDialog("Loading User Informations");
        FirebaseUser user = mAuth.getCurrentUser();
        SA_LoginActivity _this = this;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SA_User sa_user = dataSnapshot.getValue(SA_User.class);
                StashAwayApp.getInstance().setCurrentUser(sa_user);
                saveUserInformation(sa_user);
                SA_Helper.goToMainWithClearFlag(SA_LoginActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                _this.hideProgressDialog();
            }
        };
        SA_User.load(user.getUid(),postListener);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void createAccount(String userName, String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        //noinspection ConstantConditions
                        Log.e(TAG, task.getException().getMessage());
                        SA_Helper.showSnackbar(SA_LoginActivity.this.findViewById(R.id.fragment_container), task.getException().getMessage());
                        hideProgressDialog();
                    } else {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        SA_User user = new SA_User(firebaseUser);
                        user.name = userName;
                        user.nameLowerCase = userName.toLowerCase();
                        user.save().addOnCompleteListener(saveTask -> signIn(email, password));
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(SA_LoginActivity.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        goToMain();
                    }

                    hideProgressDialog();
                });
        // [END sign_in_with_email]
    }

    private void saveUserInformation(SA_User sa_user) {
        SharedPreferences sharedPreferences = getSharedPreferences(SA_Helper.USER_PREF, MODE_PRIVATE);
        sharedPreferences.edit().putString(LAST_ACCOUNT, sa_user.getEmail()).apply();
        sharedPreferences.edit().putString(LAST_USERNAME, sa_user.getName()).apply();
    }

    @Override
    public void startLoginWithEmailFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, SA_SignUpFragment.newInstance()).addToBackStack("EMAIL_LOGIN");
        fragmentTransaction.commit();
    }

    @Override
    public void startLoginWithFBAction() {
        showProgressDialog();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]

    // [START auth_with_facebook]
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
    private void handleFacebookAccessToken(AccessToken token, String s) {
        showProgressDialog();
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {

                        Log.w(TAG, "signInWithCredential", task.getException());
                        Toast.makeText(SA_LoginActivity.this, "This password is incorrect.",
                                Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    } else {

                        FirebaseUser firebaseUser = task.getResult().getUser();
                        SA_User user = new SA_User(firebaseUser);
                        user.name = s;
                        user.nameLowerCase = s.toLowerCase();
                        user.photoUrl = firebaseUser.getPhotoUrl().toString();
                        user.save().addOnCompleteListener(saveTask -> {
                            if (saveTask.isSuccessful()) {
                                goToMain();
                            } else {
                                Log.e(TAG, saveTask.getException().getMessage());
                                SA_Helper.showSnackbar(SA_LoginActivity.this.findViewById(R.id.fragment_container), saveTask.getException().getMessage());
                            }
                            hideProgressDialog();
                        });
                    }
                });
    }

    private void handleFacebookError(Exception e) {
        SA_Helper.showSnackbar(SA_LoginActivity.this.findViewById(R.id.fragment_container), e.getMessage());
    }
    // [END auth_with_facebook]
    @Override
    public void onSignUpWithEmail(String userName, String email, String password) {
        Log.d(TAG, String.format("onSignUpWithEmail %s - %s - %s", userName, email, password));
        createAccount(userName, email, password);
    }

    @Override
    public void doLoginWithEmail(String userName, String password) {
        signIn(userName, password);
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
    @Override
    public void doResetPasswordAction(String email) {
        showProgressDialog();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SA_LoginFragment.newInstance("")).addToBackStack("EMAIL_LOGIN").commit();
                    } else {
                        Log.e(TAG, task.getException().getMessage());
                        EventBus.getDefault().post(new SA_ErrorEvent("Error in sent email"));
                    }
                    hideProgressDialog();
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.getRepeatCount() == 0) {
                Log.d(TAG, "onKeyDown Called");
                android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment != null && fragment instanceof SA_WelcomeFragment) {
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
