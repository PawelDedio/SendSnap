package com.example.pdedio.sendsnap.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.TextureView;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.BackKeyListener;
import com.example.pdedio.sendsnap.edit_snap.EditSnapFragment;
import com.example.pdedio.sendsnap.edit_snap.EditSnapFragment_;
import com.example.pdedio.sendsnap.helpers.BitmapsManager;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.SessionManager;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.models.User;
import com.example.pdedio.sendsnap.permissions.PermissionManager;
import com.example.pdedio.sendsnap.permissions.PermissionSession;
import com.example.pdedio.sendsnap.permissions.PermissionsResult;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.DimensionRes;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static android.Manifest.permission;

/**
 * Created by p.dedio on 31.08.16.
 */
@EBean
public class CameraPresenter extends BaseFragmentPresenter implements CameraContract.CameraPresenter, BackKeyListener {

    @Bean
    protected PermissionManager permissionManager;

    @Bean
    protected BitmapsManager bitmapsManager;

    @Bean
    protected SharedPreferenceManager sharedPreferenceManager;

    @Bean
    protected SessionManager sessionManager;

    protected CameraContract.CameraView cameraView;

    protected CameraHelper cameraHelper;

    protected boolean isFlashEnabled;

    protected boolean isMenuVisible;

    protected boolean isCameraConfigured;

    protected boolean isRecording;


    //Lifecycle
    @Override
    public void init(CameraContract.CameraView view, Context context, TextureView textureView) {
        this.cameraView = view;
        this.cameraView.hideStatusBar();
        this.cameraView.setOnBackKeyListener(this);
        this.checkPermissions(context, textureView);
        this.setUserName();
    }

    @Override
    public void destroy() {
        this.cameraView.setOnBackKeyListener(null);
        this.cameraView = null;
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
        }
    }


    //CameraPresenter methods
    @Override
    public void initCameraHelper(Context context, TextureView textureView) {
        if(this.cameraHelper == null || this.cameraView == null || this.isCameraConfigured) {
            return;
        }
        int cameraId = this.sharedPreferenceManager.getCameraId();

        this.cameraHelper.init(context, textureView, cameraId);
    }

    @Override
    public void takePicture(Context context, TextureView textureView) {
        if(isFlashEnabled && cameraHelper.isFrontCamera()) {
            this.cameraView.startFrontFlash();
        }

        this.cameraHelper.takePicture(context, textureView, new CameraHelper.PhotoCallback() {
            @Override
            public void onPhotoTaken(File photo) {
                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    cameraView.stopFrontFlash();
                }
                Bitmap rotatedBitmap = rotateAndSaveImage(photo);
                openFragment(photo, Consts.SNAP_TYPE_PHOTO, rotatedBitmap);
            }

            @Override
            public void onError(Exception e) {
                if(isFlashEnabled && cameraHelper.isFrontCamera()) {
                    cameraView.stopFrontFlash();
                }
            }
        });
    }

    @Override
    public void onBtnMenuClick() {
        if(this.isMenuVisible) {
            this.cameraView.hideMenu();
        } else {
            this.cameraView.showMenu();
        }

        this.isMenuVisible = !this.isMenuVisible;
    }

    @Override
    public void startRecording(Context context, TextureView textureView) {
        if(this.isRecording) {
            return;
        }
        this.isRecording = true;

        if(isFlashEnabled && cameraHelper.isFrontCamera()) {
            this.cameraView.startFrontFlash();
        }

        this.cameraHelper.startRecording(context, textureView);
    }

    @Override
    public void stopRecording() {
        if(!this.isRecording) {
            return;
        }
        this.isRecording = false;

        File videoFile = this.cameraHelper.stopRecording();

        if(isFlashEnabled && cameraHelper.isFrontCamera()) {
            this.cameraView.stopFrontFlash();
        }

        openFragment(videoFile, Consts.SNAP_TYPE_VIDEO, null);
    }

    @Override
    public void switchCamera(Context context, TextureView textureView) {
        this.cameraHelper.switchCamera(context, textureView);
        if(!cameraHelper.isFrontCamera()) {
            cameraHelper.setFlashLight(isFlashEnabled);
        }
        this.sharedPreferenceManager.setCameraId(this.cameraHelper.getCurrentCameraId());
    }

    @Override
    public void changeFlashState() {
        isFlashEnabled = !isFlashEnabled;
        int resourceId = isFlashEnabled ? R.drawable.btn_flash_enabled : R.drawable.btn_flash_disabled;
        this.cameraView.changeBtnFlashDrawableId(resourceId);

        if(!cameraHelper.isFrontCamera()) {
            cameraHelper.setFlashLight(isFlashEnabled);
        }
    }

    @Override
    public void enableAutoFocus() {
        this.cameraHelper.enableAutoFocus();
    }

    //BackKeyListener methods
    @Override
    public boolean onBackKeyClick() {
        if(this.isMenuVisible) {
            this.onBtnMenuClick();
            return true;
        }
        return false;
    }

    @Override
    public void onSettingsClick() {
        //TODO: implementation
    }

    @Override
    public void onInvitationsClick() {
        //TODO: implementation
    }

    @Override
    public void onAddFriendsClick() {
        //TODO: implementation
    }

    @Override
    public void onMyFriendsClick() {

    }


    //Private methods
    private void checkPermissions(final Context context, final TextureView textureView) {
        String[] permissions = {permission.RECORD_AUDIO, permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE};
        if(this.permissionManager.isHavePermission(context, permissions)) {
            this.prepareLogic(context, textureView);
        } else {
            this.permissionManager.requestForPermission(new PermissionManager.PermissionCallback() {
                @Override
                public void onPermissionChecked(PermissionsResult result) {
                    if(result.areAllPermissionGranted()) {
                        prepareLogic(context, textureView);
                    } else {
                        cameraView.showDeniedPermissionDialog();
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<com.example.pdedio.sendsnap.permissions.PermissionRequest> permissions, PermissionSession session) {
                    session.continuePermissionRequest();
                }
            }, permissions);
        }
    }

    private void prepareLogic(Context context, TextureView textureView) {
        if(this.cameraHelper == null) {
            this.cameraHelper = CameraHelper.Factory.create(context);
        }
        this.initCameraHelper(context, textureView);
        this.isCameraConfigured = true;
    }

    private void setUserName() {
        User user = this.sessionManager.getLoggedUser();
        if(user == null) {
            return;
        }

        if(user.displayName == null || user.displayName.isEmpty()) {
            this.cameraView.setUserName(user.name);
        } else {
            this.cameraView.setUserName(user.displayName);
        }
    }

    private Bitmap rotateAndSaveImage(File file) {
        Bitmap bitmap = this.bitmapsManager.getBitmapFromFile(file);

        if(this.cameraHelper.isFrontCamera()) {
            bitmap = this.bitmapsManager.rotateAndScale(bitmap, 90, -1.0f, 1.0f);
        } else {
            bitmap = this.bitmapsManager.rotate(bitmap, 90);
        }

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

    private void openFragment(File file, int snapType, Bitmap bitmap) {
        EditSnapFragment fragment = EditSnapFragment_.builder().snapFile(file).snapType(snapType)
                .snapBitmap(bitmap).build();
        this.cameraView.showFragment(fragment);
    }
}
