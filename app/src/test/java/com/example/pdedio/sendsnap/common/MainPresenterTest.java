package com.example.pdedio.sendsnap.common;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.common.adapters.VpBaseFragmentAdapter;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;

import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/**
 * Created by pawel on 16.12.2016.
 */

public class MainPresenterTest {
    
    @Mock
    protected MainContract.MainView mockedView;

    @Mock
    protected FragmentStackManager mockedStackManager;

    @Mock
    protected VpBaseFragmentAdapter mockedMainAdapter;

    @Mock
    protected FragmentManager mockedFragmentManager;



    private MainPresenter configureAndInitPresenter() {
        MainPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedFragmentManager);

        return presenter;
    }

    private MainPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        MainPresenter presenter = new MainPresenter();
        presenter.vpMainAdapter = mockedMainAdapter;
        presenter.fragmentStackManager = mockedStackManager;

        return presenter;
    }


    //init()
    @Test
    public void shouldSetFragmentsToAdapter() {
        MainPresenter presenter = configureAndInitPresenter();

        verify(mockedMainAdapter).setFragments(anyList());
    }

    @Test
    public void shouldInitViewPager() {
        MainPresenter presenter = configureAndInitPresenter();

        verify(mockedView).initViewPager(any(VpBaseFragmentAdapter.class));
    }


    //showFragment()
    @Test
    public void shouldOnlyReplaceFragmentOnStackWhenViewPagerIsNotVisible() {
        MainPresenter presenter = configureAndInitPresenter();

        when(mockedView.getMainViewPagerVisibility()).thenReturn(View.GONE);

        presenter.showFragment(mock(BaseFragment.class));

        verify(mockedView, never()).showFrameLayout();
        verify(mockedStackManager).replaceFragmentWithAddingToBackStack(anyInt(), any(BaseFragment.class));
    }

    @Test
    public void shouldShowFrameLayoutWhenViewPagerIsVisible() {
        MainPresenter presenter = configureAndInitPresenter();

        when(mockedView.getMainViewPagerVisibility()).thenReturn(View.VISIBLE);
        when(mockedMainAdapter.getItem(anyInt())).thenReturn(mock(BaseFragment.class));

        presenter.showFragment(mock(BaseFragment.class));

        verify(mockedView).showFrameLayout();
    }


    //popFragment()
    @Test
    public void shouldPopBackStackWhenThereIsMoreThanOneFragments() {
        MainPresenter presenter = configureAndInitPresenter();

        when(mockedStackManager.getBackStackCount()).thenReturn(2);

        presenter.popFragment();

        verify(mockedStackManager).popBackStack();
    }

    @Test
    public void shouldFinishViewWhenThereIsZeroFragments() {
        MainPresenter presenter = configureAndInitPresenter();

        when(mockedStackManager.getBackStackCount()).thenReturn(0);

        presenter.popFragment();

        verify(mockedView).finish();
    }

    @Test
    public void shouldPopBackStackAndShowViewPagerWhenIsOneFragment() {
        MainPresenter presenter = configureAndInitPresenter();

        when(mockedStackManager.getBackStackCount()).thenReturn(1);
        when(mockedMainAdapter.getItem(anyInt())).thenReturn(mock(BaseFragment.class));

        presenter.popFragment();

        verify(mockedStackManager).popBackStack();
        verify(mockedView).showViewPager();
    }
}
