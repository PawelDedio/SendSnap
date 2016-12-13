package com.example.pdedio.sendsnap;

/**
 * Created by p.dedio on 31.08.16.
 */
public abstract class BasePresenter implements BaseContract.BasePresenter {

    public abstract void afterViews();

    public abstract void destroy();

    public void onPause(){}

    public void onResume(){}
}
