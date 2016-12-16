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
import org.mockito.Mockito;
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

        Mockito.verify(mockedView).hideStatusBar();
    }

    @Test
    public void initShouldCheckPermissions() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Mockito.verify(mockedPermissionManager).isHavePermission(mockedContext, permissions);
    }

    @Test
    public void shouldNotRequestForPermissionsWhenAllGranted() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        Mockito.when(mockedPermissionManager.isHavePermission(Mockito.any(Context.class), Mockito.any(String[].class)))
                .thenReturn(true);

        Mockito.verify(mockedPermissionManager, Mockito.never())
                .requestForPermission(Mockito.any(PermissionManager.PermissionCallback.class), Mockito.any(String[].class));
    }

    @Test
    public void shouldRequestForPermissionsWhenDenied() {
        CameraPresenter presenter = this.configurePresenter();

        Mockito.when(mockedPermissionManager.isHavePermission(Mockito.any(Context.class), Mockito.any(String[].class)))
                .thenReturn(false);

        presenter.init(mockedView, mockedContext, mockedTexture);

        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Mockito.verify(mockedPermissionManager).requestForPermission(Mockito.any(PermissionManager.PermissionCallback.class), (String) Mockito.anyVararg());
    }


    //startRecording()
    @Test
    public void shouldStartRecording() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.startRecording(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).startRecording(Mockito.any(Context.class), Mockito.any(TextureView.class));
    }

    @Test
    public void shouldEnableFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.startRecording(mockedContext, mockedTexture);

        Mockito.verify(mockedView).startFrontFlash();
    }


    //stopRecording()
    @Test
    public void shouldStopRecording() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        Mockito.when(mockedCameraHelper.stopRecording()).thenReturn(templateFile);
        presenter.stopRecording();

        Mockito.verify(mockedCameraHelper).stopRecording();
    }

    @Test
    public void shouldStopFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.stopRecording();

        Mockito.verify(mockedView).stopFrontFlash();
    }

    @Test
    public void showOpenFragment() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.stopRecording();

        Mockito.verify(mockedView).showFragment(Mockito.any(BaseFragment.class));
    }


    //takePicture()
    @Test
    public void shouldTakePicture() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.takePicture(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).takePicture(Mockito.any(Context.class), Mockito.any(TextureView.class), Mockito.any(CameraHelper.PhotoCallback.class));
    }

    @Test
    public void shouldStartFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.takePicture(mockedContext, mockedTexture);

        Mockito.verify(mockedView).startFrontFlash();
    }

    @Test
    public void shouldStopFrontFlashIfEnabledAfterSuccess() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);
        Mockito.when(mockedBitmapsManager.getBitmapFromFile(Mockito.any(File.class))).thenReturn(Mockito.mock(Bitmap.class));
        Mockito.when(mockedBitmapsManager.rotateAndScale(Mockito.any(Bitmap.class), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat())).thenReturn(Mockito.mock(Bitmap.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CameraHelper.PhotoCallback callback = (CameraHelper.PhotoCallback) invocation.getArguments()[2];
                callback.onPhotoTaken(templateFile);
                return null;
            }
        }).when(mockedCameraHelper).takePicture(Mockito.any(Context.class), Mockito.any(TextureView.class), Mockito.any(CameraHelper.PhotoCallback.class));

        presenter.takePicture(mockedContext, mockedTexture);

        Mockito.verify(mockedView).stopFrontFlash();
    }


    //switchCamera()
    @Test
    public void shouldSwitchCamera() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.switchCamera(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).switchCamera(Mockito.any(Context.class), Mockito.any(TextureView.class));
    }

    @Test
    public void shouldSetActualFlashValueWhenIsRearCamera() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(false);

        presenter.switchCamera(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).setFlashLight(presenter.isFlashEnabled);
    }

    @Test
    public void shouldUpdateValueInSharedPreferenceManager() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.switchCamera(mockedContext, mockedTexture);

        Mockito.verify(mockedPreferenceManager).setCameraId(Mockito.any(Integer.class));
    }


    //initCameraHelper()
    @Test
    public void shouldInitCameraWithValueFromSharedPreferences() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        Mockito.when(mockedPreferenceManager.getCameraId()).thenReturn(1);
        presenter.initCameraHelper(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).init(mockedContext, mockedTexture, 1);
    }

    @Test
    public void shouldNotInitCameraWhenCameraHelperIsNull() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.cameraHelper = null;

        Mockito.verify(mockedCameraHelper, Mockito.never()).init(mockedContext, mockedTexture, 1);
    }

    @Test
    public void shouldNotInitCameraWhenCameraViewIsNull() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.cameraView = null;

        Mockito.verify(mockedCameraHelper, Mockito.never()).init(mockedContext, mockedTexture, 1);
    }

    @Test
    public void shouldNotInitWhenCameraIsConfigured() {
        CameraPresenter presenter = this.configureAndInitPresenter();
        presenter.isCameraConfigured = true;

        Mockito.verify(mockedCameraHelper, Mockito.never()).init(mockedContext, mockedTexture, 1);
    }


    //changeFlashState()
    @Test
    public void shouldChangeButtonDrawable() {
        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.changeFlashState();

        Mockito.verify(mockedView).changeBtnFlashDrawableId(Mockito.anyInt());
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

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(false);

        presenter.changeFlashState();

        Mockito.verify(mockedCameraHelper).setFlashLight(presenter.isFlashEnabled);
    }


    //enableAutoFocus()
    @Test
    public void shouldEnableAutoFocus() {

        CameraPresenter presenter = this.configureAndInitPresenter();

        presenter.enableAutoFocus();

        Mockito.verify(mockedCameraHelper).enableAutoFocus();
    }
}
