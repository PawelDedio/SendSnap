package com.example.pdedio.sendsnap.camera;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.TextureView;

import com.example.pdedio.sendsnap.BaseFragment;
import com.example.pdedio.sendsnap.helpers.BitmapsManager;
import com.example.pdedio.sendsnap.permissions.PermissionManager;

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

    protected File templateFile = new File("./src/test/resources/photo.jpg");

    private CameraPresenter configurePresenter() {
        MockitoAnnotations.initMocks(this);
        CameraPresenter presenter = new CameraPresenter();
        presenter.permissionManager = mockedPermissionManager;
        presenter.cameraHelper = mockedCameraHelper;
        presenter.bitmapsManager = mockedBitmapsManager;

        return presenter;
    }


    //init()
    @Test
    public void initShouldHideStatusBar() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        Mockito.verify(mockedView).hideStatusBar();
    }

    @Test
    public void initShouldCheckPermissions() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Mockito.verify(mockedPermissionManager).isHavePermission(mockedContext, permissions);
    }

    @Test
    public void shouldNotRequestForPermissionsWhenAllGranted() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

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
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        presenter.startRecording(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).startRecording(Mockito.any(Context.class), Mockito.any(TextureView.class));
    }

    @Test
    public void shouldEnableFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.startRecording(mockedContext, mockedTexture);

        Mockito.verify(mockedView).startFrontFlash();
    }


    //stopRecording()
    @Test
    public void shouldStopRecording() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        Mockito.when(mockedCameraHelper.stopRecording()).thenReturn(templateFile);
        presenter.stopRecording();

        Mockito.verify(mockedCameraHelper).stopRecording();
    }

    @Test
    public void shouldStopFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.stopRecording();

        Mockito.verify(mockedView).stopFrontFlash();
    }

    @Test
    public void showOpenFragment() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        presenter.stopRecording();

        Mockito.verify(mockedView).showFragment(Mockito.any(BaseFragment.class));
    }


    //takePicture()
    @Test
    public void shouldTakePicture() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);

        presenter.takePicture(mockedContext, mockedTexture);

        Mockito.verify(mockedCameraHelper).takePicture(Mockito.any(Context.class), Mockito.any(TextureView.class), Mockito.any(CameraHelper.PhotoCallback.class));
    }

    @Test
    public void shouldStartFrontFlashIfEnabled() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);
        presenter.isFlashEnabled = true;

        Mockito.when(mockedCameraHelper.isFrontCamera()).thenReturn(true);

        presenter.takePicture(mockedContext, mockedTexture);

        Mockito.verify(mockedView).startFrontFlash();
    }

    @Test
    public void shouldStopFrontFlashIfEnabledAfterSuccess() {
        CameraPresenter presenter = this.configurePresenter();
        presenter.init(mockedView, mockedContext, mockedTexture);
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
        
    }
}
