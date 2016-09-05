package com.example.pdedio.sendsnap.presenters.fragments;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.pdedio.sendsnap.logic.helpers.CameraHelper;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.androidannotations.annotations.EBean;

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
@EBean
public class CameraPresenter extends BasePresenter {

    public static final String TAG = CameraPresenter.class.getSimpleName();

    private PresenterCallback presenterCallback;

    private Subscription progressSubscription;

    private Subscription recordingSubscription;

    private static final int TIME_TO_START_RECORDING = 1000;

    private static final int MAX_RECORD_TIME = 1000;


    ///Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }

    @Override
    public void afterViews() {
        this.configureViews();
    }

    @Override
    public void destroy() {

    }


    //Private methods
    private void configureViews() {
        DonutProgress progress = this.presenterCallback.getCameraProgressBar();
        progress.setRotation(270);
        progress.setMax(MAX_RECORD_TIME);


        this.presenterCallback.getCameraButton().setOnTouchListener(new View.OnTouchListener() {
            long downTime = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    this.downTime = System.currentTimeMillis();
                    startCameraButtonEvent();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    long upTime = System.currentTimeMillis();

                    if(upTime - this.downTime < TIME_TO_START_RECORDING) {
                        recordingSubscription.unsubscribe();
                        takePicture();
                    } else {
                        stopRecording();
                    }
                }

                return false;
            }
        });
    }

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

    private void takePicture() {

    }

    private void startRecording() {
        this.startProgress();
    }

    private void stopRecording() {
        this.stopProgress();
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
                        stopRecording();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        presenterCallback.getCameraProgressBar().setProgress(integer);
                    }
                });
    }

    private void stopProgress() {
        this.progressSubscription.unsubscribe();
        this.presenterCallback.getCameraProgressBar().setProgress(0);
    }


    public interface PresenterCallback {
        DonutProgress getCameraProgressBar();

        Button getCameraButton();
    }
}
