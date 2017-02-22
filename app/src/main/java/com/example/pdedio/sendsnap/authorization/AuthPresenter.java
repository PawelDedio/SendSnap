package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BasePresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

/**
 * Created by p.dedio on 22.02.17.
 */
@EBean
public class AuthPresenter extends BasePresenter implements AuthContract.AuthPresenter {

    protected AuthContract.AuthView authView;

    @Bean
    protected FragmentStackManager fragmentStackManager;


    @Override
    public void init(AuthContract.AuthView authView) {
        this.authView = authView;
    }

    @Override
    public void destroy() {

    }


    @Override
    public void onLogInClick() {
        this.authView.showFragment(null);
        this.authView.startLogInAnimation();
        this.authView.setLogInButtonEnabled(false);
    }

    @Override
    public void onSignUpClick() {
        this.authView.showFragment(null);
        this.authView.startSignUpAnimation();
        this.authView.setSignUpButtonEnabled(false);
    }

    @Override
    public boolean onBackKeyClick() {
        int fragmentsCount = this.fragmentStackManager.getBackStackCount();

        if(fragmentsCount >= 0) {
            this.fragmentStackManager.popBackStack();
            this.authView.restoreOriginalViewsState();
            this.authView.setLogInButtonEnabled(true);
            this.authView.setSignUpButtonEnabled(true);
        } else {
            this.authView.finish();
        }
        return false;
    }
}
