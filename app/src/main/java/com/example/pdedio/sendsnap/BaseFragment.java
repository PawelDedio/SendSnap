package com.example.pdedio.sendsnap;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.pdedio.sendsnap.common.BackKeyListener;
import com.example.pdedio.sendsnap.common.StatusBarManager;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by p.dedio on 31.08.16.
 */
public class BaseFragment extends Fragment implements BaseFragmentContract.BaseFragmentView {


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


    public void showStatusBar() {
        if(this.getActivity() instanceof StatusBarManager) {
            StatusBarManager manager = (StatusBarManager) this.getActivity();
            manager.showStatusBar();
        }
    }

    public void hideStatusBar() {
        if(this.getActivity() instanceof StatusBarManager) {
            StatusBarManager manager = (StatusBarManager) this.getActivity();
            manager.hideStatusBar();
        }
    }

    public void onVisibilityChanged(boolean isVisible) {}

    @Override
    public void showFragment(BaseFragment fragment) {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView activity = (BaseContract.BaseView) this.getActivity();
            activity.showFragment(fragment);
        }
    }

    @Override
    public void popFragment() {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView activity = (BaseContract.BaseView) this.getActivity();
            activity.popFragment();
        }
    }

    @Override
    public void showToast(@StringRes int stringId, int length) {
        Toast.makeText(this.getContext(), stringId, length).show();
    }

    @Override
    public void showProgressDialog() {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView activity = (BaseContract.BaseView) this.getActivity();
            activity.showProgressDialog();
        }
    }

    @Override
    public void hideProgressDialog() {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView activity = (BaseContract.BaseView) this.getActivity();
            activity.hideProgressDialog();
        }
    }

    @Override
    public void hideSoftKeyboard() {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView activity = (BaseContract.BaseView) this.getActivity();
            activity.hideSoftKeyboard();
        }
    }

    @Override
    public void openActivity(Class activity) {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView baseView = (BaseContract.BaseView) this.getActivity();
            baseView.openActivity(activity);
        }
    }

    @Override
    public void finishCurrentActivity() {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView baseView = (BaseContract.BaseView) this.getActivity();
            baseView.finishCurrentActivity();
        }
    }

    @Override
    public void showSnackbar(@StringRes int stringId, int length) {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView baseView = (BaseContract.BaseView) this.getActivity();
            baseView.showSnackbar(stringId, length);
        }
    }

    @Override
    public void setOnBackKeyListener(BackKeyListener backKeyListener) {
        if(this.getActivity() instanceof BaseContract.BaseView) {
            BaseContract.BaseView baseView = (BaseContract.BaseView) this.getActivity();
            baseView.setOnBackKeyListener(backKeyListener);
        }
    }
}
