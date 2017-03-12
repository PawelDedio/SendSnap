package com.example.pdedio.sendsnap.authorization;

import android.content.Context;

import com.example.pdedio.sendsnap.common.MainActivity_;
import com.example.pdedio.sendsnap.helpers.ValidationHelper;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import static com.example.pdedio.sendsnap.TestHelper.*;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

/**
 * Created by pawel on 24.02.2017.
 */

public class LogInPresenterTest {

    @Mock
    protected LogInContract.LogInView mockedView;

    @Mock
    protected Context mockedContext;

    @Mock
    protected User mockedUser;

    @Mock
    protected BaseSnapModel.OperationError<User> mockedError;



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

    private void assignValuesToMockedUser(String name, String password) {
        this.mockedUser.name = name;
        this.mockedUser.password = password;
    }


    //onBtnLogInClick()
    @Test
    public void shouldShowProgressDialogForCorrectInputs() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("someLogin", "somePassword");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(mockedView).showProgressDialog();
    }

    @Test
    public void shouldShowErrorForEmptyData() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("", "");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(mockedView).showBlankNameError();
        verify(mockedView).showBlankPasswordError();
    }

    @Test
    public void shouldShowErrorForBlankLogin() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("", "correctPassword");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(mockedView).showBlankNameError();
    }

    @Test
    public void shouldShowErrorForBlankPassword() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("correctLogin", "");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(mockedView).showBlankPasswordError();
    }

    @Test
    public void shouldClearErrors() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("", "");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(mockedView).clearErrors();
    }

    @Test
    public void shouldHideKeyboard() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("", "");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).hideSoftKeyboard();
    }

    @Test
    public void shouldOpenNewActivityWhenCallbackReturnsSuccess() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("validname", "validpassword");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onSuccess(mockedUser);

                return null;
            }
        }).when(this.mockedUser).logIn(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).openActivity(MainActivity_.class);
    }

    @Test
    public void shouldFinishCurrentActivityWhenCallbackReturnsSuccess() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("validname", "validpassword");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onSuccess(mockedUser);

                return null;
            }
        }).when(this.mockedUser).logIn(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).finishCurrentActivity();
    }

    @Test
    public void shouldShowInvalidCredentialsErrorFor401Error() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("validname", "validpassword");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onFailure(mockedError);

                return null;
            }
        }).when(this.mockedUser).logIn(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        this.mockedError.response = prepareErrorResponse(401);
        this.mockedError.model = this.mockedUser;
        this.mockedUser.nameError = "Error";

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).showInvalidCredentialsError();
    }

    @Test
    public void shouldShowProgressDialogBeforeRequest() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("validname", "validpassword");

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).showProgressDialog();
    }

    @Test
    public void shouldHideProgressDialogWhenCallbackReturnsSuccess() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("validname", "validpassword");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onSuccess(mockedUser);

                return null;
            }
        }).when(this.mockedUser).logIn(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).hideProgressDialog();
    }

    @Test
    public void shouldHideProgressDialogFor401Error() {
        LogInPresenter presenter = this.configureAndInitPresenter();
        this.assignValuesToMockedUser("validname", "validpassword");

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onFailure(mockedError);

                return null;
            }
        }).when(this.mockedUser).logIn(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        this.mockedError.response = prepareErrorResponse(401);
        this.mockedError.model = this.mockedUser;
        this.mockedUser.nameError = "Error";

        presenter.onBtnLogInClick(this.mockedUser, this.mockedContext);

        verify(this.mockedView).hideProgressDialog();
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        LogInPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.view);
    }
}
