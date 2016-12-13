package com.example.pdedio.sendsnap.common;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.pdedio.sendsnap.BaseContract;
import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.BasePresenter;
import com.example.pdedio.sendsnap.common.adapters.VpBaseFragmentAdapter;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;
import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.camera.CameraFragment;
import com.example.pdedio.sendsnap.ui.fragments.CameraFragment_;
import com.example.pdedio.sendsnap.common.views.BaseViewPager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.KeyUp;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity implements MainContract.MainView, StatusBarManager {

    @Bean
    protected MainContract.MainPresenter presenter;

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
    public BaseContract.BasePresenter getPresenter() {
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void showStatusBar() {
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Created by p.dedio on 31.08.16.
     */
    @EBean
    public static class MainPresenter extends BasePresenter {

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
                    this.presenterCallback.finish();
                    return;
                }
                this.fragmentStackManager.popBackStack();
                this.restoreFragmentInVp();
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

        private void stopFragmentInVp() {
            BaseFragment fragment = (BaseFragment) this.vpMainAdapter.getItem(this.presenterCallback.getMainViewPager().getCurrentItem());
            fragment.onPause();
            fragment.onStop();
            fragment.onVisibilityChanged(false);
        }

        private void restoreFragmentInVp() {
            BaseFragment fragment = (BaseFragment) this.vpMainAdapter.getItem(this.presenterCallback.getMainViewPager().getCurrentItem());
            fragment.onStart();
            fragment.onResume();
            fragment.onVisibilityChanged(true);
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
}
