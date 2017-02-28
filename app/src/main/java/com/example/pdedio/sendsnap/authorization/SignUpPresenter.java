package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.models.User;

import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 22.02.2017.
 */
@EBean
public class SignUpPresenter extends BaseFragmentPresenter implements SignUpContract.SignUpPresenter {

    protected SignUpContract.SignUpView view;



    //Lifecycle
    @Override
    public void init(SignUpContract.SignUpView signUpView) {
        this.view = signUpView;
    }

    @Override
    public void destroy() {
        this.view = null;
    }


    //SignUpPresenter methods
    @Override
    public void onSignUpClick(User user, Context context) {

        if(user.isValid(context)) {
            this.registerUser(user, context);
        }

        this.showErrors(user);
    }


    //Private methods
    private void registerUser(User user, Context context) {
        user.save(context, null);
        //TODO: implementation
    }

    private void showErrors(User user) {
        this.view.setNameError(user.nameError);
        this.view.setEmailError(user.emailError);
        this.view.setPasswordError(user.passwordError);
        this.view.setPasswordConfirmationError(user.passwordConfirmationError);
    }
}
