package com.example.pdedio.sendsnap.helpers;

import com.example.pdedio.sendsnap.BaseContract;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.authorization.AuthActivity_;
import com.example.pdedio.sendsnap.models.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by pawel on 11.03.2017.
 */

public class ErrorManagerTest {


    private ErrorManager errorManager;

    @Mock
    public BaseContract.BaseView mockedView;


    @Before
    public void prepareErrorManager() {
        MockitoAnnotations.initMocks(this);
        this.errorManager = new ErrorManager();
    }


    private Response<User> prepareErrorResponse(int code) {
        Response<User> response = Response.error(code, ResponseBody.create(MediaType.parse("json"), "{}"));

        return response;
    }


    //service error
    @Test
    public void shouldReturnTrueAndShowNetworkErrorForEmptyResponse() {
        assertTrue(this.errorManager.serviceError(this.mockedView, null));

        verify(this.mockedView).showSnackbar(eq(R.string.error_network), anyInt());
    }

    @Test
    public void shouldReturnTrueAndShowServerErrorFor500Error() {
        assertTrue(this.errorManager.serviceError(this.mockedView, this.prepareErrorResponse(500)));

        verify(this.mockedView).showSnackbar(eq(R.string.error_server), anyInt());
    }

    @Test
    public void shouldOpenAuthActivityFor401Error() {
        assertTrue(this.errorManager.serviceError(this.mockedView, this.prepareErrorResponse(401)));

        verify(this.mockedView).openActivity(AuthActivity_.class);
    }

    @Test
    public void shouldFinishCurrentActivityFor401Error() {
        assertTrue(this.errorManager.serviceError(this.mockedView, this.prepareErrorResponse(401)));

        verify(this.mockedView).finishCurrentActivity();
    }

    @Test
    public void shouldShowToastFor401Error() {
        assertTrue(this.errorManager.serviceError(this.mockedView, this.prepareErrorResponse(401)));

        verify(this.mockedView).showToast(eq(R.string.error_session_expired), anyInt());
    }
}
