package com.example.pdedio.sendsnap.camera;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.TextureView;
import android.view.View;

import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.BaseFragmentContract;
import com.example.pdedio.sendsnap.common.views.BaseButton;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by pawel on 13.12.2016.
 */

public class CameraContract {

    public interface CameraPresenter extends BaseFragmentContract.BaseFragmentPresenter {

        void init(CameraView view, Context context, TextureView textureView);

        void startRecording(Context context, TextureView textureView);

        void stopRecording();

        void takePicture(Context context, TextureView textureView);

        void onBtnMenuClick();

        void switchCamera(Context context, TextureView textureView);

        void initCameraHelper(Context context, TextureView textureView);

        void changeFlashState();

        void enableAutoFocus();

        boolean onBackKeyClick();
    }

    public interface CameraView extends BaseFragmentContract.BaseFragmentView {

        void startFrontFlash();

        void stopFrontFlash();

        void showMenu();

        void hideMenu();

        void changeBtnFlashDrawableId(@DrawableRes int drawableId);

        void showDeniedPermissionDialog();
    }
}
