package com.example.pdedio.sendsnap;

/**
 * Created by pawel on 13.12.2016.
 */

public class BaseFragmentContract {

    public interface BaseFragmentPresenter extends BaseContract.BasePresenter {

        void openFragment(BaseFragmentActivity activity, BaseFragment fragment);

        void popFragment(BaseFragmentActivity activity);

        void onDestroyView();

        void onVisibilityChanged(boolean isVisible);
    }

    public interface BaseFragmentView {

        void showStatusBar();

        void hideStatusBar();
    }
}
