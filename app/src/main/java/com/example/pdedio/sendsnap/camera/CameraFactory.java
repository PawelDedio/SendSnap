package com.example.pdedio.sendsnap.camera;

import android.hardware.Camera;

import org.androidannotations.annotations.EBean;

/**
 * Created by p.dedio on 20.12.16.
 */

@EBean
public class CameraFactory {


    public Camera getOldCamera(int cameraId) {
        return Camera.open(cameraId);
    }
}
