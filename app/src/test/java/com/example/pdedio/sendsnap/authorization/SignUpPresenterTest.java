package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.models.BaseModel;
import com.example.pdedio.sendsnap.models.User;

import org.junit.Test;
import static org.mockito.Matchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.*;

/**
 * Created by pawel on 27.02.2017.
 */

public class SignUpPresenterTest {

    @Mock
    protected SignUpContract.SignUpView mockedView;

    @Mock
    protected Context mockedContext;

    @Mock
    protected User mockedUser;



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

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(false);
        this.mockedUser.nameError = "error";
        this.mockedUser.emailError = "error";
        this.mockedUser.passwordError = "error";
        this.mockedUser.passwordConfirmationError = "error";

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).setNameError(anyString());
        verify(this.mockedView).setEmailError(anyString());
        verify(this.mockedView).setPasswordError(anyString());
        verify(this.mockedView).setPasswordConfirmationError(anyString());
    }

    @Test
    public void shouldSaveUserAfterSuccessCreation() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedUser).save(any(Context.class), any(BaseModel.OperationCallback.class));
    }

    @Test
    public void shouldClearErrors() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).clearErrors();
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.view);
    }
}
