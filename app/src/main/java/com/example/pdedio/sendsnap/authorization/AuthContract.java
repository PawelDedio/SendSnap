package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseContract;
import com.example.pdedio.sendsnap.BaseFragment;

/**
 * Created by p.dedio on 22.02.17.
 */

public class AuthContract {

    public interface AuthPresenter extends BaseContract.BasePresenter {
        void init(AuthView authView);

        void onLogInClick();

        void onSignUpClick();

        boolean onBackKeyClick();

        void showFragment(BaseFragment fragment);

        void popFragment();
    }

    public interface AuthView extends BaseContract.BaseView {
        void startLogInAnimation();

        void startSignUpAnimation();

        void setLogInButtonEnabled(boolean enabled);

        void setSignUpButtonEnabled(boolean enabled);

        void restoreOriginalViewsState();

        void finish();
    }
}
