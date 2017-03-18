package com.example.pdedio.sendsnap;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.pdedio.sendsnap.SendSnapApplication;
import com.example.pdedio.sendsnap.BasePresenter;
import com.example.pdedio.sendsnap.BaseFragment;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by pawel on 19.09.2016.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements BaseContract.BaseView {

    public abstract BaseContract.BasePresenter getPresenter();

    public abstract void showFragment(BaseFragment fragment);

    public abstract void popFragment();

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = SendSnapApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public void showToast(@StringRes int stringId, int length) {
        Toast.makeText(this, stringId, length).show();
    }

    @Override
    public void setNotificationColor(@ColorRes int colorId) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, colorId));
    }
}
