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
@EFragment(R.layout.fragment_sign_up)
public class SignUpFragment extends BaseFragment implements SignUpContract.SignUpView {


    @Bean(SignUpPresenter.class)
    protected SignUpContract.SignUpPresenter presenter;

    @ViewById(R.id.tilSignUpName)
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
        User user = new User(this.tilName.getEditText().getText().toString(), null,
                this.tilEmail.getEditText().getText().toString(),
                this.tilPassword.getEditText().getText().toString(),
                this.tilPasswordConfirmation.getEditText().getText().toString(), true);

        this.presenter.onSignUpClick(user, this.getContext());
    }


    //SignUpView methods
    @Override
    public void setNameError(String error) {
        this.tilName.setError(error);
    }

    @Override
    public void setEmailError(String error) {
        this.tilEmail.setError(error);
    }

    @Override
    public void setPasswordError(String error) {
        this.tilPassword.setError(error);
    }

    @Override
    public void setPasswordConfirmationError(String error) {
        this.tilPasswordConfirmation.setError(error);
    }
}
