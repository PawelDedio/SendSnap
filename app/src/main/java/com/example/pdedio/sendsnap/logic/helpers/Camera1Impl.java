package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.TextureView;

import java.io.File;

/**
 * Created by p.dedio on 05.09.16.
 */
public class Camera1Impl implements CameraHelper, TextureView.SurfaceTextureListener {

    private Camera camera;

    private MediaRecorder mediaRecorder;

    private String videoPath;


    @Override
    public void init(Context context, TextureView textureView) {
        this.camera = Camera.open();
        this.camera.setDisplayOrientation(90);
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
        this.initMediaRecorder(context);

        try {
            this.mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.mediaRecorder.start();
    }

    @Override
    public File stopRecording() {
        this.mediaRecorder.stop();

        return new File(this.videoPath);
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


    //Private methods
    private void initMediaRecorder(Context context) {
        this.mediaRecorder = new MediaRecorder();
        this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        this.mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        this.mediaRecorder.setProfile(profile);

        if(videoPath == null || videoPath.isEmpty()) {
            this.videoPath = context.getExternalFilesDir(null).getAbsolutePath() + "/photo.mp4";
        }
        this.mediaRecorder.setOutputFile(this.videoPath);
        this.mediaRecorder.setOrientationHint(90);
    }
}
