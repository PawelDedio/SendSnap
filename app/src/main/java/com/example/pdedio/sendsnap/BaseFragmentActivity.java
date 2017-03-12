package com.example.pdedio.sendsnap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.InputMethodService;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.pdedio.sendsnap.SendSnapApplication;
import com.example.pdedio.sendsnap.BasePresenter;
import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.common.MainActivity;
import com.example.pdedio.sendsnap.common.views.ProgressView;
import com.example.pdedio.sendsnap.common.views.ProgressView_;
import com.squareup.leakcanary.RefWatcher;

import org.androidannotations.annotations.UiThread;

/**
 * Created by pawel on 19.09.2016.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements BaseContract.BaseView {


    protected Dialog progressDialog;



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
    public void showProgressDialog() {
        if(this.progressDialog == null) {
            this.progressDialog = this.configureProgressDialog();
        }

        this.progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(this.progressDialog == null) {
            this.progressDialog = this.configureProgressDialog();
        }

        this.progressDialog.hide();
    }

    @Override
    public void hideSoftKeyboard() {
        if(getCurrentFocus() != null) {
            InputMethodManager manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void openActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
    }

    @Override
    public void finishCurrentActivity() {
        this.finish();
    }

    @Override
    public void showSnackbar(@StringRes int stringId, int length) {
        Snackbar.make(this.getCurrentFocus(), stringId, length).show();
    }


    //Private methods
    private Dialog configureProgressDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);

        if(dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        ProgressView progressView = ProgressView_.build(this);
        dialog.setContentView(progressView);

        return dialog;
    }
}
