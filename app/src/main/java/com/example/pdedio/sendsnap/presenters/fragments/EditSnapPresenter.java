package com.example.pdedio.sendsnap.presenters.fragments;

import com.example.pdedio.sendsnap.presenters.BasePresenter;

import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class EditSnapPresenter extends BasePresenter {


    private PresenterCallback presenterCallback;




    // Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }


    @Override
    public void afterViews() {

    }

    @Override
    public void destroy() {

    }


    public interface PresenterCallback {

    }
}
