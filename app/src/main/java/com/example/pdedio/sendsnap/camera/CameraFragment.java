package com.example.pdedio.sendsnap.camera;

import android.content.Context;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by p.dedio on 31.08.16.
 */
@EFragment(R.layout.fragment_camera)
public class CameraFragment extends BaseFragment implements CameraContract.CameraView {

    @Bean(CameraPresenter.class)
    protected CameraContract.CameraPresenter cameraPresenter;

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

    private static final int TIME_TO_START_RECORDING = 1000;

    private static final int MAX_RECORD_TIME = 1000;

    private long btnCameraDownTime = 0;



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


    //Events
    @Touch(R.id.btnCameraRecord)
    protected boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            this.btnCameraDownTime = System.currentTimeMillis();
            startCameraButtonEvent();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            long upTime = System.currentTimeMillis();

            if(upTime - btnCameraDownTime < TIME_TO_START_RECORDING) {
                recordingSubscription.unsubscribe();
                this.cameraPresenter.takePicture(this.getActivityContext(), this.tvCameraPreview);
            } else {
                this.cameraPresenter.stopRecording();
            }
        }

        return false;
    }


    //Private methods
    private void startCameraButtonEvent() {
        this.recordingSubscription = Observable.timer(TIME_TO_START_RECORDING, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        startRecording();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }
                });
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
