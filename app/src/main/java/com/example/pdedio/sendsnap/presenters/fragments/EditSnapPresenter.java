package com.example.pdedio.sendsnap.presenters.fragments;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.presenters.BasePresenter;
import com.example.pdedio.sendsnap.ui.views.BaseImageView;

import org.androidannotations.annotations.EBean;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class EditSnapPresenter extends BasePresenter {


    private PresenterCallback presenterCallback;

    private MediaPlayer mediaPlayer;


    // Lifecycle
    public void init(PresenterCallback presenterCallback) {
        this.presenterCallback = presenterCallback;
    }


    @Override
    public void afterViews() {
        this.configureViews();
    }

    @Override
    public void destroy() {
        this.presenterCallback = null;
    }


    //Private methods
    private void configureViews() {
        switch(this.presenterCallback.getSnapType()) {
            case PHOTO :
                this.showPhoto();
                break;
            case VIDEO :
                this.showVideo();
                break;
        }
    }

    private void showPhoto() {
        Bitmap bitmap = this.presenterCallback.getSnapBitmap();
        this.presenterCallback.getPreviewImageView().setImageBitmap(bitmap);
        this.presenterCallback.getPreviewImageView().setVisibility(View.VISIBLE);
        this.presenterCallback.getPreviewTextureView().setVisibility(View.GONE);
    }

    private void showVideo() {
        this.presenterCallback.getPreviewImageView().setVisibility(View.GONE);
        this.presenterCallback.getPreviewTextureView().setVisibility(View.VISIBLE);
        this.presenterCallback.getPreviewTextureView().setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                try {
                    Surface surface = new Surface(surfaceTexture);
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(presenterCallback.getSnapFile().getAbsolutePath());
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.prepare();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                ActivityManager.isUserAMonkey();
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                ActivityManager.isUserAMonkey();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                ActivityManager.isUserAMonkey();
            }
        });
    }


    public interface PresenterCallback {
        TextureView getPreviewTextureView();

        BaseImageView getPreviewImageView();

        File getSnapFile();

        Consts.SnapType getSnapType();

        Bitmap getSnapBitmap();
    }
}
