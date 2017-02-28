package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.helpers.FragmentStackManager;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

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

        verify(this.mockedView).showFragment(any(LogInFragment.class));
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

        verify(this.mockedView).showFragment(any(SignUpFragment.class));
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


    //onBtnBackClick()
    @Test
    public void shouldRestoreOriginalViewsState() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnBackClick();

        verify(mockedView).restoreOriginalViewsState();
    }

    @Test
    public void shouldEnableButtons() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnBackClick();

        verify(mockedView).setLogInButtonEnabled(true);
        verify(mockedView).setSignUpButtonEnabled(true);
    }

    @Test
    public void shouldHideKeyboard() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnBackClick();

        verify(this.mockedView).hideSoftKeyboard();
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        AuthPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.authView);
    }
}
