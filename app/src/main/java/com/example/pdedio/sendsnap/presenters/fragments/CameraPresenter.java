package com.example.pdedio.sendsnap.presenters.fragments;

import android.content.Context;
import android.util.Log;

import com.example.pdedio.sendsnap.presenters.BasePresenter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by p.dedio on 31.08.16.
 */
@EBean
public class CameraPresenter extends BasePresenter {

    private PresenterCallback presenterCallback;



    ///Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }

    @Override
    public void afterViews() {
        Log.e("CameraPresenter", "afterViews");
        this.presenterCallback.changeText("Hello world!");
    }

    @Override
    public void destroy() {

    }


    public interface PresenterCallback {
        void changeText(String text);
    }
}
