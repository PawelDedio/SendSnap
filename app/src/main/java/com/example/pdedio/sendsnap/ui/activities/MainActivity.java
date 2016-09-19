package com.example.pdedio.sendsnap.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.logic.helpers.FragmentStackManager;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.presenters.activities.MainPresenter;
import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity implements MainPresenter.PresenterCallback {

    @Bean
    protected MainPresenter presenter;

    @ViewById(R.id.vpMain)
    protected ViewPager vpMain;

    @ViewById(R.id.flMain)
    protected FrameLayout flMain;

    @Bean
    protected FragmentStackManager fragmentStackManager;



    // Lifecycle
    @AfterViews
    protected void afterViewsMainActivity() {
        this.presenter.afterViews();
    }


    // PresenterCallback methods
    @Override
    public FragmentManager getActivityFragmentManager() {
        return this.getSupportFragmentManager();
    }

    @Override
    public void initViewPager(PagerAdapter adapter) {
        this.vpMain.setAdapter(adapter);
    }

    @Override
    public void showViewPager() {
        this.vpMain.setVisibility(View.VISIBLE);
        this.flMain.setVisibility(View.GONE);
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        if(this.vpMain.getVisibility() == View.VISIBLE) {
            this.showFrameLayout();
        }

        this.fragmentStackManager.replaceFragmentWithAddingToBackStack(R.id.flMain, fragment);
    }

    @Override
    public void popFragment() {

    }



    // BaseFragmentActivity methods
    @Override
    public BasePresenter getPresenter() {
        return this.presenter;
    }


    //Private methods
    private void showFrameLayout() {
        this.flMain.setVisibility(View.VISIBLE);
        this.vpMain.setVisibility(View.GONE);
    }
}
