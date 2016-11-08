package com.example.pdedio.sendsnap.presenters.fragments;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.logic.helpers.SharedPrefHelper_;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.views.BaseImageButton;
import com.example.pdedio.sendsnap.ui.views.BaseImageView;
import com.example.pdedio.sendsnap.ui.views.BaseTextView;
import com.example.pdedio.sendsnap.ui.views.MovableEditText;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class EditSnapPresenter extends BaseFragmentPresenter {


    private PresenterCallback presenterCallback;

    private MediaPlayer mediaPlayer;

    @Pref
    protected SharedPrefHelper_ sharedPrefHelper;


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

        this.presenterCallback.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popFragment(presenterCallback.getBaseFragmentActivity());
            }
        });

        this.presenterCallback.getTimerButton().setText(this.sharedPrefHelper.snapDuration().get().toString());

        this.presenterCallback.getTextEt().setOnCenter();

        this.presenterCallback.getMainLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovableEditText editText = presenterCallback.getTextEt();
                if(editText.isFocused()) {
                    editText.stopTyping();
                } else {
                    editText.setVisibility(View.VISIBLE);
                    editText.startTyping();
                }
            }
        });
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
        BaseFragmentActivity getBaseFragmentActivity();

        TextureView getPreviewTextureView();

        BaseImageView getPreviewImageView();

        File getSnapFile();

        Consts.SnapType getSnapType();

        Bitmap getSnapBitmap();

        BaseImageButton getCloseButton();

        BaseImageButton getDrawButton();

        BaseImageButton getAddTextButton();

        BaseTextView getTimerButton();

        BaseImageButton getSaveImageButton();

        BaseImageButton getSendButton();

        MovableEditText getTextEt();

        RelativeLayout getMainLayout();
    }
}
