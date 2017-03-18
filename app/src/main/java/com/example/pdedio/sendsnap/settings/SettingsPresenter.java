package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;

/**
 * Created by pawel on 18.03.2017.
 */

public class SettingsPresenter extends BaseFragmentPresenter implements SettingsContract.SettingsPresenter {

    private SettingsContract.SettingsView view;




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
