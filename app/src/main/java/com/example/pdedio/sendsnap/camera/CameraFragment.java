package com.example.pdedio.sendsnap.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.example.pdedio.sendsnap.common.views.BaseTextView;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.transitionseverywhere.TransitionManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelOffsetRes;

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

    @ViewById(R.id.clCameraMainLayout)
    protected ConstraintLayout mainLayout;

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

    @ViewById(R.id.txvCameraUserName)
    protected BaseTextView txvUserName;

    @DimensionPixelOffsetRes(R.dimen.camera_ll_menu_margin_bottom)
    protected int menuMarginBottom;

    private ConstraintSet baseConstraintSet;

    private static final int TIME_TO_START_RECORDING = 1000;

    private static final int MAX_RECORD_TIME = 1000;

    private long btnCameraDownTime = 0;

    private Subscription progressSubscription;

    private Subscription recordingSubscription;

    private boolean isRecording;


    private float oldBrightnessLevel;



    //Lifecycle
    @AfterViews
    protected void afterViewsCameraFragment() {
        this.cameraPresenter.init(this, this.getContext(), this.tvCameraPreview);
        this.configureViews();
        this.baseConstraintSet = new ConstraintSet();
        this.baseConstraintSet.clone(this.mainLayout);
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
            this.cameraPresenter.initCameraHelper(this.getContext(), this.tvCameraPreview);
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
    protected boolean onBtnRecordTouch(MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            this.btnCameraDownTime = System.currentTimeMillis();
            startCameraButtonEvent();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            long upTime = System.currentTimeMillis();

            if(upTime - btnCameraDownTime < TIME_TO_START_RECORDING) {
                recordingSubscription.unsubscribe();
                this.cameraPresenter.takePicture(this.getContext(), this.tvCameraPreview);
            } else {
                this.stopRecording();
            }
        }

        return false;
    }


    @Click(R.id.btnCameraChangeCamera)
    protected void onChangeCameraClick() {
        this.cameraPresenter.switchCamera(this.getContext(), this.tvCameraPreview);
    }

    @Click(R.id.btnCameraTopMenu)
    protected void onMenuClick() {
        this.cameraPresenter.onBtnMenuClick();
    }

    @Click(R.id.btnCameraFlash)
    protected void onFlashClick() {
        this.cameraPresenter.changeFlashState();
    }

    @Touch(R.id.tvCameraPreview)
    protected boolean onPreviewTextureTouch() {
        this.cameraPresenter.enableAutoFocus();

        return false;
    }

    @Click(R.id.btnCameraSettings)
    protected void onSettingsClick() {
        this.cameraPresenter.onSettingsClick();
    }

    @Click(R.id.btnCameraInvitations)
    protected void onInvitationsClick() {
        this.cameraPresenter.onInvitationsClick();
    }

    @Click(R.id.btnCameraAddFriends)
    protected void onAddFriendsClick() {
        this.cameraPresenter.onAddFriendsClick();
    }

    @Click(R.id.btnCameraMyFriends)
    protected void onMyFriendsClick() {
        this.cameraPresenter.onMyFriendsClick();
    }


    //CameraView methods
    @Override
    public void setUserName(String name) {
        this.txvUserName.setText(name);
    }

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

    @Override
    public void showMenu() {
        this.runShowMenuAnimation();
    }

    @Override
    public void hideMenu() {
        this.runHideMenuAnimation();
    }

    @Override
    public void changeBtnFlashDrawableId(@DrawableRes int drawableId) {
        this.btnCameraFlash.setImageResource(drawableId);
    }

    @Override
    public void showDeniedPermissionDialog() {
        new AlertDialog.Builder(this.getContext())
                .setTitle(R.string.camera_permissions_denied_title)
                .setMessage(R.string.camera_permissions_denied_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int buttonId) {
                        getActivity().finish();
                    }
                })
                .show();
    }


    //Private methods
    private void configureViews() {
        this.pbRecordProgress.setStartingDegree(270);
        this.pbRecordProgress.setMax(MAX_RECORD_TIME);
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

    private void runShowMenuAnimation() {
        TransitionManager.beginDelayedTransition(this.mainLayout);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.baseConstraintSet);

        constraintSet.connect(R.id.clCameraMenu, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintSet.connect(R.id.clCameraMenu, ConstraintSet.BOTTOM, R.id.btnCameraTopMenu, ConstraintSet.TOP, this.menuMarginBottom);

        constraintSet.connect(R.id.btnCameraTopMenu, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, this.btnCameraRecord.getHeight());
        constraintSet.clear(R.id.btnCameraTopMenu, ConstraintSet.TOP);

        constraintSet.connect(R.id.btnCameraRecord, ConstraintSet.TOP, R.id.tvCameraPreview, ConstraintSet.BOTTOM, 0);
        constraintSet.clear(R.id.btnCameraRecord, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.pbCameraRecordProgress, ConstraintSet.TOP, R.id.tvCameraPreview, ConstraintSet.BOTTOM, 0);
        constraintSet.clear(R.id.pbCameraRecordProgress, ConstraintSet.BOTTOM);

        constraintSet.connect(R.id.btnCameraFlash, ConstraintSet.BOTTOM, R.id.tvCameraPreview, ConstraintSet.TOP, 0);
        constraintSet.clear(R.id.btnCameraFlash, ConstraintSet.TOP);

        constraintSet.connect(R.id.btnCameraChangeCamera, ConstraintSet.BOTTOM, R.id.tvCameraPreview, ConstraintSet.TOP, 0);
        constraintSet.clear(R.id.btnCameraChangeCamera, ConstraintSet.TOP);

        constraintSet.applyTo(this.mainLayout);
    }

    private void runHideMenuAnimation() {
        TransitionManager.beginDelayedTransition(this.mainLayout);

        this.baseConstraintSet.applyTo(this.mainLayout);
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
