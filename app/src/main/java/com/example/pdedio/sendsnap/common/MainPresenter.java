package com.example.pdedio.sendsnap.common;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.BasePresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.camera.CameraFragment;
import com.example.pdedio.sendsnap.camera.CameraFragment_;
import com.example.pdedio.sendsnap.common.adapters.VpBaseFragmentAdapter;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.dedio on 31.08.16.
 */
@EBean
public class MainPresenter extends BasePresenter implements MainContract.MainPresenter {

    protected MainContract.MainView mainView;

    protected VpBaseFragmentAdapter vpMainAdapter;

    @Bean
    protected FragmentStackManager fragmentStackManager;



    ///Lifecycle
    @Override
    public void init(MainContract.MainView mainView, FragmentManager fragmentManager) {
        this.mainView = mainView;
        this.setFragmentsToViewPager(fragmentManager);
    }

    @Override
    public void destroy() {
        this.mainView = null;
    }


    ///Public methods
    public void showFragment(BaseFragment fragment) {
        if(this.mainView.getMainViewPagerVisibility() == View.VISIBLE) {

            this.mainView.showFrameLayout();
            this.stopFragmentInVp();
        }

        this.fragmentStackManager.replaceFragmentWithAddingToBackStack(R.id.flMain, fragment);
    }

    public boolean onBackKeyClick() {
        this.popFragment();
        return true;
    }

    public void popFragment() {
        int fragmentsCount = this.fragmentStackManager.getBackStackCount();
        if(fragmentsCount > 1) {
            this.fragmentStackManager.popBackStack();
        } else {
            if(fragmentsCount == 0) {
                this.mainView.finish();
                return;
            }
            this.fragmentStackManager.popBackStack();
            this.restoreFragmentInVp();
            this.mainView.showViewPager();
        }
    }


    ///Private methods
    private void setFragmentsToViewPager(FragmentManager fragmentManager) {
        if(this.vpMainAdapter == null) {
            this.vpMainAdapter = new VpBaseFragmentAdapter(fragmentManager);
        }

        this.vpMainAdapter.setFragments(this.prepareFragments());
        this.mainView.initViewPager(this.vpMainAdapter);
    }

    private List<BaseFragment> prepareFragments() {
        ArrayList<BaseFragment> list = new ArrayList<>();

        CameraFragment fragment = CameraFragment_.builder().build();
        list.add(fragment);
        return list;
    }

    private void stopFragmentInVp() {
        BaseFragment fragment = (BaseFragment) this.vpMainAdapter.getItem(this.mainView.getCurrentItemInViewPager());
        fragment.onPause();
        fragment.onStop();
        fragment.onVisibilityChanged(false);
    }

    private void restoreFragmentInVp() {
        BaseFragment fragment = (BaseFragment) this.vpMainAdapter.getItem(this.mainView.getCurrentItemInViewPager());
        fragment.onStart();
        fragment.onResume();
        fragment.onVisibilityChanged(true);
    }
}
