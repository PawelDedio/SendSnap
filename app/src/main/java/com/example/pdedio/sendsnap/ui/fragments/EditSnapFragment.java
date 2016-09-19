package com.example.pdedio.sendsnap.ui.fragments;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.EditSnapPresenter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

/**
 * Created by pawel on 19.09.2016.
 */
@EFragment(R.layout.fragment_edit_snap)
public class EditSnapFragment extends BaseFragment implements EditSnapPresenter.PresenterCallback {

    @Bean
    protected EditSnapPresenter editSnapPresenter;



    // Lifecycle
    @AfterInject
    protected void afterInjectEditSnapFragment() {
        this.editSnapPresenter.init(this);
    }

    @AfterViews
    protected void afterViewsEditSnapFragment() {
        this.editSnapPresenter.afterViews();
    }
}
