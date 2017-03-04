package com.example.pdedio.sendsnap.helpers;

import org.androidannotations.annotations.EBean;

/**
 * Created by p.dedio on 21.12.16.
 */
@EBean
public class AndroidManager {

    public int getSdkCode() {
        return android.os.Build.VERSION.SDK_INT;
    }
}
