package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseTextInputLayout;
import com.example.pdedio.sendsnap.models.User;

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

    @ViewById(R.id.tilLogInName)
    protected BaseTextInputLayout tilName;

    @ViewById(R.id.tilLogInPassword)
    protected BaseTextInputLayout tilPassword;



    //Lifecycle
    @AfterViews
    protected void afterViewsLogInFragment() {
        this.presenter.init(this);
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }


    //Events
    @Click(R.id.btnLogInLogIn)
    protected void onLogInClick() {
        String name = this.tilName.getEditText().getText().toString();
        String password = this.tilPassword.getEditText().getText().toString();

        User user = new User();
        user.name = name;
        user.password = password;
        this.presenter.onBtnLogInClick(user, this.getContext());
    }


    //LogInView methods
    @Override
    public void showBlankNameError() {
        this.tilName.setError(R.string.error_field_blank, this.getString(R.string.user_name));
    }

    @Override
    public void showBlankPasswordError() {
        this.tilPassword.setError(R.string.error_field_blank, this.getString(R.string.user_password));
    }

    @Override
    public void showInvalidCredentialsError() {
        this.tilName.setError(R.string.error_invalid_login_credentials);
    }

    @Override
    public void clearErrors() {
        this.tilName.setError(null);
        this.tilName.setErrorEnabled(false);
        this.tilPassword.setError(null);
        this.tilPassword.setErrorEnabled(false);
    }
}
