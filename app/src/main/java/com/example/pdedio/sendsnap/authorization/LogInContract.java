package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.BaseFragmentContract;
import com.example.pdedio.sendsnap.models.User;

/**
 * Created by pawel on 22.02.2017.
 */

public class LogInContract {

    public interface LogInPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(LogInView logInView);

        void onBtnLogInClick(User user, Context context);
    }

    public interface LogInView extends BaseFragmentContract.BaseFragmentView {

        void showBlankNameError();

        void showBlankPasswordError();

        void showInvalidCredentialsError();

        void clearErrors();
    }
}
