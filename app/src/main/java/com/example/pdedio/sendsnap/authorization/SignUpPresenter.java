package com.example.pdedio.sendsnap.authorization;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.MainActivity;
import com.example.pdedio.sendsnap.common.MainActivity_;
import com.example.pdedio.sendsnap.helpers.ErrorManager;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 22.02.2017.
 */
@EBean
public class SignUpPresenter extends BaseFragmentPresenter implements SignUpContract.SignUpPresenter,
        BaseSnapModel.OperationCallback<User> {

    protected SignUpContract.SignUpView view;

    @Bean
    protected ErrorManager errorManager;



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
        this.view.clearErrors();
        this.view.hideSoftKeyboard();

        if(user.isValid(context)) {
            this.registerUser(user, context);
        } else {
            this.showErrors(user);
        }
    }


    //OperationCallback methods
    @Override
    public void onSuccess(User model) {
        this.view.hideProgressDialog();

        this.view.openActivity(MainActivity_.class);
        this.view.finishCurrentActivity();
    }

    @Override
    public void onFailure(BaseSnapModel.OperationError<User> error) {
        this.view.hideProgressDialog();

        if(!this.errorManager.serviceError(this.view, error.response)) {
            if(error.response.code() == 400) {
                if(error.model != null) {
                    this.showErrors(error.model);
                }
            }
        }
    }

    @Override
    public void onCanceled(int canceledReason, Throwable throwable) {
        this.view.hideProgressDialog();
        this.errorManager.serviceError(this.view, null);
    }


    //Private methods
    private void registerUser(User user, Context context) {
        this.view.showProgressDialog();

        user.save(context, this);
    }

    private void showErrors(User user) {
        this.view.setNameError(user.nameError);
        this.view.setEmailError(user.emailError);
        this.view.setPasswordError(user.passwordError);
        this.view.setPasswordConfirmationError(user.passwordConfirmationError);
    }
}
