package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
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



    //Lifecycle
    @Override
    public void init(AuthContract.AuthView authView) {
        this.authView = authView;
    }

    @Override
    public void destroy() {

    }


    //AuthPresenter methods
    @Override
    public void onLogInClick() {
        this.showLogInFragment();
    }

    @Override
    public void onSignUpClick() {
        this.showSignUpFragment();
    }

    @Override
    public boolean onBackKeyClick() {
        int fragmentsCount = this.fragmentStackManager.getBackStackCount();

        if(fragmentsCount > 0) {
            this.restoreOriginalViewsState();
        } else {
            this.authView.finish();
        }
        return true;
    }

    @Override
    public void showFragment(BaseFragment fragment) {
        this.fragmentStackManager.replaceFragmentWithAddingToBackStack(R.id.flAuth, fragment);
    }

    @Override
    public void popFragment() {
        this.fragmentStackManager.popBackStack();
    }

    @Override
    public void onBtnBackClick() {
        this.restoreOriginalViewsState();
    }


    //Private methods
    private void showLogInFragment() {
        LogInFragment fragment = LogInFragment_.builder().build();
        this.authView.showFragment(fragment);

        this.authView.setLogInButtonEnabled(false);
        this.authView.startLogInAnimation();
    }

    private void showSignUpFragment() {
        SignUpFragment fragment = SignUpFragment_.builder().build();
        this.authView.showFragment(fragment);

        this.authView.setSignUpButtonEnabled(false);
        this.authView.startSignUpAnimation();
    }

    private void restoreOriginalViewsState() {
        this.authView.restoreOriginalViewsState();
        this.authView.setLogInButtonEnabled(true);
        this.authView.setSignUpButtonEnabled(true);
    }
}
