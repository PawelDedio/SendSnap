package com.example.pdedio.sendsnap.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseTextView;
import com.example.pdedio.sendsnap.databinding.FragmentSettingsBinding;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by pawel on 18.03.2017.
 */
@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends BaseFragment implements SettingsContract.SettingsView {

    @Bean(SettingsPresenter.class)
    protected SettingsContract.SettingsPresenter presenter;

    @ViewById(R.id.txvSettingsNameContent)
    protected BaseTextView txvName;

    @ViewById(R.id.txvSettingsDisplayNameContent)
    protected BaseTextView txvDisplayName;

    @ViewById(R.id.txvSettingsEmailContent)
    protected BaseTextView txvEmail;

    private FragmentSettingsBinding settingsBinding;



    //Lifecycle
    @AfterViews
    protected void afterViewsSettingsFragment() {
        this.presenter.init(this);
        this.configureViews();
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.settingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View rootView = this.settingsBinding.getRoot();
        return rootView;
    }



    //Events
    @Click({R.id.txvSettingsDisplayNameLabel, R.id.txvSettingsDisplayNameContent})
    protected void onDisplayNameClick() {
        this.presenter.onDisplayNameClick(this.txvDisplayName.getText().toString());
    }

    @CheckedChange(R.id.schSettingsDisplay)
    protected void onDisplaySwitchStateChange(boolean isChecked) {
        this.presenter.onDisplaySwitchStateChange(isChecked);
    }

    @CheckedChange(R.id.schSettingsLed)
    protected void onLedSwitchStateChange(boolean isChecked) {
        this.presenter.onLedSwitchStateChange(isChecked);
    }

    @CheckedChange(R.id.schSettingsVibration)
    protected void onVibrationSwitchStateChange(boolean isChecked) {
        this.presenter.onVibrationSwitchStateChange(isChecked);
    }

    @CheckedChange(R.id.schSettingsSound)
    protected void onSoundSwitchStateChange(boolean isChecked) {
        this.presenter.onSoundSwitchStateChange(isChecked);
    }

    @Click(R.id.btnSettingsLogOut)
    protected void onLogOutClick() {
        this.presenter.onLogOutClick();
    }


    //SettingsView methods



    //Private methods
    private void configureViews() {
        this.settingsBinding.setUser(this.presenter.getLoggedUser());
        this.settingsBinding.setPrefs(this.presenter.getSharedPreferenceManager());
    }
}
