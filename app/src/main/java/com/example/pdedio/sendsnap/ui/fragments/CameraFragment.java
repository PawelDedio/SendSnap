package com.example.pdedio.sendsnap.ui.fragments;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.CameraPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

/**
 * Created by p.dedio on 31.08.16.
 */
@EFragment(R.layout.fragment_camera)
public class CameraFragment extends BaseFragment implements CameraPresenter.PresenterCallback {

    @Bean
    protected CameraPresenter cameraPresenter;


    @AfterViews
    protected void afterViewsCameraFragment() {
        this.cameraPresenter.afterViews();
    }
}
