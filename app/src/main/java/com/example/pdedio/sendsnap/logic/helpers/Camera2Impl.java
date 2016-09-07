package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.util.Arrays;

/**
 * Created by p.dedio on 05.09.16.
 */
public class Camera2Impl implements CameraHelper {

    private CameraDevice cameraDevice;

    private CameraCaptureSession cameraCaptureSession;

    private CaptureRequest.Builder captureRequestBuilder;

    private String[] cameraIds;

    private int currentCameraId;

    private Size imageDimension;

    private Handler backgroundHandler;

    private HandlerThread backgroundThread;

    private static final String HANDLER_THREAD_NAME = "Camera Thread";

    private ImageReader imageReader;

    private int REQUIRED_WIDTH = 1920;

    private int REQUIRED_HEIGHT = 1080;




    @Override
    public void init(final Context context, final TextureView textureView) {
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                currentCameraId = 0;
                openCamera(context, currentCameraId, textureView);
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
        if(cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

        if(imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    private void openCamera(Context context, int cameraIndex, final TextureView textureView) {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            this.cameraIds = manager.getCameraIdList();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(this.cameraIds[cameraIndex]);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            this.imageDimension = this.getBestCameraSize(map);

            manager.openCamera(this.cameraIds[cameraIndex], new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    cameraDevice = camera;
                    createCameraPreview(textureView);
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    release();
                }

                @Override
                public void onError(CameraDevice cameraDevice, int i) {
                    release();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size getBestCameraSize(StreamConfigurationMap map) {
        Size[] dimensions = map.getOutputSizes(SurfaceTexture.class);
        Size closestSize = dimensions[0];
        int lastWidthResult = Integer.MAX_VALUE;
        int lastHeightResult = Integer.MAX_VALUE;

        for(Size size : dimensions) {
            Log.e("getCameraSize", "width: " + size.getWidth() + " height: " + size.getHeight());
            int width = Math.max(size.getHeight(), size.getWidth());
            int height = Math.min(size.getHeight(), size.getWidth());
            int widthResult = Math.abs(REQUIRED_WIDTH - width);
            int heightResult = Math.abs(REQUIRED_HEIGHT - height);

            if(widthResult < lastWidthResult && heightResult < lastHeightResult) {
                closestSize = size;
                lastWidthResult = widthResult;
                lastHeightResult = heightResult;
            }
        }

        Log.e("getCameraSize", "bestWidth: " + closestSize.getWidth() + " bestHeight: " + closestSize.getHeight());

        return closestSize;
    }

    private void createCameraPreview(TextureView textureView) {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    if(cameraDevice == null) {
                        return;
                    }

                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if (cameraDevice != null) {
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            try {
                cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread(HANDLER_THREAD_NAME);
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        backgroundThread.quitSafely();

        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
