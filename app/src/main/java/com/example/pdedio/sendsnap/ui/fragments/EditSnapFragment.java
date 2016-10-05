package com.example.pdedio.sendsnap.ui.fragments;

import android.view.TextureView;
import android.widget.ImageView;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.presenters.fragments.EditSnapPresenter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EFragment(R.layout.fragment_edit_snap)
public class EditSnapFragment extends BaseFragment implements EditSnapPresenter.PresenterCallback {

    @Bean
    protected EditSnapPresenter editSnapPresenter;

    @FragmentArg
    protected Consts.SnapType snapType;

    @FragmentArg
    protected File snapFile;

    @ViewById(R.id.ivEditSnapPhoto)
    protected ImageView ivPhoto;

    @ViewById(R.id.tvEditSnapVideo)
    protected TextureView tvVideo;



    // Lifecycle
    @AfterInject
    protected void afterInjectEditSnapFragment() {
        this.editSnapPresenter.init(this);
    }

    @AfterViews
    protected void afterViewsEditSnapFragment() {
        this.editSnapPresenter.afterViews();
    }

    @Override
    public void onDestroy() {
        this.editSnapPresenter.destroy();
        super.onDestroy();
    }


    //PresenterCallback methods
    @Override
    public TextureView getPreviewTextureView() {
        return this.tvVideo;
    }

    @Override
    public ImageView getPreviewImageView() {
        return this.ivPhoto;
    }

    @Override
    public File getSnapFile() {
        return this.snapFile;
    }

    @Override
    public Consts.SnapType getSnapType() {
        return this.snapType;
    }
}
