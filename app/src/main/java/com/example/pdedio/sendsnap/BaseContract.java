package com.example.pdedio.sendsnap;

import android.content.Intent;
import android.support.annotation.StringRes;

/**
 * Created by pawel on 13.12.2016.
 */

public class BaseContract {


    public interface BasePresenter {

        void destroy();

        void onPause();

        void onResume();
    }

    public interface BaseView {

        void showFragment(BaseFragment fragment);

        void popFragment();

        void showToast(@StringRes int stringId, int length);

        void showProgressDialog();

        void hideSoftKeyboard();

        void openActivity(Class activity);

        void finishCurrentActivity();
    }
}
