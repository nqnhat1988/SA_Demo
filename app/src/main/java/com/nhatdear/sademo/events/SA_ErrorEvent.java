package com.nhatdear.sademo.events;

/**
 * Created by NhatNguyen on 11/22/2016.
 */

public class SA_ErrorEvent {
    public String mMessage;
    public Throwable mThrowable;
    public SA_ErrorEvent(String message) {
        this.mMessage = message;
    }

    public SA_ErrorEvent(Throwable mThrowable) {
        this.mThrowable = mThrowable;
    }

    public SA_ErrorEvent(String mMessage, Throwable mThrowable) {
        this.mMessage = mMessage;
        this.mThrowable = mThrowable;
    }
}
