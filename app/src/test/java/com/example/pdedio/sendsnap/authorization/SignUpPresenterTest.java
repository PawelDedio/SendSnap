package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/**
 * Created by pawel on 27.02.2017.
 */

public class SignUpPresenterTest {

    @Mock
    protected SignUpContract.SignUpView mockedView;

    @Mock
    protected Context mockedContext;



    private SignUpPresenter configureAndInitPresenter() {
        SignUpPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private SignUpPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        SignUpPresenter presenter = new SignUpPresenter();

        return presenter;
    }


    //onSignUpClick()
    @Test
    public void shouldSetErrorsAfterButtonClick() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick("name", "test@test.com", null, null, this.mockedContext);

        verify(this.mockedView).setNameError(anyString());
        verify(this.mockedView).setEmailError(anyString());
        verify(this.mockedView).setPasswordError(anyString());
        verify(this.mockedView).setPasswordConfirmationError(anyString());
    }
}
