package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ValidationHelper;

import static junit.framework.Assert.*;

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

    @Mock
    protected Context mockedContext;



    private LogInPresenter configureAndInitPresenter() {
        LogInPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private LogInPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        LogInPresenter presenter = new LogInPresenter();
        presenter.validationHelper = new ValidationHelper();

        return presenter;
    }


    //onBtnLogInClick()
    @Test
    public void shouldShowProgressDialogForCorrectInputs() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("someLogin", "somePassword", this.mockedContext);

        verify(mockedView).showProgressDialog();
    }

    @Test
    public void shouldShowErrorForEmptyData() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "", this.mockedContext);

        verify(mockedView).showBlankNameError();
        verify(mockedView).showBlankPasswordError();
    }

    @Test
    public void shouldShowErrorForBlankLogin() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "correctPassword", this.mockedContext);

        verify(mockedView).showBlankNameError();
    }

    @Test
    public void shouldShowErrorForBlankPassword() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("correctLogin", "", this.mockedContext);

        verify(mockedView).showBlankPasswordError();
    }

    @Test
    public void shouldClearErrors() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "", this.mockedContext);

        verify(mockedView).clearErrors();
    }

    @Test
    public void shouldHideKeyboard() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.onBtnLogInClick("", "", this.mockedContext);

        verify(this.mockedView).hideSoftKeyboard();
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.view);
    }
}
