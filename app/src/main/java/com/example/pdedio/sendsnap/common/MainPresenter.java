package com.example.pdedio.sendsnap.common;

import android.content.Context;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.BasePresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.camera.CameraFragment_;
import com.example.pdedio.sendsnap.common.adapters.VpBaseFragmentAdapter;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;
import com.example.pdedio.sendsnap.camera.CameraFragment;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

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
    @RootContext
    public void setContext(Context context) {
        if(context instanceof MainContract.MainView) {
            this.mainView = (MainContract.MainView) context;
        }
    }
     @Override
    public void afterViews() {
        this.setFragmentsToViewPager();
    }

    @Override
    public void destroy() {
        this.mainView = null;
    }


    ///Public methods
    public void showFragment(BaseFragment fragment) {
        if(this.mainView.getMainViewPager().getVisibility() == View.VISIBLE) {

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
    private void setFragmentsToViewPager() {
        this.vpMainAdapter = new VpBaseFragmentAdapter(this.mainView.getActivityFragmentManager());
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
        BaseFragment fragment = (BaseFragment) this.vpMainAdapter.getItem(this.mainView.getMainViewPager().getCurrentItem());
        fragment.onPause();
        fragment.onStop();
        fragment.onVisibilityChanged(false);
    }

    private void restoreFragmentInVp() {
        BaseFragment fragment = (BaseFragment) this.vpMainAdapter.getItem(this.mainView.getMainViewPager().getCurrentItem());
        fragment.onStart();
        fragment.onResume();
        fragment.onVisibilityChanged(true);
    }
}
