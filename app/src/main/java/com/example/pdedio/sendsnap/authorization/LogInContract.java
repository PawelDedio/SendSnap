package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.BaseFragmentContract;

/**
 * Created by pawel on 22.02.2017.
 */

public class LogInContract {

    public interface LogInPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(LogInView logInView);

        void onBtnLogInClick(String name, String password, Context context);
    }

    public interface LogInView extends BaseFragmentContract.BaseFragmentView {

        void showBlankNameError();

        void showBlankPasswordError();

        void showInvalidCredentialsError();

        void clearErrors();
    }
}
