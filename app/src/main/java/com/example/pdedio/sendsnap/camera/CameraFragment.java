package com.example.pdedio.sendsnap.camera;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

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

    private Subscription progressSubscription;

    private Subscription recordingSubscription;

    private boolean isRecording;


    private float oldBrightnessLevel;



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
                this.cameraPresenter.takePicture(this.getContext(), this.tvCameraPreview);
            } else {
                this.cameraPresenter.stopRecording();
            }
        }

        return false;
    }

    @Click(R.id.btnCameraChangeCamera)
    protected void onChangeCameraClick() {
        this.cameraPresenter.switchCamera(this.getContext(), this.tvCameraPreview);
    }


    //CameraView methods
    @Override
    public void startFrontFlash() {
        Activity activity = this.getActivity();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        this.oldBrightnessLevel = lp.screenBrightness;

        lp.screenBrightness = 1F;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
        this.frontCameraFlash.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopFrontFlash() {
        this.frontCameraFlash.setVisibility(View.GONE);
        Activity activity = this.getActivity();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

        lp.screenBrightness = oldBrightnessLevel;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
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

    private void startRecording() {
        this.isRecording = true;
        this.startProgress();
        cameraPresenter.startRecording(this.getContext(), this.tvCameraPreview);
    }

    private void startProgress() {
        progressSubscription = Observable.interval(10, TimeUnit.MILLISECONDS).map(new Func1<Long, Integer>() {
            @Override
            public Integer call(Long aLong) {
                return aLong.intValue();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {

                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        cameraPresenter.stopRecording();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(integer >= MAX_RECORD_TIME) {
                            progressSubscription.unsubscribe();
                            onCompleted();
                        } else {
                            pbRecordProgress.setProgress(integer);
                        }
                    }
                });
    }

    private void stopRecording() {
        this.progressSubscription.unsubscribe();
        this.pbRecordProgress.setProgress(0);
        this.cameraPresenter.stopRecording();
        this.btnCameraRecord.setPressed(false);
    }
}
