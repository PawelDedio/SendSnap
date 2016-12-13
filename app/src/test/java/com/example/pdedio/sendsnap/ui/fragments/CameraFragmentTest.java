package com.example.pdedio.sendsnap.ui.fragments;

import android.graphics.drawable.Drawable;

import com.example.pdedio.sendsnap.BuildConfig;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static android.Manifest.permission;

/**
 * Created by p.dedio on 03.11.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class CameraFragmentTest {

    private void grantPermissions() {
        ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
        shadowApplication.grantPermissions(permission.RECORD_AUDIO, permission.CAMERA,
                permission.WRITE_EXTERNAL_STORAGE);
    }

    @Test
    public void clickOnFlashButtonShouldChangeIcon() {
        this.grantPermissions();

        CameraFragment_ fragment = (CameraFragment_) CameraFragment_.builder().build();
        SupportFragmentTestUtil.startFragment(fragment);

        BaseImageButton flashButton = (BaseImageButton) fragment.findViewById(R.id.btnCameraFlash);
        Drawable oldImage = flashButton.getDrawable();
        flashButton.performClick();
        Drawable newImage = flashButton.getDrawable();

        Assert.assertNotEquals(oldImage, newImage);
    }
}
