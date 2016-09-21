package com.example.pdedio.sendsnap.presenters.fragments;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.logic.helpers.CameraHelper;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.presenters.activities.MainPresenter;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.activities.MainActivity;
import com.example.pdedio.sendsnap.ui.fragments.EditSnapFragment;
import com.example.pdedio.sendsnap.ui.fragments.EditSnapFragment_;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.IOException;
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

    private PresenterCallback presenterCallback;

    private Subscription progressSubscription;

    private Subscription recordingSubscription;

    private static final int TIME_TO_START_RECORDING = 1000;

    private static final int MAX_RECORD_TIME = 1000;

    private CameraHelper cameraHelper;

    private MediaPlayer mediaPlayer;

    private boolean isFlashEnabled;


    //Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }

    @Override
    public void afterViews() {
        this.configureViews();
        this.cameraHelper = CameraHelper.Factory.build();
    }

    @Override
    public void destroy() {
        if(this.cameraHelper != null) {
            this.cameraHelper.release();
        }
    }

    @Override
    public void onResume() {
        if(this.cameraHelper != null && this.presenterCallback != null) {
            this.cameraHelper.init(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
        }
    }

    @Override
    public void onPause() {
        if(this.cameraHelper != null) {
            this.cameraHelper.release();
        }
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

        this.presenterCallback.getChangeCameraButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        this.presenterCallback.getFlashButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                if(isFlashEnabled) {
                    button.setImageResource(R.drawable.flash_disabled);
                } else {
                    button.setImageResource(R.drawable.flash_enabled);
                }

                isFlashEnabled = !isFlashEnabled;
                cameraHelper.setFlashLight(isFlashEnabled);
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
        this.cameraHelper.takePicture(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
    }

    private void startRecording() {
        this.startProgress();
        this.cameraHelper.startRecording(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
    }

    private void stopRecording() {
        this.stopProgress();
        File videoFile = this.cameraHelper.stopRecording();
        showVideo(videoFile);
    }

    private void switchCamera() {
        this.cameraHelper.switchCamera(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
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

    private void showVideo(final File videoFile) {
        TextureView textureView = this.presenterCallback.getPlayingTextureView();
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Log.e("listener", "onSurfaceTextureAvailable");
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                Log.e("listener", "onSurfaceTextureSizeChanged");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                Log.e("listener", "onSurfaceTextureDestroyed");
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                Log.e("listener", "onSurfaceTextureUpdated");
                Surface surface = new Surface(surfaceTexture);
            }
        });

        Surface surface = new Surface(this.presenterCallback.getPlayingTextureView().getSurfaceTexture());
        startMediaPlayer(videoFile, surface);
        /*BaseFragmentActivity activity = this.presenterCallback.getBaseFragmentActivity();

        if(activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;

            EditSnapFragment fragment = EditSnapFragment_.builder().build();
            mainActivity.showFragment(fragment);
        }*/
    }

    private void startMediaPlayer(File videoFile, Surface surface) {
        try {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setDataSource(videoFile.getAbsolutePath());
            this.mediaPlayer.setSurface(surface);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setLooping(true);
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.presenterCallback.getPreviewTextureView().setVisibility(View.GONE);
            this.mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface PresenterCallback {
        DonutProgress getCameraProgressBar();

        Button getCameraButton();

        Context getActivityContext();

        TextureView getPreviewTextureView();

        TextureView getPlayingTextureView();

        ImageButton getChangeCameraButton();

        ImageButton getFlashButton();

        BaseFragmentActivity getBaseFragmentActivity();
    }
}
