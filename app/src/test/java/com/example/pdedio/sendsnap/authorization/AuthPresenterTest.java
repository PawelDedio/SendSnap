package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.common.MainActivity_;
import com.example.pdedio.sendsnap.helpers.DateHelper;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;
import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.models.User;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created by p.dedio on 22.02.17.
 */

public class AuthPresenterTest {

    @Mock
    protected AuthContract.AuthView mockedView;

    @Mock
    protected FragmentStackManager mockedFragmentStackManager;

    @Mock
    protected User mockedUser;

    @Mock
    protected DateHelper mockedDateHelper;

    @Mock
    protected SessionManager mockedSessionManager;



    private AuthPresenter configureAndInitPresenter() {
        AuthPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private AuthPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        AuthPresenter presenter = new AuthPresenter();
        presenter.fragmentStackManager = this.mockedFragmentStackManager;
        presenter.dateHelper = this.mockedDateHelper;
        presenter.sessionManager = this.mockedSessionManager;

        return presenter;
    }


    //init()
    @Test
    public void shouldOpenNewActivityWhenSavedUserTokenIsNotNull() {
        AuthPresenter presenter = this.configurePresenter();

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);
        this.mockedUser.authToken = "fdssdfsfdsf";

        presenter.init(this.mockedView);

        verify(this.mockedView).openActivity(MainActivity_.class);
    }

    @Test
    public void shouldFinishCurrentActivityWhenSavedUserTokenIsNotNull() {
        AuthPresenter presenter = this.configurePresenter();
        presenter.authView = this.mockedView;

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);
        this.mockedUser.authToken = "fdssdfsfdsf";

        presenter.init(this.mockedView);

        verify(this.mockedView).finishCurrentActivity();
    }

    @Test
    public void shouldNotFinishAndOpenNewActivityWhenSavedUserIsNull() {
        AuthPresenter presenter = this.configurePresenter();
        presenter.authView = this.mockedView;

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(null);

        presenter.init(this.mockedView);

        verify(this.mockedView, never()).openActivity(MainActivity_.class);
        verify(this.mockedView, never()).finishCurrentActivity();
    }

    @Test
    public void shouldNotFinishAndOpenNewActivityWhenUserTokenIsNull() {
        AuthPresenter presenter = this.configurePresenter();
        presenter.authView = this.mockedView;

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);
        this.mockedUser.authToken = null;

        presenter.init(this.mockedView);

        verify(this.mockedView, never()).openActivity(MainActivity_.class);
        verify(this.mockedView, never()).finishCurrentActivity();
    }


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
