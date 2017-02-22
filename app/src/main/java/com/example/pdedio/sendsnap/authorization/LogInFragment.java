package com.example.pdedio.sendsnap.authorization;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

/**
 * Created by pawel on 22.02.2017.
 */
@EFragment(R.layout.fragment_log_in)
public class LogInFragment extends BaseFragment implements LogInContract.LogInView {

    @Bean(LogInPresenter.class)
    protected LogInContract.LogInPresenter presenter;
}
