package com.example.pdedio.sendsnap.presenters.fragments;

import org.androidannotations.annotations.EBean;

/**
 * Created by p.dedio on 28.11.16.
 */
@EBean
public class SelectSnapRecipientPresenter extends BaseFragmentPresenter {

    private PresenterCallback presenterCallback;



    //Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }


    @Override
    public void afterViews() {

    }

    @Override
    public void destroy() {
        this.presenterCallback = null;
    }



    public interface PresenterCallback {

    }
}
