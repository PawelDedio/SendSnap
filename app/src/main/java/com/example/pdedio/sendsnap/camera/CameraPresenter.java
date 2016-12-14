package com.example.pdedio.sendsnap.camera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.example.pdedio.sendsnap.edit_snap.EditSnapFragment;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.SharedPrefHelper_;
import com.example.pdedio.sendsnap.ui.fragments.EditSnapFragment_;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static android.Manifest.permission;

/**
 * Created by p.dedio on 31.08.16.
 */
@EBean
public class CameraPresenter extends BaseFragmentPresenter implements CameraContract.CameraPresenter {

    @Pref
    protected SharedPrefHelper_ sharedPrefHelper;

    private CameraContract.CameraView cameraView;

    private CameraHelper cameraHelper;

    private boolean isFlashEnabled;

    private float oldBrightnessLevel;

    private boolean isCameraConfigured;


    //Lifecycle
    public void init(CameraContract.CameraView view) {
        this.cameraView = view;
    }

    @Override
    public void afterViews() {
        this.cameraView.hideStatusBar();
        this.checkPermissions();
    }

    @Override
    public void destroy() {
        this.cameraView = null;
    }

    @Override
    public void onResume() {
        if(this.cameraHelper != null && this.cameraView != null && !this.isCameraConfigured) {
            this.initCameraHelper();
        }
    }

    @Override
    public void onVisibilityChanged(boolean isVisible) {
        if(isVisible) {
            this.cameraView.hideStatusBar();
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
                    new AlertDialog.Builder(cameraView.getActivityContext())
                            .setTitle(R.string.camera_permissions_denied_title)
                            .setMessage(R.string.camera_permissions_denied_message)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int buttonId) {
                                    cameraView.getBaseFragmentActivity().finish();
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
        if(this.cameraHelper == null) {
            this.cameraHelper = CameraHelper.Factory.create();
        }
        this.initCameraHelper();
        this.isCameraConfigured = true;
    }

    private void configureViews() {
        DonutProgress progress = this.cameraView.getCameraProgressBar();
        progress.setStartingDegree(270);
        progress.setMax(MAX_RECORD_TIME);


        this.cameraView.getCameraButton().setOnTouchListener(new View.OnTouchListener() {
            long downTime = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    cameraView.startFrontFlash();
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

        this.cameraView.getChangeCameraButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        this.cameraView.getFlashButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseImageButton button = (BaseImageButton) v;
                isFlashEnabled = !isFlashEnabled;
                int resourceId = isFlashEnabled ? R.drawable.btn_flash_enabled : R.drawable.btn_flash_disabled;
                button.setImageResource(resourceId);

                if(!cameraHelper.isFrontCamera()) {
                    cameraHelper.setFlashLight(isFlashEnabled);
                }


            }
        });

        this.cameraView.getPreviewTextureView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cameraHelper.enableAutoFocus();
                return false;
            }
        });
    }

    public void initCameraHelper(Context context, TextureView textureView) {
        int cameraId = this.sharedPrefHelper.cameraId().get();

        this.cameraHelper.init(context, textureView, cameraId);
    }

    public void takePicture(Context context, TextureView textureView) {
        this.cameraHelper.takePicture(context, textureView, new CameraHelper.PhotoCallback() {
            @Override
            public void onPhotoTaken(File photo) {
                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    cameraView.stopFrontFlash();
                }
                Bitmap rotatedBitmap = rotateAndSaveImage(photo);
                openFragment(photo, Consts.SnapType.PHOTO, rotatedBitmap);
            }

            @Override
            public void onError(Exception e) {
                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    cameraView.stopFrontFlash();
                }
            }
        });
    }

    private Bitmap rotateAndSaveImage(File file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        if(this.cameraHelper.isFrontCamera()) {
            matrix.preScale(-1.0f, 1.0f);
        }

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        FileOutputStream stream;
        try {
            file.createNewFile();
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void startRecording(Context context, TextureView textureView) {
        this.cameraHelper.startRecording(context, textureView);
    }

    public void stopRecording() {
        File videoFile = this.cameraHelper.stopRecording();
        openFragment(videoFile, Consts.SnapType.VIDEO, null);
    }

    public void switchCamera(Context context, TextureView textureView) {
        this.cameraHelper.switchCamera(context, textureView);
        if(!cameraHelper.isFrontCamera()) {
            cameraHelper.setFlashLight(isFlashEnabled);
        }
        this.sharedPrefHelper.cameraId().put(this.cameraHelper.getCurrentCameraId());
    }

    private void openFragment(File file, Consts.SnapType snapType, Bitmap bitmap) {
        EditSnapFragment fragment = EditSnapFragment_.builder().snapFile(file).snapType(snapType)
                .snapBitmap(bitmap).build();
        this.cameraView.showFragment(fragment);
    }


    public interface PresenterCallback {
        DonutProgress getCameraProgressBar();

        BaseButton getCameraButton();

        Context getActivityContext();

        TextureView getPreviewTextureView();

        BaseImageButton getChangeCameraButton();

        BaseImageButton getFlashButton();

        BaseFragmentActivity getBaseFragmentActivity();

        View getFrontFlashView();

        void showStatusBar();

        void hideStatusBar();
    }

    //TODO: Move clickListeners to view, change permission logic
}
