package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragmentContract;

/**
 * Created by pawel on 18.03.2017.
 */

public class SettingsContract {

    public interface SettingsPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(SettingsView view);

        void onDisplayNameClick(String displayName);

        void onDisplaySwitchStateChange(boolean isChecked);

        void onLedSwitchStateChange(boolean isChecked);

        void onVibrationSwitchStateChange(boolean isChecked);

        void onSoundSwitchStateChange(boolean isChecked);

        void onLogOutClick();
    }

    public interface SettingsView extends BaseFragmentContract.BaseFragmentView {

    }
}
