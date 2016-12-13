package com.example.pdedio.sendsnap;

/**
 * Created by pawel on 13.12.2016.
 */

public class BaseContract {


    public interface BasePresenter {

        void afterViews();

        void destroy();

        void onPause();

        void onResume();
    }

    public interface BaseView {

        void showFragment(BaseFragment fragment);

        void popFragment();
    }
}
