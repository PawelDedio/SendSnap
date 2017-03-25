package com.example.pdedio.sendsnap.settings;

import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.models.User;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

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

        presenter.destroy();

        assertNull(presenter.view);
    }

    @Test
    public void shouldHideStatusBar() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        verify(this.mockedView).hideStatusBar();
    }
}
