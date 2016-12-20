package com.example.pdedio.sendsnap.camera;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.TextureView;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.helpers.BitmapsManager;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.permissions.PermissionManager;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;

/**
 * Created by pawel on 16.12.2016.
 */

public class CameraPresenterTest {

    @Mock
    protected CameraContract.CameraView mockedView;

    @Mock
    protected PermissionManager mockedPermissionManager;

    @Mock
    protected Context mockedContext;

    @Mock
    protected TextureView mockedTexture;

    @Mock
    protected CameraHelper mockedCameraHelper;

    @Mock
    protected BitmapsManager mockedBitmapsManager;

    @Mock
    protected SharedPreferenceManager mockedPreferenceManager;

    protected File templateFile = new File("./src/test/resources/photo.jpg");



    private CameraPresenter configureAndInitPresenter() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        return presenter;
    }

    private CameraPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        CameraPresenter presenter = new CameraPresenter();
        presenter.permissionManager = mockedPermissionManager;
        presenter.cameraHelper = mockedCameraHelper;
        presenter.bitmapsManager = mockedBitmapsManager;
        presenter.sharedPreferenceManager = mockedPreferenceManager;

        return presenter;
    }


    //init()
    @Test
    public void initShouldHideStatusBar() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        verify(mockedView).hideStatusBar();
    }

    @Test
    public void initShouldCheckPermissions() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        verify(mockedPermissionManager).isHavePermission(mockedContext, permissions);
    }

    @Test
    public void shouldNotRequestForPermissionsWhenAllGranted() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        when(mockedPermissionManager.isHavePermission(any(Context.class), any(String[].class)))
                .thenReturn(true);

        verify(mockedPermissionManager, never())
                .requestForPermission(any(PermissionManager.PermissionCallback.class), any(String[].class));
    }

    @Test
    public void shouldRequestForPermissionsWhenDenied() {
        CameraPresenter presenter = this.configurePresenter();

        when(mockedPermissionManager.isHavePermission(any(Context.class), any(String[].class)))
                .thenReturn(false);

        presenter.init(mockedView, mockedContext, mockedTexture);

        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        verify(mockedPermissionManager).requestForPermission(any(PermissionManager.PermissionCallback.class), (String) anyVararg());
    }


    //startRecording()
    @Test
    public void shouldStartRecording() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.startRecording(mockedContext, mockedTexture);

        verify(mockedCameraHelper).startRecording(any(Context.class), any(TextureView.class));
    }

    @Test
    public void shouldEnableFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.startRecording(mockedContext, mockedTexture);

        verify(mockedView).startFrontFlash();
    }


    //stopRecording()
    @Test
    public void shouldStopRecording() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        when(mockedCameraHelper.stopRecording()).thenReturn(templateFile);
        presenter.stopRecording();

        verify(mockedCameraHelper).stopRecording();
    }

    @Test
    public void shouldStopFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.stopRecording();

        verify(mockedView).stopFrontFlash();
    }

    @Test
    public void showOpenFragment() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.stopRecording();

        verify(mockedView).showFragment(any(BaseFragment.class));
    }


    //takePicture()
    @Test
    public void shouldTakePicture() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.takePicture(mockedContext, mockedTexture);

        verify(mockedCameraHelper).takePicture(any(Context.class), any(TextureView.class), any(CameraHelper.PhotoCallback.class));
    }

    @Test
    public void shouldStartFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.takePicture(mockedContext, mockedTexture);

        verify(mockedView).startFrontFlash();
    }

    @Test
    public void shouldStopFrontFlashIfEnabledAfterSuccess() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        when(mockedCameraHelper.isFrontCamera()).thenReturn(true);
        when(mockedBitmapsManager.getBitmapFromFile(any(File.class))).thenReturn(mock(Bitmap.class));
        when(mockedBitmapsManager.rotateAndScale(any(Bitmap.class), anyFloat(), anyFloat(), anyFloat())).thenReturn(mock(Bitmap.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CameraHelper.PhotoCallback callback = (CameraHelper.PhotoCallback) invocation.getArguments()[2];
                callback.onPhotoTaken(templateFile);
                return null;
            }
        }).when(mockedCameraHelper).takePicture(any(Context.class), any(TextureView.class), any(CameraHelper.PhotoCallback.class));

        presenter.takePicture(mockedContext, mockedTexture);

        verify(mockedView).stopFrontFlash();
    }


    //switchCamera()
    @Test
    public void shouldSwitchCamera() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.switchCamera(mockedContext, mockedTexture);

        verify(mockedCameraHelper).switchCamera(any(Context.class), any(TextureView.class));
    }

    @Test
    public void shouldSetActualFlashValueWhenIsRearCamera() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        when(mockedCameraHelper.isFrontCamera()).thenReturn(false);

        presenter.switchCamera(mockedContext, mockedTexture);

        verify(mockedCameraHelper).setFlashLight(presenter.isFlashEnabled);
    }

    @Test
    public void shouldUpdateValueInSharedPreferenceManager() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.switchCamera(mockedContext, mockedTexture);

        verify(mockedPreferenceManager).setCameraId(any(Integer.class));
    }


    //initCameraHelper()
    @Test
    public void shouldInitCameraWithValueFromSharedPreferences() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        when(mockedPreferenceManager.getCameraId()).thenReturn(1);
        presenter.initCameraHelper(mockedContext, mockedTexture);

        verify(mockedCameraHelper).init(mockedContext, mockedTexture, 1);
    }

    @Test
    public void shouldNotInitCameraWhenCameraHelperIsNull() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.cameraHelper = null;

        verify(mockedCameraHelper, never()).init(mockedContext, mockedTexture, 1);
    }

    @Test
    public void shouldNotInitCameraWhenCameraViewIsNull() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.cameraView = null;

        verify(mockedCameraHelper, never()).init(mockedContext, mockedTexture, 1);
    }

    @Test
    public void shouldNotInitWhenCameraIsConfigured() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isCameraConfigured = true;

        verify(mockedCameraHelper, never()).init(mockedContext, mockedTexture, 1);
    }


    //changeFlashState()
    @Test
    public void shouldChangeButtonDrawable() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.changeFlashState();

        verify(mockedView).changeBtnFlashDrawableId(anyInt());
    }

    @Test
    public void shouldChangeIsFlashEnabledValue() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        boolean oldValue = presenter.isFlashEnabled;

        presenter.changeFlashState();

        Assert.assertNotSame(oldValue, presenter.isFlashEnabled);
    }

    @Test
    public void shouldSetProperFlashValueIfRearCamera() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        when(mockedCameraHelper.isFrontCamera()).thenReturn(false);

        presenter.changeFlashState();

        verify(mockedCameraHelper).setFlashLight(presenter.isFlashEnabled);
    }


    //enableAutoFocus()
    @Test
    public void shouldEnableAutoFocus() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.enableAutoFocus();

        verify(mockedCameraHelper).enableAutoFocus();
    }


    //destroy()
    @Test
    public void shouldSetViewToNull() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.destroy();

        Assert.assertNull(presenter.cameraView);
    }
}
