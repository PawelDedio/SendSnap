package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.BaseFragmentContract;
import com.example.pdedio.sendsnap.models.User;

/**
 * Created by pawel on 22.02.2017.
 */

public class SignUpContract {

    public interface SignUpPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(SignUpView signUpView);

        void onSignUpClick(User user, Context context);
    }

    public interface SignUpView extends BaseFragmentContract.BaseFragmentView {

        void setNameError(String error);

        void setEmailError(String error);

        void setPasswordError(String error);

        void setPasswordConfirmationError(String error);
    }
}
