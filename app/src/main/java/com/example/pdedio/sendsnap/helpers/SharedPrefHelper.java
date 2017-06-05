package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by p.dedio on 04.11.16.
 */
@SharedPref
public interface SharedPrefHelper {

    @DefaultInt(0)
    int cameraId();

    @DefaultInt(10)
    int snapDuration();

    @DefaultBoolean(true)
    boolean notificationDisplay();

    @DefaultBoolean(true)
    boolean notificationLed();

    @DefaultBoolean(false)
    boolean notificationVibration();

    @DefaultBoolean(false)
    boolean notificationSound();

    @DefaultString("")
    String userId();

    @DefaultString("")
    String userToken();
}
