package com.example.pdedio.sendsnap.ui.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.example.pdedio.sendsnap.SendSnapApplication;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by p.dedio on 31.08.16.
 */
public class BaseFragment extends Fragment {


    public BaseFragmentActivity getBaseFragmentActivity() {
        Activity activity = this.getActivity();
        if(activity instanceof BaseFragmentActivity) {
            return (BaseFragmentActivity) activity;
        }

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = SendSnapApplication.getRefWatcher(this.getContext());
        refWatcher.watch(this);
    }
}
