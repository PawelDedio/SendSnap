package com.example.pdedio.sendsnap.common;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import com.example.pdedio.sendsnap.BaseContract;
import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.camera.CameraContract;
import com.example.pdedio.sendsnap.common.views.BaseViewPager;

/**
 * Created by pawel on 13.12.2016.
 */

public class MainContract {

    public interface MainPresenter extends BaseContract.BasePresenter {

        void init(MainView cameraView, FragmentManager fragmentManager);

        void showFragment(BaseFragment fragment);

        boolean onBackKeyClick();

        void popFragment();
    }

    public interface MainView extends BaseContract.BaseView {

        void initViewPager(PagerAdapter adapter);

        void showViewPager();

        void showFrameLayout();

        int getMainViewPagerVisibility();

        int getCurrentItemInViewPager();

        void finish();
    }
}
