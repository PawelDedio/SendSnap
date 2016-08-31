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
    @RootContext
    protected void setContext(Context context) {
        if(context instanceof PresenterCallback) {
            this.presenterCallback = (PresenterCallback) context;
        }
    }

    @Override
    public void afterViews() {
        Log.e("CameraPresenter", "afterViews");
    }

    @Override
    public void destroy() {

    }


    public interface PresenterCallback {

    }
}
