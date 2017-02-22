package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

/**
 * Created by pawel on 22.02.2017.
 */
@EFragment(R.layout.fragment_sign_up)
public class SignUpFragment extends BaseFragment implements SignUpContract.SignUpView {


    @Bean(SignUpPresenter.class)
    protected SignUpContract.SignUpPresenter presenter;
}
