package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.helpers.ValidationHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 22.02.2017.
 */
@EBean
public class LogInPresenter extends BaseFragmentPresenter implements LogInContract.LogInPresenter {


    protected LogInContract.LogInView view;

    @Bean
    protected ValidationHelper validationHelper;


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
    public void onBtnLogInClick(String name, String password) {
        this.view.clearErrors();

        if(this.validateAndShowErrors(name, password)) {
            this.view.showProgressDialog();
            //TODO: Request to server
        }
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
}
