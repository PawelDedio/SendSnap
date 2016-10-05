package com.example.pdedio.sendsnap.presenters.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.logic.helpers.CameraHelper;
import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.activities.MainActivity;
import com.example.pdedio.sendsnap.ui.fragments.EditSnapFragment;
import com.example.pdedio.sendsnap.ui.fragments.EditSnapFragment_;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static android.Manifest.permission;

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

    private float oldBrightnessLevel;

    private boolean isCameraConfigured;

    private boolean isRecording;


    //Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }

    @Override
    public void afterViews() {
        this.checkPermissions();
    }

    @Override
    public void destroy() {
        if(this.cameraHelper != null) {
            this.cameraHelper.release();
        }
    }

    @Override
    public void onResume() {
        if(this.cameraHelper != null && this.presenterCallback != null && !this.isCameraConfigured) {
            this.cameraHelper.init(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
        }
    }

    @Override
    public void onPause() {
        if(this.cameraHelper != null) {
            this.cameraHelper.release();
            this.isCameraConfigured = false;
            this.isRecording = false;
        }
    }


    //Private methods
    private void checkPermissions() {
        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted()) {
                    prepareLogic();
                } else {
                    new AlertDialog.Builder(presenterCallback.getActivityContext())
                            .setTitle(R.string.camera_permissions_denied_title)
                            .setMessage(R.string.camera_permissions_denied_message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int buttonId) {
                                    presenterCallback.getBaseFragmentActivity().finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, permission.RECORD_AUDIO, permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE);
    }

    private void prepareLogic() {
        this.configureViews();
        this.cameraHelper = CameraHelper.Factory.create();
        this.cameraHelper.init(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
        this.isCameraConfigured = true;
    }

    private void configureViews() {
        DonutProgress progress = this.presenterCallback.getCameraProgressBar();
        progress.setRotation(270);
        progress.setMax(MAX_RECORD_TIME);


        this.presenterCallback.getCameraButton().setOnTouchListener(new View.OnTouchListener() {
            long downTime = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    startFrontFlash();
                }

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
                    cameraHelper.setFlashLight(false);
                } else {
                    button.setImageResource(R.drawable.flash_enabled);
                    cameraHelper.setFlashLight(true);
                }

                isFlashEnabled = !isFlashEnabled;
            }
        });

        this.presenterCallback.getPreviewTextureView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cameraHelper.enableAutoFocus();
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
        this.cameraHelper.takePicture(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView(), new CameraHelper.PhotoCallback() {
            @Override
            public void onPhotoTaken(File photo) {
                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    stopFrontFlash();
                }
                openFragment(photo, Consts.SnapType.PHOTO);
            }

            @Override
            public void onError(Exception e) {
                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    stopFrontFlash();
                }
            }
        });
    }

    private void startRecording() {
        this.isRecording = true;
        this.startProgress();
        this.cameraHelper.startRecording(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
    }

    private void stopRecording() {
        if(!this.isRecording) {
            return;
        }
        this.stopProgress();
        File videoFile = this.cameraHelper.stopRecording();
        openFragment(videoFile, Consts.SnapType.VIDEO);
        isRecording = false;
        this.presenterCallback.getCameraButton().setPressed(false);
    }

    private void switchCamera() {
        this.cameraHelper.switchCamera(this.presenterCallback.getActivityContext(), this.presenterCallback.getPreviewTextureView());
    }

    private void startFrontFlash() {
        Activity activity = this.presenterCallback.getBaseFragmentActivity();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        this.oldBrightnessLevel = lp.screenBrightness;

        lp.screenBrightness = 1F;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
        this.presenterCallback.getFrontFlashView().setVisibility(View.VISIBLE);
    }

    private void stopFrontFlash() {
        this.presenterCallback.getFrontFlashView().setVisibility(View.GONE);
        Activity activity = this.presenterCallback.getBaseFragmentActivity();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

        lp.screenBrightness = oldBrightnessLevel;
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
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
                        if(integer >= MAX_RECORD_TIME) {
                            progressSubscription.unsubscribe();
                            onCompleted();
                        } else {
                            presenterCallback.getCameraProgressBar().setProgress(integer);
                        }
                    }
                });
    }

    private void stopProgress() {
        this.progressSubscription.unsubscribe();
        this.presenterCallback.getCameraProgressBar().setProgress(0);
    }

    private void openFragment(File file, Consts.SnapType snapType) {
        BaseFragmentActivity activity = this.presenterCallback.getBaseFragmentActivity();

        if(activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;

            EditSnapFragment fragment = EditSnapFragment_.builder().snapFile(file).snapType(snapType).build();
            mainActivity.showFragment(fragment);
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

        View getFrontFlashView();
    }
}
