package com.example.pdedio.sendsnap.ui.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.presenters.fragments.CameraPresenter;
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

    public static final String TAG = CameraFragment.class.getSimpleName();

    @Bean
    protected CameraPresenter cameraPresenter;

    @ViewById(R.id.pbCameraRecordProgress)
    protected DonutProgress pbRecordProgress;

    @ViewById(R.id.btnCameraRecord)
    protected Button btnCameraRecord;

    @ViewById(R.id.tvCameraPreview)
    protected TextureView tvCameraPreview;


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


    //PresenterCallback methods
    @Override
    public DonutProgress getCameraProgressBar() {
        return this.pbRecordProgress;
    }

    @Override
    public Button getCameraButton() {
        return this.btnCameraRecord;
    }

    @Override
    public TextureView getTextureView() {
        return this.tvCameraPreview;
    }

    @Override
    public Context getActivityContext() {
        return this.getActivity();
    }
}
