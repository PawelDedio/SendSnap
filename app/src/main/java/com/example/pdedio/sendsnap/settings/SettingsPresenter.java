package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.models.User;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 18.03.2017.
 */
@EBean
public class SettingsPresenter extends BaseFragmentPresenter implements SettingsContract.SettingsPresenter {

    protected SettingsContract.SettingsView view;

    @Bean
    protected SessionManager sessionManager;

    @Bean
    protected SharedPreferenceManager sharedPreferenceManager;




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
    public User getLoggedUser() {
        return this.sessionManager.getLoggedUser();
    }

    @Override
    public SharedPreferenceManager getSharedPreferenceManager() {
        return this.sharedPreferenceManager;
    }

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
