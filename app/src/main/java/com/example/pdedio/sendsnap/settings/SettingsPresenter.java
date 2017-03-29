package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.Consts;
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

    private User loggedUser;




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
        if(this.loggedUser == null) {
            this.loggedUser = this.sessionManager.getLoggedUser();
        }

        return this.loggedUser;
    }

    @Override
    public SharedPreferenceManager getSharedPreferenceManager() {
        return this.sharedPreferenceManager;
    }

    @Override
    public void onDisplayNameClick(String displayName) {
        this.view.showTextInputDialog(R.string.settings_enter_display_name, R.string.user_display_name,
                Consts.USER_DISPLAY_NAME_MIN_LENGTH, Consts.USER_DISPLAY_NAME_MAX_LENGTH,
                this.getLoggedUser().displayName, new TextInputDialog.ResultListener() {
                    @Override
                    public void onValueSet(String value) {
                        getLoggedUser().setDisplayName(value);
                    }
                });
    }

    @Override
    public void onDisplaySwitchStateChange(boolean isChecked) {
        this.sharedPreferenceManager.setNotificationDisplay(isChecked);
    }

    @Override
    public void onLedSwitchStateChange(boolean isChecked) {
        this.sharedPreferenceManager.setNotificationLed(isChecked);
    }

    @Override
    public void onVibrationSwitchStateChange(boolean isChecked) {
        this.sharedPreferenceManager.setNotificationVibration(isChecked);
    }

    @Override
    public void onSoundSwitchStateChange(boolean isChecked) {
        this.sharedPreferenceManager.setNotificationSound(isChecked);
    }

    @Override
    public void onLogOutClick() {

    }
}
