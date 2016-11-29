package com.example.pdedio.sendsnap.ui.fragments;

import android.content.Context;
import android.view.TextureView;
import android.view.View;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.CameraPresenter;
import com.example.pdedio.sendsnap.ui.views.BaseButton;
import com.example.pdedio.sendsnap.ui.views.BaseImageButton;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by p.dedio on 31.08.16.
 */
@EFragment(R.layout.fragment_camera)
public class CameraFragment extends BaseFragment implements CameraPresenter.PresenterCallback {

    @Bean
    protected CameraPresenter cameraPresenter;

    @ViewById(R.id.pbCameraRecordProgress)
    protected DonutProgress pbRecordProgress;

    @ViewById(R.id.btnCameraRecord)
    protected BaseButton btnCameraRecord;

    @ViewById(R.id.tvCameraPreview)
    protected TextureView tvCameraPreview;

    @ViewById(R.id.btnCameraChangeCamera)
    protected BaseImageButton btnChangeCamera;

    @ViewById(R.id.btnCameraFlash)
    protected BaseImageButton btnCameraFlash;

    @ViewById(R.id.vCameraFrontFlash)
    protected View frontCameraFlash;



    //Lifecycle
    @AfterInject
    protected void afterInjectCameraFragment() {
        this.cameraPresenter.init(this);
    }

    @AfterViews
    protected void afterViewsCameraFragment() {
        this.cameraPresenter.afterViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(this.cameraPresenter != null) {
            this.cameraPresenter.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.cameraPresenter != null) {
            this.cameraPresenter.onResume();
        }
    }

    @Override
    public void onDestroy() {
        this.cameraPresenter.destroy();
        this.cameraPresenter = null;
        super.onDestroy();
    }

    @Override
    public void onVisibilityChanged(boolean isVisible) {
        this.cameraPresenter.onVisibilityChanged(isVisible);
    }


    //PresenterCallback methods
    @Override
    public DonutProgress getCameraProgressBar() {
        return this.pbRecordProgress;
    }

    @Override
    public BaseButton getCameraButton() {
        return this.btnCameraRecord;
    }

    @Override
    public TextureView getPreviewTextureView() {
        return this.tvCameraPreview;
    }

    @Override
    public BaseImageButton getChangeCameraButton() {
        return this.btnChangeCamera;
    }

    @Override
    public BaseImageButton getFlashButton() {
        return this.btnCameraFlash;
    }

    @Override
    public View getFrontFlashView() {
        return this.frontCameraFlash;
    }

    @Override
    public Context getActivityContext() {
        return this.getActivity();
    }
}
