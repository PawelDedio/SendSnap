package com.example.pdedio.sendsnap.ui.activities;

import android.support.v4.app.FragmentActivity;

import com.example.pdedio.sendsnap.SendSnapApplication;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.ui.fragments.BaseFragment;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by pawel on 19.09.2016.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    public abstract BasePresenter getPresenter();

    public abstract void showFragment(BaseFragment fragment);

    public abstract void popFragment();

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = SendSnapApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
