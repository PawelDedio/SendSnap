package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

/**
 * Created by pawel on 18.03.2017.
 */
@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends BaseFragment implements SettingsContract.SettingsView {

    @Bean(SettingsPresenter.class)
    protected SettingsContract.SettingsPresenter presenter;



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
}
