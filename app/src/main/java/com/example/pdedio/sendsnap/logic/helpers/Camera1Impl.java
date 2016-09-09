package com.example.pdedio.sendsnap.logic.helpers;

import android.content.Context;
import android.view.TextureView;

import java.io.File;

/**
 * Created by p.dedio on 05.09.16.
 */
public class Camera1Impl implements CameraHelper {


    @Override
    public void init(Context context, TextureView textureView) {

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

    }

    @Override
    public File stopRecording() {
        return null;
    }
}
