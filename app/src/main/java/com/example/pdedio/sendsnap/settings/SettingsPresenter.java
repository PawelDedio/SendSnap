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
        this.view.showStatusBar();
    }

    @Override
    public void destroy() {
        this.view.hideStatusBar();
        this.view = null;
    }


    //Settings presenter methods
    @Override
    public void onDisplayNameClick(String displayName) {

    }

    @Override
    public void onDisplaySwitchStateChange(boolean isChecked) {

    }

    @Override
    public void onLedSwitchStateChange(boolean isChecked) {

    }

    @Override
    public void onVibrationSwitchStateChange(boolean isChecked) {

    }

    @Override
    public void onSoundSwitchStateChange(boolean isChecked) {

    }

    @Override
    public void onLogOutClick() {

    }
}
