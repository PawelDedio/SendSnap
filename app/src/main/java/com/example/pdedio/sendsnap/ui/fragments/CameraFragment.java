package com.example.pdedio.sendsnap.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.CameraPresenter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by p.dedio on 31.08.16.
 */
@EFragment
public class CameraFragment extends BaseFragment implements CameraPresenter.PresenterCallback {

    @Bean
    protected CameraPresenter cameraPresenter;

    @ViewById(R.id.txvCamera)
    protected TextView txvCamera;


    //Lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.e("CameraFragment", "onCreateView view: " + view);

        return view;
    }

    @AfterInject
    protected void afterInjectCameraFragment() {
        this.cameraPresenter.init(this);
    }

    @AfterViews
    protected void afterViewsCameraFragment() {
        this.cameraPresenter.afterViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("CameraFragment", "onAttach");
    }


    //PresenterCallback methods
    @Override
    public void changeText(String text) {
        this.txvCamera.setText(text);
    }
}
