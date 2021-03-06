package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
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
}
