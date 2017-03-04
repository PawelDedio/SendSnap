package com.example.pdedio.sendsnap.select_recipient;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by p.dedio on 28.11.16.
 */
@EFragment(R.layout.fragment_select_snap_recipient)
public class SelectSnapRecipientFragment extends BaseFragment implements SelectSnapRecipientContract.SelectSnapRecipientView {

    @Bean
    protected SelectSnapRecipientPresenter presenter;

    @FragmentArg
    protected File snapFile;

    @ViewById(R.id.btnSelectSnapRecipientBack)
    protected BaseImageButton btnBack;

    @ViewById(R.id.btnSelectSnapRecipientSearch)
    protected BaseImageButton btnSearch;



    // Lifecycle
    @AfterViews
    protected void afterViewsSelectSnapRecipientFragment() {
        this.presenter.init(this);
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        this.presenter = null;
        super.onDestroy();
    }


    //Events
    @Click(R.id.btnSelectSnapRecipientBack)
    public void onBackClick() {
        this.presenter.onBackClick();
    }


    //SelectSnapView methods
    @Override
    public void setNotificationColor(@ColorRes int colorId) {
        Window window = this.getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this.getActivity(), colorId));
    }
}
