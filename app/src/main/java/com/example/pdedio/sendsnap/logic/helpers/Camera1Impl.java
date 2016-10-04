package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.TextureView;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Policy;

/**
 * Created by p.dedio on 05.09.16.
 */
public class Camera1Impl implements CameraHelper, TextureView.SurfaceTextureListener {

    private Camera camera;

    private MediaRecorder mediaRecorder;

    private String videoPath;

    private String[] cameraIds;

    private int currentCameraId;

    private File photo;


    @Override
    public void init(Context context, TextureView textureView) {
        this.openCamera(this.currentCameraId, textureView);
    }

    @Override
    public void release() {
        this.camera.release();
    }

    @Override
    public void takePicture(Context context, TextureView textureView, final PhotoCallback callback) {
        this.photo = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
        this.camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {

                try {
                    FileOutputStream fos = new FileOutputStream(photo);
                    fos.write(bytes);
                    fos.close();
                    callback.onPhotoTaken(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        });
    }

    @Override
    public void switchCamera(Context context, TextureView textureView) {
        if(this.camera != null) {
            this.camera.release();
            this.camera = null;
        }
        this.currentCameraId = this.currentCameraId == 0 ? 1 : 0;

        this.openCamera(this.currentCameraId, textureView);
    }

    @Override
    public void startRecording(Context context, TextureView textureView) {
        this.initMediaRecorder(context);

        try {
            this.mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                    Log.e("onInfo", "i: " + i + "i1" + i1);
                }
            });
            this.mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                    Log.e("onError", "i: " + i + "i1" + i1);
                }
            });
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

    @Override
    public int getNumberOfCameras(Context context) {
        return Camera.getNumberOfCameras();
    }

    @Override
    public void setFlashLight(boolean enabled) {

        if(enabled) {
            Camera.Parameters parameters = this.camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            this.camera.setParameters(parameters);
        } else {
            Camera.Parameters parameters = this.camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            this.camera.setParameters(parameters);
        }
    }

    @Override
    public boolean isFrontCamera() {
        return this.currentCameraId == 1;
    }

    @Override
    public void enableAutoFocus() {
        this.camera.autoFocus(null);
    }


    // TextureView.SurfaceTextureListener methods
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.setCameraPreview(surface);
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
    private void openCamera(int cameraId, TextureView textureView) {
        this.camera = Camera.open(cameraId);
        this.camera.setDisplayOrientation(90);

        if(textureView.getSurfaceTextureListener() == null) {
            if(textureView.isAvailable()) {
                this.setCameraPreview(textureView.getSurfaceTexture());
            } else {
                textureView.setSurfaceTextureListener(this);
            }

        } else {
            try {
                this.camera.reconnect();
                this.setCameraPreview(textureView.getSurfaceTexture());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setCameraPreview(SurfaceTexture surface) {
        try {
            this.camera.setPreviewTexture(surface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.camera.startPreview();
    }

    private void initMediaRecorder(Context context) {
        this.mediaRecorder = new MediaRecorder();
        this.camera.unlock();
        this.mediaRecorder.setCamera(this.camera);
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
