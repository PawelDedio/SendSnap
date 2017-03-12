package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ErrorManager;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.example.pdedio.sendsnap.TestHelper.prepareErrorResponse;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    protected ErrorManager mockedErrorManager;



    private SignUpPresenter configureAndInitPresenter() {
        SignUpPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private SignUpPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        SignUpPresenter presenter = new SignUpPresenter();
        presenter.errorManager = this.mockedErrorManager;

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

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        this.mockedError.response = prepareErrorResponse(400);
        this.mockedError.model = this.mockedUser;
        this.mockedUser.nameError = "Error";

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).setNameError("Error");
    }

    @Test
    public void shouldShowProgressDialogBeforeRequest() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).showProgressDialog();
    }

    @Test
    public void shouldHideProgressDialogWhenCallbackReturnsSuccess() {
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

        verify(this.mockedView).hideProgressDialog();
    }

    @Test
    public void shouldHideProgressDialogWhenCallbackReturnsError() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onFailure(mockedError);

                return null;
            }
        }).when(this.mockedUser).save(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        this.mockedError.response = prepareErrorResponse(400);
        this.mockedError.model = this.mockedUser;

        presenter.onSignUpClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).hideProgressDialog();
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        SignUpPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.view);
    }
}
