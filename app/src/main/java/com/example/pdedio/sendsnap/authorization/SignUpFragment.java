package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseTextInputLayout;

import org.androidannotations.annotations.Bean;
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
}
