package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;

import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/**
 * Created by p.dedio on 22.02.17.
 */

public class AuthPresenterTest {

    @Mock
    protected AuthContract.AuthView mockedView;

    @Mock
    protected FragmentStackManager mockedFragmentStackManager;



    private AuthPresenter configureAndInitPresenter() {
        AuthPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private AuthPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        AuthPresenter presenter = new AuthPresenter();
        presenter.fragmentStackManager = mockedFragmentStackManager;

        return presenter;
    }



    //init()


    //onLogInClick()
    @Test
    public void shouldStartLogInAnimation() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onLogInClick();

        verify(this.mockedView).startLogInAnimation();
    }

    @Test
    public void shouldShowLogInFragment() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onLogInClick();

        verify(this.mockedView).showFragment(any(BaseFragment.class));
    }

    @Test
    public void shouldSetLogInButtonDisabled() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onLogInClick();

        verify(mockedView).setLogInButtonEnabled(false);
    }


    //onSignUpClick()
    @Test
    public void shouldStartSignUpAnimation() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick();

        verify(this.mockedView).startSignUpAnimation();
    }

    @Test
    public void shouldShowSignUpFragment() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick();

        verify(this.mockedView).showFragment(any(BaseFragment.class));
    }

    @Test
    public void shouldSetSignUpButtonDisabled() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick();

        verify(this.mockedView).setSignUpButtonEnabled(false);
    }


    //onBackKeyClick()
    @Test
    public void shouldFinishActivityWhenThereIs0Fragments() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        when(mockedFragmentStackManager.getBackStackCount()).thenReturn(0);

        presenter.onBackKeyClick();

        verify(mockedView).finish();
    }

    @Test
    public void shouldRestoreOriginalViewStateWhenIsMoreThan0Fragments() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        when(mockedFragmentStackManager.getBackStackCount()).thenReturn(1);

        presenter.onBackKeyClick();

        verify(mockedView).restoreOriginalViewsState();
    }

    @Test
    public void shouldEnableButtonsWhenIsMoreThan0Fragments() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        when(mockedFragmentStackManager.getBackStackCount()).thenReturn(1);

        presenter.onBackKeyClick();

        verify(mockedView).setLogInButtonEnabled(true);
        verify(mockedView).setSignUpButtonEnabled(true);
    }
}
