package com.example.pdedio.sendsnap.logic.helpers;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

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

    private int REQUIRED_PHOTO_WIDTH = 1920;

    private int REQUIRED_PHOTO_HEIGHT = 1080;

    private int REQUIRED_VIDEO_WIDTH = 854;

    private int REQUIRED_VIDEO_HEIGHT = 480;

    private MediaRecorder mediaRecorder;

    private String videoPath;

    private boolean isFalshEnabled;

    private Semaphore cameraOpenCloseLock = new Semaphore(1);




    @Override
    public void init(final Context context, final TextureView textureView) {

        if(textureView.isAvailable()) {
            openCamera(context, currentCameraId, textureView);
            return;
        }

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                currentCameraId = 0;
                openCamera(context, currentCameraId, textureView);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                ActivityManager.isUserAMonkey();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                ActivityManager.isUserAMonkey();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                ActivityManager.isUserAMonkey();
            }
        });

    }
    @Override
    public void release() {
        try {
            this.cameraOpenCloseLock.acquire();

            if(this.cameraDevice != null) {
                this.cameraDevice.close();
                this.cameraDevice = null;
            }

            if(this.imageReader != null) {
                this.imageReader.close();
                this.imageReader = null;
            }

            if(this.cameraCaptureSession != null) {
                this.cameraCaptureSession.close();
                this.cameraCaptureSession = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cameraOpenCloseLock.release();
        }


        this.stopBackgroundThread();
    }

    private void openCamera(Context context, int cameraIndex, final TextureView textureView) {
        this.startBackgroundThread();

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            this.cameraIds = manager.getCameraIdList();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(this.cameraIds[cameraIndex]);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] dimensions = map.getOutputSizes(SurfaceTexture.class);
            this.imageDimension = this.getBestCameraSize(dimensions);

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

    private Size getBestCameraSize(Size[] dimensions) {
        return this.getClosestDimension(dimensions, REQUIRED_PHOTO_WIDTH, REQUIRED_PHOTO_HEIGHT);
    }

    private Size getBestVideoSize(Size[] dimensions) {
        return this.getClosestDimension(dimensions, REQUIRED_VIDEO_WIDTH, REQUIRED_VIDEO_HEIGHT);
    }

    private Size getClosestDimension(Size[] dimensions, int requiredWidth, int requiredHeight) {
        Size closestSize = dimensions[0];
        int lastWidthResult = Integer.MAX_VALUE;
        int lastHeightResult = Integer.MAX_VALUE;

        for(Size size : dimensions) {
            int width = Math.max(size.getHeight(), size.getWidth());
            int height = Math.min(size.getHeight(), size.getWidth());
            int widthResult = Math.abs(requiredWidth - width);
            int heightResult = Math.abs(requiredHeight - height);

            if(widthResult < lastWidthResult && heightResult < lastHeightResult) {
                closestSize = size;
                lastWidthResult = widthResult;
                lastHeightResult = heightResult;
            }
        }

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
                if(this.isFalshEnabled) {
                    captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                } else {
                    captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                }
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
        if(backgroundThread != null) {
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

    @Override
    public void takePicture(Context context, final TextureView textureView, final PhotoCallback callback) {
        if(cameraDevice == null) {
            return;
        }
        final File file;

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

            Size imageSize = this.getBestCameraSize(jpegSizes);

            ImageReader reader = ImageReader.newInstance(imageSize.getWidth(), imageSize.getHeight(), ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = this.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            int rotation = windowManager.getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotation);

            file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
            if(file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {

                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try {
                        image = imageReader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onError(e);
                    } finally {
                        if(image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if(output != null) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, backgroundHandler);

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(captureBuilder.build(), null, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, backgroundHandler);

            callback.onPhotoTaken(file);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }

    @Override
    public void switchCamera(Context context, TextureView textureView) {
        if(this.cameraDevice != null) {
            this.cameraDevice.close();
            this.cameraDevice = null;
            this.captureRequestBuilder = null;
        }

        if(this.cameraCaptureSession != null) {
            this.cameraCaptureSession.close();
            this.cameraCaptureSession = null;
        }
        this.currentCameraId = this.currentCameraId == 0 ? 1 : 0;

        this.openCamera(context, this.currentCameraId, textureView);
    }

    @Override
    public void startRecording(Context context, TextureView textureView) {

        try {
            this.setUpMediaRecorder(context);
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(this.imageDimension.getWidth(), this.imageDimension.getHeight());

            captureRequestBuilder = this.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

            if(this.isFalshEnabled) {
                captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            }
            List<Surface> surfaces = new ArrayList<>();

            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            captureRequestBuilder.addTarget(previewSurface);

            Surface recordSurface = this.mediaRecorder.getSurface();
            surfaces.add(recordSurface);
            captureRequestBuilder.addTarget(recordSurface);

            this.cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    updatePreview();
                    mediaRecorder.start();

                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File stopRecording() {
        try {
            this.mediaRecorder.stop();
            this.cameraCaptureSession.abortCaptures();
            this.cameraCaptureSession.close();
            this.cameraCaptureSession = null;
            this.cameraDevice.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mediaRecorder.release();
        this.mediaRecorder = null;

        File file = new File(this.videoPath);

        return file;
    }

    @Override
    public void setFlashLight(boolean enabled) {
        this.isFalshEnabled = enabled;
        this.updatePreview();
    }

    @Override
    public boolean isFrontCamera() {
        return this.currentCameraId == 1;
    }

    @Override
    public void enableAutoFocus() {
        try {
            this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CameraMetadata.CONTROL_AF_MODE_AUTO);

            this.cameraCaptureSession.capture(captureRequestBuilder.build(), null, backgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpMediaRecorder(Context context) {
        if(this.mediaRecorder != null) {
            return;
        }

        try {
            this.mediaRecorder = new MediaRecorder();

            this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            if(videoPath == null || videoPath.isEmpty()) {
                this.videoPath = context.getExternalFilesDir(null).getAbsolutePath() + "/photo.mp4";
            }
            this.mediaRecorder.setOutputFile(this.videoPath);
            this.mediaRecorder.setVideoEncodingBitRate(10000000);
            this.mediaRecorder.setVideoFrameRate(30);
            this.mediaRecorder.setOrientationHint(90);

            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            this.cameraIds = manager.getCameraIdList();
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(this.cameraIds[this.currentCameraId]);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            Size size = this.getBestVideoSize(map.getOutputSizes(MediaRecorder.class));
            this.mediaRecorder.setVideoSize(size.getWidth(), size.getHeight());
            this.mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            this.mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
