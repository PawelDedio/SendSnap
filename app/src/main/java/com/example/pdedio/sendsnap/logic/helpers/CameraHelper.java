package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.os.Build;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.File;

/**
 * Created by p.dedio on 05.09.16.
 */
public interface CameraHelper {

    void init(Context context, TextureView textureView);

    void release();

    File takePicture(Context context, final TextureView textureView);

    void switchCamera(Context context, TextureView textureView);

    void startRecording(Context context, TextureView textureView);

    File stopRecording();

    int getNumberOfCameras(Context context);

    void setFlashLight(boolean enabled);


    class Factory {
        public static CameraHelper build() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //return new Camera2Impl();
                return new Camera1Impl();
            } else {
                return new Camera1Impl();
            }
        }
    }
}
