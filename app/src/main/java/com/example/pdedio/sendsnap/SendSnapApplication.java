package com.example.pdedio.sendsnap;

import android.app.Application;

import com.karumi.dexter.Dexter;

import org.androidannotations.annotations.EApplication;

/**
 * Created by p.dedio on 26.09.16.
 */
@EApplication
public class SendSnapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
    }
}
