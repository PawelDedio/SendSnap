package com.example.pdedio.sendsnap.presenters.activities;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.logic.helpers.FragmentStackManager;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.ui.adapters.VpBaseFragmentAdapter;
import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;
import com.example.pdedio.sendsnap.ui.fragments.CameraFragment;
import com.example.pdedio.sendsnap.ui.fragments.CameraFragment_;
import com.example.pdedio.sendsnap.ui.views.BaseViewPager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.dedio on 31.08.16.
 */
@EBean
public class MainPresenter extends BasePresenter {

    protected PresenterCallback presenterCallback;

    protected VpBaseFragmentAdapter vpMainAdapter;

    @Bean
    protected FragmentStackManager fragmentStackManager;



    ///Lifecycle
    @RootContext
    public void setContext(Context context) {
        if(context instanceof PresenterCallback) {
            this.presenterCallback = (PresenterCallback) context;
        }
    }
     @Override
    public void afterViews() {
        this.setFragmentsToViewPager();
    }

    @Override
    public void destroy() {
        this.presenterCallback = null;
    }


    ///Public methods
    public void showFragment(BaseFragment fragment) {
        if(this.presenterCallback.getMainViewPager().getVisibility() == View.VISIBLE) {
            this.presenterCallback.showFrameLayout();
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
                this.presenterCallback.finish();
                return;
            }
            this.fragmentStackManager.popBackStack();
            this.presenterCallback.showViewPager();
        }
    }


    ///Private methods
    private void setFragmentsToViewPager() {
        this.vpMainAdapter = new VpBaseFragmentAdapter(this.presenterCallback.getActivityFragmentManager());
        this.vpMainAdapter.setFragments(this.prepareFragments());
        this.presenterCallback.initViewPager(this.vpMainAdapter);
    }

    private List<BaseFragment> prepareFragments() {
        ArrayList<BaseFragment> list = new ArrayList<>();

        CameraFragment fragment = CameraFragment_.builder().build();
        list.add(fragment);
        return list;
    }


    public interface PresenterCallback {
        FragmentManager getActivityFragmentManager();

        void initViewPager(PagerAdapter adapter);

        void showViewPager();

        void showFrameLayout();

        BaseViewPager getMainViewPager();

        void finish();
    }
}
