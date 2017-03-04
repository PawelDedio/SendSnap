package com.example.pdedio.sendsnap.camera;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.support.design.widget.TabLayout;
import android.view.TextureView;

import com.example.pdedio.sendsnap.R;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Mockito.*;

/**
 * Created by p.dedio on 20.12.16.
 */

public class Camera1ImplTest {

    @Mock
    private Context mockedContext;

    @Mock
    private TextureView mockedTexture;

    @Mock
    private Camera mockedCamera;

    @Mock
    private MediaRecorder mockedRecorder;

    @Mock
    private CameraFactory mockedCameraFactory;

    @Mock
    private Camera.Parameters mockedParameters;


    private Camera1Impl configureAndInitCameraHelper() {
        Camera1Impl cameraHelper = this.configureCameraHelper();
        cameraHelper.init(this.mockedContext, this.mockedTexture, 0);

        return cameraHelper;
    }

    private Camera1Impl configureCameraHelper() {
        MockitoAnnotations.initMocks(this);

        Camera1Impl cameraHelper = new Camera1Impl();
        cameraHelper.camera = this.mockedCamera;
        cameraHelper.mediaRecorder = this.mockedRecorder;
        cameraHelper.cameraFactory = this.mockedCameraFactory;

        return cameraHelper;
    }


    //init()
    @Test
    public void initShouldOpenCamera() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        int requiredId = 1;

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.init(this.mockedContext, this.mockedTexture, requiredId);

        verify(this.mockedCameraFactory).getOldCamera(requiredId);
    }

    @Test
    public void initShouldSetProperCameraOrientation() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        int requiredId = 1;

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.init(this.mockedContext, this.mockedTexture, requiredId);

        verify(this.mockedCamera).setDisplayOrientation(90);
    }


    //release()
    @Test
    public void shouldReleaseCamera() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        cameraHelper.release();

        verify(this.mockedCamera).release();
    }


    //takePicture()
    @Test
    public void shouldTakePicture() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedContext.getDir("media", Context.MODE_PRIVATE)).thenReturn(new File("/data/user/0/com.example.pdedio.sendsnap/app_media"));
        when(mockedContext.getString(R.string.snap_sent_file_name)).thenReturn("tmp_photo");

        cameraHelper.takePicture(this.mockedContext, this.mockedTexture, mock(CameraHelper.PhotoCallback.class));

        verify(mockedCamera).takePicture(any(Camera.ShutterCallback.class), any(Camera.PictureCallback.class), any(Camera.PictureCallback.class));
    }


    //switchCamera()
    @Test
    public void switchCameraShouldReleaseCamera() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.switchCamera(mockedContext, mockedTexture);

        verify(mockedCamera).release();
    }

    @Test
    public void shouldNotReleaseCameraWhenIsNull() {
        Camera1Impl cameraHelper = this.configureCameraHelper();
        cameraHelper.camera = null;

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.switchCamera(mockedContext, mockedTexture);

        verify(mockedCamera, never()).release();
    }

    @Test
    public void shouldOpenAnotherCamera() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        int requiredId = 1;
        int expectedId = 0;

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.init(mockedContext, mockedTexture, requiredId);
        cameraHelper.switchCamera(mockedContext, mockedTexture);

        verify(this.mockedCameraFactory).getOldCamera(expectedId);
    }


    //startRecording()
    @Test
    public void shouldPrepareMediaRecorder() throws Exception {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedContext.getExternalFilesDir(null)).thenReturn(new File("/data/user/0/com.example.pdedio.sendsnap/app_media"));

        cameraHelper.startRecording(mockedContext, mockedTexture);

        verify(mockedRecorder).prepare();
    }

    @Test
    public void shouldStartMediaRecorder() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedContext.getExternalFilesDir(null)).thenReturn(new File("/data/user/0/com.example.pdedio.sendsnap/app_media"));

        cameraHelper.startRecording(mockedContext, mockedTexture);

        verify(mockedRecorder).start();
    }


    //stopRecording()
    @Test
    public void shouldStopMediaRecorder() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedContext.getExternalFilesDir(null)).thenReturn(new File("/data/user/0/com.example.pdedio.sendsnap/app_media"));

        cameraHelper.startRecording(mockedContext, mockedTexture);

        cameraHelper.stopRecording();

        verify(mockedRecorder).stop();
    }

    @Test
    public void shouldReturnProperFilePath() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedContext.getExternalFilesDir(null)).thenReturn(new File("/data/user/0/com.example.pdedio.sendsnap/app_media"));

        cameraHelper.startRecording(mockedContext, mockedTexture);

        File file = cameraHelper.stopRecording();

        Assert.assertEquals(file.getAbsolutePath(), "/data/user/0/com.example.pdedio.sendsnap/app_media/photo.mp4");
    }


    //setFlashLight()
    @Test
    public void shouldSetFlashModeTorch() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedCamera.getParameters()).thenReturn(mockedParameters);

        cameraHelper.setFlashLight(true);

        verify(mockedParameters).setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        verify(mockedCamera).setParameters(mockedParameters);
    }

    @Test
    public void shouldDisableFlash() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(mockedCamera.getParameters()).thenReturn(mockedParameters);

        cameraHelper.setFlashLight(false);

        verify(mockedParameters).setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        verify(mockedCamera).setParameters(mockedParameters);
    }


    //isFrontCamera()
    @Test
    public void shouldReturnFalseWhenCameraIdIs0() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.init(mockedContext, mockedTexture, 0);

        Assert.assertFalse(cameraHelper.isFrontCamera());
    }

    @Test
    public void shouldReturnTrueWhenCameraIdIs1() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        when(this.mockedCameraFactory.getOldCamera(anyInt())).thenReturn(this.mockedCamera);

        cameraHelper.init(mockedContext, mockedTexture, 1);

        Assert.assertTrue(cameraHelper.isFrontCamera());
    }


    //enableAutoFocus()
    @Test
    public void shouldTriggerCameraAutoFocus() {
        Camera1Impl cameraHelper = this.configureCameraHelper();

        cameraHelper.enableAutoFocus();

        verify(mockedCamera).autoFocus(any(Camera.AutoFocusCallback.class));
    }
}
