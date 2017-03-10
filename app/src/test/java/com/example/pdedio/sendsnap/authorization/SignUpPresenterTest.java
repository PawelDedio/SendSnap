package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import org.junit.Test;
import static org.mockito.Matchers.*;

import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

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

    @Mock
    protected BaseSnapModel.OperationError<User> mockedError;



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

    private Response<User> prepareErrorResponse(int code) {
        Response<User> response = Response.error(code, ResponseBody.create(MediaType.parse("json"), "{}"));

        return response;
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

        verify(this.mockedUser).save(any(Context.class), any(BaseSnapModel.OperationCallback.class));
    }

    @Test
    public void shouldClearErrors() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).clearErrors();
    }

    @Test
    public void shouldHideKeyboard() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).hideSoftKeyboard();
    }

    @Test
    public void shouldOpenNewActivityWhenCallbackReturnsSuccess() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onSuccess(mockedUser);

                return null;
            }
        }).when(this.mockedUser).save(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).openActivity(any(Class.class));
    }

    @Test
    public void shouldFinishCurrentActivityWhenCallbackReturnsSuccess() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onSuccess(mockedUser);

                return null;
            }
        }).when(this.mockedUser).save(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).finishCurrentActivity();
    }

    @Test
    public void shouldShowFormErrorsFor400Error() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onFailure(mockedError);

                return null;
            }
        }).when(this.mockedUser).save(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        this.mockedError.response = this.prepareErrorResponse(400);
        this.mockedUser.nameError = "Error";

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).setNameError("Error");
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.view);
    }
}
