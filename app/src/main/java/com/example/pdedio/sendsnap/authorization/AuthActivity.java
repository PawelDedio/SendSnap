package com.example.pdedio.sendsnap.authorization;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.KeyEvent;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.KeyUp;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_auth)
public class AuthActivity extends BaseFragmentActivity implements AuthContract.AuthView {

    @Bean
    protected AuthPresenter presenter;

    @ViewById(R.id.clAuthMainLayout)
    protected ConstraintLayout mainLayout;

    @ViewById(R.id.btnAuthLogIn)
    protected BaseButton btnLogIn;

    @ViewById(R.id.btnAuthSignUp)
    protected BaseButton btnSignUp;

    private ConstraintSet baseConstraintSet;



    // Lifecycle
    @AfterViews
    protected void afterViewsAuthActivity() {
        this.baseConstraintSet = new ConstraintSet();
        this.baseConstraintSet.clone(this.mainLayout);
        this.presenter.init(this);
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }


    //Events
    @Click(R.id.btnAuthLogIn)
    protected void onLogInClick() {
        this.presenter.onLogInClick();
    }

    @Click(R.id.btnAuthSignUp)
    protected void onSignUpClick() {
        this.presenter.onSignUpClick();
    }

    @KeyUp(KeyEvent.KEYCODE_BACK)
    protected boolean onBackClick() {
        return this.presenter.onBackKeyClick();
    }


    //AuthView methods
    @Override
    public void showFragment(BaseFragment fragment) {
        this.presenter.showFragment(fragment);
    }

    @Override
    public void popFragment() {
        this.presenter.popFragment();
    }

    @Override
    public void startLogInAnimation() {
        TransitionManager.beginDelayedTransition(this.mainLayout);
        this.prepareLogInConstraintSet().applyTo(this.mainLayout);
    }

    @Override
    public void startSignUpAnimation() {
        TransitionManager.beginDelayedTransition(this.mainLayout);
        this.prepareSignUpConstraintSet().applyTo(this.mainLayout);
    }

    @Override
    public void setLogInButtonEnabled(boolean enabled) {
        this.btnLogIn.setEnabled(enabled);
    }

    @Override
    public void setSignUpButtonEnabled(boolean enabled) {
        this.btnLogIn.setEnabled(enabled);
    }

    @Override
    public void restoreOriginalViewsState() {
        AutoTransition transition = new AutoTransition();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                popFragment();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.go(new Scene(this.mainLayout), transition);
        this.baseConstraintSet.applyTo(this.mainLayout);
    }


    //Private methods
    private ConstraintSet prepareLogInConstraintSet() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.baseConstraintSet);

        constraintSet.connect(R.id.btnAuthLogIn, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.clear(R.id.btnAuthLogIn, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.btnAuthSignUp, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintSet.clear(R.id.btnAuthSignUp, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.flAuth, ConstraintSet.TOP, R.id.btnAuthLogIn, ConstraintSet.BOTTOM, 0);

        return constraintSet;
    }

    private ConstraintSet prepareSignUpConstraintSet() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.baseConstraintSet);

        constraintSet.connect(R.id.btnAuthLogIn, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintSet.clear(R.id.btnAuthLogIn, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.btnAuthSignUp, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.clear(R.id.btnAuthSignUp, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.flAuth, ConstraintSet.TOP, R.id.btnAuthSignUp, ConstraintSet.BOTTOM, 0);

        return constraintSet;
    }
}
