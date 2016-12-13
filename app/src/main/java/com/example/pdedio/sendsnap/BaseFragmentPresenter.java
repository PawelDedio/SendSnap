package com.example.pdedio.sendsnap;

/**
 * Created by p.dedio on 07.11.16.
 */

public abstract class BaseFragmentPresenter extends BasePresenter implements BaseFragmentContract.BaseFragmentPresenter{

    public void openFragment(BaseFragmentActivity activity, BaseFragment fragment) {
        activity.showFragment(fragment);
    }

    public void popFragment(BaseFragmentActivity activity) {
        activity.popFragment();
    }

    public void onDestroyView() {}

    public void onVisibilityChanged(boolean isVisible) {}
}
