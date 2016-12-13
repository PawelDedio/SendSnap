package com.example.pdedio.sendsnap.camera;

import android.content.Context;
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
        void init(CameraView view);

        void startRecording();

        void stopRecording();

        void takePicture(Context context, TextureView textureView);
    }

    public interface CameraView extends BaseFragmentContract.BaseFragmentView {

        DonutProgress getCameraProgressBar();

        BaseButton getCameraButton();

        Context getActivityContext();

        TextureView getPreviewTextureView();

        BaseImageButton getChangeCameraButton();

        BaseImageButton getFlashButton();

        BaseFragmentActivity getBaseFragmentActivity();

        View getFrontFlashView();

        void showStatusBar();

        void hideStatusBar();
    }
}
