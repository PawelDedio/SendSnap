package com.example.pdedio.sendsnap.ui.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.activities.MainPresenter;
import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity implements MainPresenter.PresenterCallback {

    @Bean
    protected MainPresenter presenter;

    @ViewById(R.id.vpMain)
    protected ViewPager vpMain;



    @AfterViews
    protected void afterViewsMainActivity() {
        this.presenter.afterViews();
    }


    @Override
    public FragmentManager getActivityFragmentManager() {
        return this.getSupportFragmentManager();
    }

    /// PresenterCallback methods
    @Override
    public void initViewPager(PagerAdapter adapter) {
        this.vpMain.setAdapter(adapter);
        this.vpMain.setCurrentItem(0);
    }
}
