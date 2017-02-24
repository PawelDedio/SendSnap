package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragmentContract;

/**
 * Created by pawel on 22.02.2017.
 */

public class LogInContract {

    public interface LogInPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(LogInView logInView);

        void onBtnLogInClick(String login, String password);
    }

    public interface LogInView extends BaseFragmentContract.BaseFragmentView {

        void showBlankLoginError();

        void showBlankPasswordError();

        void showInvalidCredentialsError();

        void clearErrors();
    }
}
