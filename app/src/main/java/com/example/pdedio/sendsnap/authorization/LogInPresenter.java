package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;

import org.androidannotations.annotations.EBean;

/**
 * Created by pawel on 22.02.2017.
 */
@EBean
public class LogInPresenter extends BaseFragmentPresenter implements LogInContract.LogInPresenter {


    protected LogInContract.LogInView view;


    //Lifecycle
    @Override
    public void init(LogInContract.LogInView logInView) {
        this.view = logInView;
    }

    @Override
    public void destroy() {

    }


    //LogInPresenter methods
    @Override
    public void onBtnLogInClick(String name, String password) {
        this.view.showBlankNameError();
        this.view.showBlankPasswordError();
    }
}
