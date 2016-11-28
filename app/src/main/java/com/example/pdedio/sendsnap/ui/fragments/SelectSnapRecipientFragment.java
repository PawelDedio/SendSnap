package com.example.pdedio.sendsnap.ui.fragments;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.SelectSnapRecipientPresenter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.io.File;

/**
 * Created by p.dedio on 28.11.16.
 */
@EFragment(R.layout.fragment_select_snap_recipient)
public class SelectSnapRecipientFragment extends BaseFragment implements SelectSnapRecipientPresenter.PresenterCallback {

    @Bean
    protected SelectSnapRecipientPresenter presenter;

    @FragmentArg
    protected File snapFile;



    // Lifecycle
    @AfterInject
    protected void afterInjectSelectSnapRecipientFragment() {
        this.presenter.init(this);
    }

    @AfterViews
    protected void afterViewsSelectSnapRecipientFragment() {
        this.presenter.afterViews();
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }
}
