package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.common.MainActivity_;
import com.example.pdedio.sendsnap.helpers.ErrorManager;
import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 22.02.2017.
 */
@EBean
public class LogInPresenter extends BaseFragmentPresenter implements LogInContract.LogInPresenter,
        BaseSnapModel.OperationCallback<User>{


    protected LogInContract.LogInView view;

    @Bean
    protected ValidationHelper validationHelper;

    @Bean
    protected ErrorManager errorManager;


    //Lifecycle
    @Override
    public void init(LogInContract.LogInView logInView) {
        this.view = logInView;
    }

    @Override
    public void destroy() {
        this.view = null;
    }


    //LogInPresenter methods
    @Override
    public void onBtnLogInClick(String name, String password, Context context) {
        this.view.clearErrors();
        this.view.hideSoftKeyboard();

        if(this.validateAndShowErrors(name, password)) {
            this.view.showProgressDialog();
            this.signInUser(name, password, context);
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

        if(error.response != null && error.response.code() == 401) {
            this.view.showInvalidCredentialsError();
            return;
        }

        this.errorManager.serviceError(this.view, error.response);
    }

    @Override
    public void onCanceled(int canceledReason, Throwable throwable) {
        this.view.hideProgressDialog();
        this.errorManager.serviceError(this.view, null);
    }


    //Private methods
    private boolean validateAndShowErrors(String name, String password) {
        boolean value = true;

        if(!this.validationHelper.isNotEmpty(name)) {
            this.view.showBlankNameError();
            value = false;
        }

        if(!this.validationHelper.isNotEmpty(password)) {
            this.view.showBlankPasswordError();
            value = false;
        }

        return value;
    }

    private void signInUser(String name, String password, Context context) {
        User user = new User();
        user.name = name;
        user.password = password;
        user.logIn(context, this);
    }
}
