package com.example.pdedio.sendsnap.ui.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.presenters.activities.MainPresenter;
import com.example.pdedio.sendsnap.ui.communication.StatusBarManager;
import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;
import com.example.pdedio.sendsnap.ui.views.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.KeyUp;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity implements MainPresenter.PresenterCallback, StatusBarManager {

    @Bean
    protected MainPresenter presenter;

    @ViewById(R.id.vpMain)
    protected BaseViewPager vpMain;

    @ViewById(R.id.flMain)
    protected FrameLayout flMain;



    // Lifecycle
    @AfterViews
    protected void afterViewsMainActivity() {
        this.presenter.afterViews();
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }


    // Events
    @KeyUp(KeyEvent.KEYCODE_BACK)
    protected boolean onBackClick() {
        return this.presenter.onBackKeyClick();
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
    public void showFrameLayout() {
        this.flMain.setVisibility(View.VISIBLE);
        this.vpMain.setVisibility(View.GONE);
    }

    @Override
    public BaseViewPager getMainViewPager() {
        return this.vpMain;
    }


    // BaseFragmentActivity methods
    @Override
    public BasePresenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        this.presenter.showFragment(fragment);
    }

    @Override
    public void popFragment() {
        this.presenter.popFragment();
    }


    // Status Bar manager methods
    @Override
    public void hideStatusBar() {

    }

    @Override
    public void showStatusBar() {

    }
}
