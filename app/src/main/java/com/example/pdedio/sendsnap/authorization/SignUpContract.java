package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragmentContract;

/**
 * Created by pawel on 22.02.2017.
 */

public class SignUpContract {

    public interface SignUpPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(SignUpView signUpView);
    }

    public interface SignUpView extends BaseFragmentContract.BaseFragmentView {

    }
}
