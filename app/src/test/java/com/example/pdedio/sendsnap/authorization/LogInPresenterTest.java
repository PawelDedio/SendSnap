package com.example.pdedio.sendsnap.authorization;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Created by pawel on 24.02.2017.
 */

public class LogInPresenterTest {

    @Mock
    protected LogInContract.LogInView mockedView;



    private LogInPresenter configureAndInitPresenter() {
        LogInPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private LogInPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        LogInPresenter presenter = new LogInPresenter();

        return presenter;
    }


    //onBtnLogInClick()
    @Test
    public void shouldShowProgressDialogForCorrectInputs() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("someLogin", "somePassword");

        verify(mockedView).showProgressDialog();
    }

    @Test
    public void shouldShowErrorForEmptyData() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "");

        verify(mockedView).showBlankLoginError();
        verify(mockedView).showBlankPasswordError();
    }

    @Test
    public void shouldShowErrorForBlankLogin() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "correctPassword");

        verify(mockedView).showBlankLoginError();
    }

    @Test
    public void shouldShowErrorForBlankPassword() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("correctLogin", "");

        verify(mockedView).showBlankPasswordError();
    }

    @Test
    public void shouldClearErrors() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "");

        verify(mockedView).clearErrors();
    }
}
