package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.view.SurfaceView;
import android.view.TextureView;

/**
 * Created by p.dedio on 05.09.16.
 */
public class Camera2Impl implements CameraHelper {

    private CameraDevice cameraDevice;

    private CameraCaptureSession cameraCaptureSession;

    private CaptureRequest captureRequest;

    private CaptureRequest.Builder captureReques;

    private String cameraIds;




    @Override
    public void init(final Context context, TextureView textureView) {
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                openCamera(context);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    @Override
    public void release() {

    }

    private void openCamera(Context context) {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            this.cameraIds = manager.getCameraIdList();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraIds[0]);
        }
    }
}
