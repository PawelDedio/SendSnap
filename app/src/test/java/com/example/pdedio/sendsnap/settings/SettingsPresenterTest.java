package com.example.pdedio.sendsnap.settings;

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


    private SettingsPresenter configureAndInitPresenter() {
        SettingsPresenter presenter = this.configurePresenter();
        presenter.init(this.mockedView);

        return presenter;
    }

    private SettingsPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        SettingsPresenter presenter = new SettingsPresenter();

        return presenter;
    }


    //init()
    @Test
    public void shouldShowStatusBar() {
        SettingsPresenter presenter = this.configurePresenter();

        presenter.init(this.mockedView);

        verify(this.mockedView).showStatusBar();
    }


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
