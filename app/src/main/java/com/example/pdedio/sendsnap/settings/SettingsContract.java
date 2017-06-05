package com.example.pdedio.sendsnap.settings;

import android.content.Context;
import android.support.annotation.StringRes;

import com.example.pdedio.sendsnap.BaseFragmentContract;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.models.User;

/**
 * Created by pawel on 18.03.2017.
 */

public class SettingsContract {

    public interface SettingsPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(SettingsView view);

        User getLoggedUser();

        SharedPreferenceManager getSharedPreferenceManager();

        void onDisplayNameClick(String displayName);

        void onDisplaySwitchStateChange(boolean isChecked);

        void onLedSwitchStateChange(boolean isChecked);

        void onVibrationSwitchStateChange(boolean isChecked);

        void onSoundSwitchStateChange(boolean isChecked);

        void onLogOutClick();
    }

    public interface SettingsView extends BaseFragmentContract.BaseFragmentView {

        void showTextInputDialog(@StringRes int title, @StringRes int fieldName, int minLength, int maxLength, String initialValue, TextInputDialog.ResultListener resultListener);

        Context getApplicationContext();
    }
}
