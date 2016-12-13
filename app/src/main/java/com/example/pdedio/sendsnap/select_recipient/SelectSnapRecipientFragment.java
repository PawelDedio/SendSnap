package com.example.pdedio.sendsnap.select_recipient;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by p.dedio on 28.11.16.
 */
@EFragment(R.layout.fragment_select_snap_recipient)
public class SelectSnapRecipientFragment extends BaseFragment implements SelectSnapRecipientPresenter.PresenterCallback {

    @Bean
    protected SelectSnapRecipientPresenter presenter;

    @FragmentArg
    protected File snapFile;

    @ViewById(R.id.btnSelectSnapRecipientBack)
    protected BaseImageButton btnBack;

    @ViewById(R.id.btnSelectSnapRecipientSearch)
    protected BaseImageButton btnSearch;



    // Lifecycle
    @AfterInject
    protected void afterInjectSelectSnapRecipientFragment() {
        this.presenter.init(this);
    }

    @AfterViews
    protected void afterViewsSelectSnapRecipientFragment() {
        this.presenter.afterViews();
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }

    @Override
    public BaseFragmentActivity getBaseFragmentActivity() {
        return (BaseFragmentActivity) this.getActivity();
    }

    @Override
    public BaseImageButton getBtnBack() {
        return this.btnBack;
    }

    @Override
    public BaseImageButton getBtnSearch() {
        return this.btnSearch;
    }
}
