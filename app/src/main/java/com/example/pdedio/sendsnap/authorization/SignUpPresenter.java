package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;

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
    public void onSignUpClick(String name, String email, String password, String passwordConfirmation, Context context) {

    }
}
