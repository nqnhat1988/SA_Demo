package com.nhatdear.sademo.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.nhatdear.sademo.StashAwayApp;
import com.nhatdear.sademo.database.SA_FirebaseDatabase;


/**
 * Created by NhatNguyen on 11/24/2016.
 */

public class SA_User {
    public String uuid = "";
    public String email = "";
    public String name = "";
    public String desc = "";
    public String providers = "";
    public String photoUrl = "";
    public String lastArtwork = "";
    public String userType = "";
    public Long ts_lastupdated = 0L;
    public int status;
    public String nameLowerCase;

    public SA_User() {

    }

    public SA_User(String email, String name, String desc, String lastArtwork) {
        this.email = email;
        this.name = name;
        this.desc = desc;
        this.lastArtwork = lastArtwork;
    }

    public SA_User(FirebaseUser firebaseUser) {
        this.uuid = firebaseUser.getUid();
        this.email = firebaseUser.getEmail();
        this.providers = firebaseUser.getProviders().toString();
    }

    public static void load(String uuid, ValueEventListener postListener) {
        SA_FirebaseDatabase sa_firebaseDatabase = new SA_FirebaseDatabase();
        sa_firebaseDatabase.getUserInformation(uuid, postListener);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLastArtwork() {
        return lastArtwork;
    }

    public void setLastArtwork(String lastArtwork) {
        this.lastArtwork = lastArtwork;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getTs_lastupdated() {
        return ts_lastupdated;
    }

    public void setTs_lastupdated(Long ts_lastupdated) {
        this.ts_lastupdated = ts_lastupdated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Task<Void> save() {
        SA_FirebaseDatabase dp_firebaseDatabase = new SA_FirebaseDatabase();
        this.ts_lastupdated = StashAwayApp.getInstance().getEstimatedServerTime();
        return dp_firebaseDatabase.saveUser(this);
    }
//
//    public void saveSettings(EnumUtils.SETTING_MODE settingMode, Object... params) {
//        DP_FirebaseDatabase dp_firebaseDatabase = new DP_FirebaseDatabase();
//        dp_firebaseDatabase.saveSettings(this, settingMode, params);
//    }
//
//    public void saveLastArtwork(String uuid) {
//        DP_FirebaseDatabase dp_firebaseDatabase = new DP_FirebaseDatabase();
//        dp_firebaseDatabase.saveUserLastArtwork(this, uuid);
//    }

    @Override
    public String toString() {
        return String.format("%s - %s", name, uuid);
    }
}
