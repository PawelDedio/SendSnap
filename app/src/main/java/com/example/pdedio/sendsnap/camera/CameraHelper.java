package com.example.pdedio.sendsnap.camera;

import android.content.Context;
import android.os.Build;
import android.view.TextureView;

import java.io.File;

/**
 * Created by p.dedio on 05.09.16.
 */
public interface CameraHelper {

    void init(Context context, TextureView textureView, int cameraId);

    void release();

    void takePicture(Context context, final TextureView textureView, PhotoCallback callback);

    void switchCamera(Context context, TextureView textureView);

    void startRecording(Context context, TextureView textureView);

    File stopRecording();

    void setFlashLight(boolean enabled);

    boolean isFrontCamera();

    void enableAutoFocus();

    int getCurrentCameraId();


    class Factory {
        public static CameraHelper create(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return Camera1Impl_.getInstance_(context);
                //return new Camera2Impl();
            } else {
                return Camera1Impl_.getInstance_(context);
            }
        }
    }

    interface PhotoCallback {
        void onPhotoTaken(File photo);
        void onError(Exception e);
    }
}
