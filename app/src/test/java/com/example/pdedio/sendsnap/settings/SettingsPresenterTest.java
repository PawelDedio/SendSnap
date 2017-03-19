package com.example.pdedio.sendsnap.settings;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
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


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        SettingsPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        assertNull(presenter.view);
    }
}
