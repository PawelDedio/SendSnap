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
@EFragment(R.layout.fragment_log_in)
public class LogInFragment extends BaseFragment implements LogInContract.LogInView {

    @Bean(LogInPresenter.class)
    protected LogInContract.LogInPresenter presenter;

    @ViewById(R.id.tilLogInLogin)
    protected BaseTextInputLayout tilLogin;

    @ViewById(R.id.tilLogInPassword)
    protected BaseTextInputLayout tilPassword;



    //Lifecycle
    @AfterViews
    protected void afterViewsLogInFragment() {
        this.presenter.init(this);
    }


    //Events
    @Click(R.id.btnLogInLogIn)
    protected void onLogInClick() {
        String login = this.tilLogin.getEditText().getText().toString();
        String password = this.tilPassword.getEditText().getText().toString();
        this.presenter.onBtnLogInClick(login, password);
    }


    //LogInView methods
    @Override
    public void showBlankLoginError() {
        this.tilLogin.setError(R.string.log_in_login_blank_error);
    }

    @Override
    public void showBlankPasswordError() {
        this.tilPassword.setError(R.string.log_in_password_blank_error);
    }

    @Override
    public void showInvalidCredentialsError() {

    }

    @Override
    public void clearErrors() {
        this.tilLogin.setError(null);
        this.tilPassword.setError(null);
    }

    @Override
    public void showProgressDialog() {

    }
}
