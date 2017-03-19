package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;

import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 18.03.2017.
 */
@EBean
public class SettingsPresenter extends BaseFragmentPresenter implements SettingsContract.SettingsPresenter {

    protected SettingsContract.SettingsView view;




    //Lifecycle
    @Override
    public void init(SettingsContract.SettingsView view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        this.view = null;
    }
}
