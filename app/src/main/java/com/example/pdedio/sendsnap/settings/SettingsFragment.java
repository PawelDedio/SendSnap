package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseTextView;

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



    //Lifecycle
    @AfterViews
    protected void afterViewsSettingsFragment() {
        this.presenter.init(this);
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
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
}
