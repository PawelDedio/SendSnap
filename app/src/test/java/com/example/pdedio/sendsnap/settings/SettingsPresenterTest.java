package com.example.pdedio.sendsnap.settings;

import android.content.Context;

import com.example.pdedio.sendsnap.helpers.ErrorManager;
import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.models.BaseSnapModel;
import com.example.pdedio.sendsnap.models.User;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import retrofit2.Response;

import static com.example.pdedio.sendsnap.TestHelper.prepareErrorResponse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by pawel on 19.03.2017.
 */

public class SettingsPresenterTest {

    @Mock
    private SettingsContract.SettingsView mockedView;

    @Mock
    private SessionManager mockedSessionManager;

    @Mock
    private User mockedUser;

    @Mock
    private SharedPreferenceManager mockedPrefsManager;

    @Mock
    protected BaseSnapModel.OperationError<User> mockedError;

    @Mock
    protected ErrorManager mockedErrorManager;

    @Mock
    protected Context mockedContext;


    private SettingsPresenter configureAndInitPresenter() {
        SettingsPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private SettingsPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        SettingsPresenter presenter = new SettingsPresenter();
        presenter.sessionManager = this.mockedSessionManager;
        presenter.sharedPreferenceManager = this.mockedPrefsManager;
        presenter.errorManager = this.mockedErrorManager;

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);

        return presenter;
    }


    //init()
    @Test
    public void shouldShowStatusBar() {
        SettingsPresenter presenter = this.configurePresenter();

        presenter.init(this.mockedView);

        verify(this.mockedView).showStatusBar();
    }


    //getLoggedUser()
    @Test
    public void shouldGetUserFromSessionManager() {
        SettingsPresenter presenter = this.configurePresenter();

        presenter.getLoggedUser();

        verify(this.mockedSessionManager).getLoggedUser();
    }


    //onDisplayNameClick()
    @Test
    public void shouldShowTextInputDialog() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        this.mockedUser.displayName = "displayName";

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);

        presenter.onDisplayNameClick("displayName");

        verify(this.mockedView).showTextInputDialog(anyInt(), anyInt(), anyInt(), anyInt(), anyString(), any(TextInputDialog.ResultListener.class));
    }

    @Test
    public void shouldUpdateUserDisplayNameAfterSuccess() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        this.mockedUser.displayName = "displayName";

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                TextInputDialog.ResultListener listener = (TextInputDialog.ResultListener) invocation.getArguments()[5];
                listener.onValueSet("newDisplayName");

                return null;
            }
        }).when(this.mockedView).showTextInputDialog(anyInt(), anyInt(), anyInt(), anyInt(), anyString(), any(TextInputDialog.ResultListener.class));

        when(this.mockedSessionManager.getLoggedUser()).thenReturn(this.mockedUser);

        presenter.onDisplayNameClick("displayName");

        verify(this.mockedUser).setDisplayName("newDisplayName");
    }


    //onDisplaySwitchStateChange()
    @Test
    public void shouldSaveDisplayValueToSharedPrefs() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        boolean randomValue = Math.random() < 0.5;

        presenter.onDisplaySwitchStateChange(randomValue);

        verify(this.mockedPrefsManager).setNotificationDisplay(randomValue);
    }


    //onLedSwitchStateChange()
    @Test
    public void shouldSaveLedValueToSharedPrefs() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        boolean randomValue = Math.random() < 0.5;

        presenter.onLedSwitchStateChange(randomValue);

        verify(this.mockedPrefsManager).setNotificationLed(randomValue);
    }


    //onVibrationSwitchStateChange()
    @Test
    public void shouldSaveVibrationValueToSharedPrefs() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        boolean randomValue = Math.random() < 0.5;

        presenter.onVibrationSwitchStateChange(randomValue);

        verify(this.mockedPrefsManager).setNotificationVibration(randomValue);
    }


    //onSoundSwitchStateChange()
    @Test
    public void shouldSaveSoundValueToSharedPrefs() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        boolean randomValue = Math.random() < 0.5;

        presenter.onSoundSwitchStateChange(randomValue);

        verify(this.mockedPrefsManager).setNotificationSound(randomValue);
    }


    //onLogOutClick()


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        when(this.mockedView.getApplicationContext()).thenReturn(this.mockedContext);

        presenter.destroy();

        assertNull(presenter.view);
    }

    @Test
    public void shouldHideStatusBar() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        when(this.mockedView.getApplicationContext()).thenReturn(this.mockedContext);

        presenter.destroy();

        verify(this.mockedView).hideStatusBar();
    }

    @Test
    public void shouldUpdateUser() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        when(this.mockedView.getApplicationContext()).thenReturn(this.mockedContext);

        presenter.destroy();

        verify(this.mockedUser).update(any(Context.class), any(BaseSnapModel.OperationCallback.class));
    }

    @Test
    public void shouldDelegateUpdateErrors() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        when(this.mockedView.getApplicationContext()).thenReturn(this.mockedContext);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                BaseSnapModel.OperationCallback<User> callback = (BaseSnapModel.OperationCallback) invocation.getArguments()[1];
                callback.onFailure(mockedError);

                return null;
            }
        }).when(this.mockedUser).update(any(Context.class), Matchers.<BaseSnapModel.OperationCallback<User>>any());

        when(this.mockedUser.isValid(any(Context.class))).thenReturn(true);

        this.mockedError.response = prepareErrorResponse(400);
        this.mockedError.model = this.mockedUser;
        this.mockedUser.nameError = "Error";

        presenter.destroy();

        verify(this.mockedErrorManager).serviceError(any(SettingsContract.SettingsView.class), any(Response.class));
    }
}
