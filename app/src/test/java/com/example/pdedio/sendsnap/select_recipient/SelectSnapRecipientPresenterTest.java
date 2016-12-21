package com.example.pdedio.sendsnap.select_recipient;

import android.os.Build;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.helpers.AndroidManager;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by p.dedio on 21.12.16.
 */

public class SelectSnapRecipientPresenterTest {


    @Mock
    private SelectSnapRecipientContract.SelectSnapRecipientView mockedView;

    @Mock
    private AndroidManager mockedAndroidManager;


    private SelectSnapRecipientPresenter configureAndInitPresenter() {
        SelectSnapRecipientPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private SelectSnapRecipientPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);

        SelectSnapRecipientPresenter presenter = new SelectSnapRecipientPresenter();
        presenter.androidManager = this.mockedAndroidManager;

        return presenter;
    }


    //init()
    @Test
    public void shouldShowStatusBar() {
        SelectSnapRecipientPresenter presenter = this.configurePresenter();

        presenter.init(this.mockedView);

        verify(mockedView).showStatusBar();
    }

    @Test
    public void shouldSetNotificationColorWhenSdkVersionIsLollipopOrHigher() {
        SelectSnapRecipientPresenter presenter = this.configurePresenter();

        when(mockedAndroidManager.getSdkCode()).thenReturn(Build.VERSION_CODES.LOLLIPOP);

        presenter.init(this.mockedView);

        verify(mockedView).setNotificationColor(R.color.action_bar_blue_color_dark);
    }

    @Test
    public void shouldNotSetNotificationColorWhenSdkVersionIsBelowLollipop() {
        SelectSnapRecipientPresenter presenter = this.configurePresenter();

        when(mockedAndroidManager.getSdkCode()).thenReturn(Build.VERSION_CODES.KITKAT);

        presenter.init(this.mockedView);

        verify(mockedView, never()).setNotificationColor(R.color.action_bar_blue_color_dark);
    }


    //onBackClick()
    @Test
    public void shouldPopFragment() {
        SelectSnapRecipientPresenter presenter = this.configureAndInitPresenter();

        presenter.onBackClick();

        verify(mockedView).popFragment();
    }
}
