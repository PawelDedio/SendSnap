package com.example.pdedio.sendsnap.ui.activities;

import android.support.v4.app.FragmentActivity;

import com.example.pdedio.sendsnap.presenters.BasePresenter;

/**
 * Created by pawel on 19.09.2016.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    public abstract BasePresenter getPresenter();
}
