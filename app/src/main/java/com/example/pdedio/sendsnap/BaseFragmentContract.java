package com.example.pdedio.sendsnap;

/**
 * Created by pawel on 13.12.2016.
 */

public class BaseFragmentContract {

    public interface BaseFragmentPresenter extends BaseContract.BasePresenter {

        void onDestroyView();

        void onVisibilityChanged(boolean isVisible);
    }

    public interface BaseFragmentView extends BaseContract.BaseView {

        void showStatusBar();

        void hideStatusBar();
    }
}
