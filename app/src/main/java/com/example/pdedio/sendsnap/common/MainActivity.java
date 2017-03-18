package com.example.pdedio.sendsnap.common;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity implements MainContract.MainView, StatusBarManager {

    @Bean
    protected MainPresenter presenter;

    @ViewById(R.id.vpMain)
    protected BaseViewPager vpMain;

    @ViewById(R.id.flMain)
    protected FrameLayout flMain;



    // Lifecycle
    @AfterViews
    protected void afterViewsMainActivity() {
        this.presenter.init(this, this.getSupportFragmentManager());
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }


    // Events
    @Override
    protected boolean onBackKeyClick() {
        if(this.backKeyListener == null || !this.backKeyListener.onBackKeyClick()) {
            return this.presenter.onBackKeyClick();
        }

        return true;
    }


    // PresenterCallback methods
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
    public int getMainViewPagerVisibility() {
        return this.vpMain.getVisibility();
    }

    @Override
    public int getCurrentItemInViewPager() {
        return this.vpMain.getCurrentItem();
    }


    // BaseFragmentActivity methods
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void showStatusBar() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
