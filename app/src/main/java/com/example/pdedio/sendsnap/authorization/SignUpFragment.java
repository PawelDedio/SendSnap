package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseTextInputLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by pawel on 22.02.2017.
 */
@EFragment(R.layout.fragment_sign_up)
public class SignUpFragment extends BaseFragment implements SignUpContract.SignUpView {


    @Bean(SignUpPresenter.class)
    protected SignUpContract.SignUpPresenter presenter;

    @ViewById(R.id.tilLogInName)
    protected BaseTextInputLayout tilName;

    @ViewById(R.id.tilSignUpEmail)
    protected BaseTextInputLayout tilEmail;

    @ViewById(R.id.tilSignUpPassword)
    protected BaseTextInputLayout tilPassword;

    @ViewById(R.id.tilSignUpPasswordConfirmation)
    protected BaseTextInputLayout tilPasswordConfirmation;



    //Lifecycle
    @AfterViews
    protected void afterViewsSignUpFragment() {
        this.presenter.init(this);
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }


    //Events
    @Click(R.id.btnSignUpSignUp)
    protected void onSignUpClick() {

    }


    //SignUpView methods
    @Override
    public void setNameError(String error) {

    }

    @Override
    public void setEmailError(String error) {

    }

    @Override
    public void setPasswordError(String error) {

    }

    @Override
    public void setPasswordConfirmationError(String error) {

    }
}
