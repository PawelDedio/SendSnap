package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragmentContract;

/**
 * Created by pawel on 18.03.2017.
 */

public class SettingsContract {

    public interface SettingsPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(SettingsView view);
    }

    public interface SettingsView extends BaseFragmentContract.BaseFragmentView {

    }
}
