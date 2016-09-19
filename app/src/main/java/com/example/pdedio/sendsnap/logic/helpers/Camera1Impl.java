package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.TextureView;

import java.io.File;

/**
 * Created by p.dedio on 05.09.16.
 */
public class Camera1Impl implements CameraHelper, TextureView.SurfaceTextureListener {

    private Camera camera;


    @Override
    public void init(Context context, TextureView textureView) {
        this.camera = Camera.open();
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void release() {

    }

    @Override
    public File takePicture(Context context, TextureView textureView) {
        return null;
    }

    @Override
    public void switchCamera(Context context, TextureView textureView) {

    }

    @Override
    public void startRecording(Context context, TextureView textureView) {

    }

    @Override
    public File stopRecording() {
        return null;
    }


    // TextureView.SurfaceTextureListener methods
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Camera.Size previewSize = this.camera.getParameters().getPreviewSize();

        try {
            this.camera.setPreviewTexture(surface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.camera.startPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
