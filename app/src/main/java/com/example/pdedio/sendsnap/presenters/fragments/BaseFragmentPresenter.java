package com.example.pdedio.sendsnap.presenters.fragments;

import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;

/**
 * Created by p.dedio on 07.11.16.
 */

public abstract class BaseFragmentPresenter extends BasePresenter{

    public void openFragment(BaseFragmentActivity activity, BaseFragment fragment) {
        activity.showFragment(fragment);
    }

    public void popFragment(BaseFragmentActivity activity) {
        activity.popFragment();
    }
}
