package com.example.pdedio.sendsnap.common;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.common.adapters.VpBaseFragmentAdapter;
import com.example.pdedio.sendsnap.helpers.FragmentStackManager;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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

        Mockito.verify(mockedMainAdapter).setFragments(Mockito.anyList());
    }

    @Test
    public void shouldInitViewPager() {
        MainPresenter presenter = configureAndInitPresenter();

        Mockito.verify(mockedView).initViewPager(Mockito.any(VpBaseFragmentAdapter.class));
    }


    //showFragment()
    @Test
    public void shouldOnlyReplaceFragmentOnStackWhenViewPagerIsNotVisible() {
        MainPresenter presenter = configureAndInitPresenter();

        Mockito.when(mockedView.getMainViewPagerVisibility()).thenReturn(View.GONE);

        presenter.showFragment(Mockito.mock(BaseFragment.class));

        Mockito.verify(mockedView, Mockito.never()).showFrameLayout();
        Mockito.verify(mockedStackManager).replaceFragmentWithAddingToBackStack(Mockito.anyInt(), Mockito.any(BaseFragment.class));
    }

    @Test
    public void shouldShowFrameLayoutWhenViewPagerIsVisible() {
        MainPresenter presenter = configureAndInitPresenter();

        Mockito.when(mockedView.getMainViewPagerVisibility()).thenReturn(View.VISIBLE);
        Mockito.when(mockedMainAdapter.getItem(Mockito.anyInt())).thenReturn(Mockito.mock(BaseFragment.class));

        presenter.showFragment(Mockito.mock(BaseFragment.class));

        Mockito.verify(mockedView).showFrameLayout();
    }


    //popFragment()
    @Test
    public void shouldPopBackStackWhenThereIsMoreThanOneFragments() {
        MainPresenter presenter = configureAndInitPresenter();

        Mockito.when(mockedStackManager.getBackStackCount()).thenReturn(2);

        presenter.popFragment();

        Mockito.verify(mockedStackManager).popBackStack();
    }

    @Test
    public void shouldFinishViewWhenThereIsZeroFragments() {
        MainPresenter presenter = configureAndInitPresenter();

        Mockito.when(mockedStackManager.getBackStackCount()).thenReturn(0);

        presenter.popFragment();

        Mockito.verify(mockedView).finish();
    }

    @Test
    public void shouldPopBackStackAndShowViewPagerWhenIsOneFragment() {
        MainPresenter presenter = configureAndInitPresenter();

        Mockito.when(mockedStackManager.getBackStackCount()).thenReturn(1);
        Mockito.when(mockedMainAdapter.getItem(Mockito.anyInt())).thenReturn(Mockito.mock(BaseFragment.class));

        presenter.popFragment();

        Mockito.verify(mockedStackManager).popBackStack();
        Mockito.verify(mockedView).showViewPager();
    }
}
