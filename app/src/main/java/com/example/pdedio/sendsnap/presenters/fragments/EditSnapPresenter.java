package com.example.pdedio.sendsnap.presenters.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;

import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.presenters.BasePresenter;

import org.androidannotations.annotations.EBean;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class EditSnapPresenter extends BasePresenter {


    private PresenterCallback presenterCallback;




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
        Log.e("showPhoto", this.presenterCallback.getSnapFile().getAbsolutePath());
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(this.presenterCallback.getSnapFile().getAbsolutePath());
            rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("showPhoto", rotation + " ");
        Bitmap bitmap = BitmapFactory.decodeFile(this.presenterCallback.getSnapFile().getAbsolutePath());
        this.presenterCallback.getPreviewImageView().setImageBitmap(bitmap);
    }

    private void showVideo() {

    }
    /*private void startMediaPlayer(File videoFile, Surface surface) {
        try {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setDataSource(videoFile.getAbsolutePath());
            this.mediaPlayer.setSurface(surface);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setLooping(true);
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.presenterCallback.getPreviewTextureView().setVisibility(View.GONE);
            this.mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    public interface PresenterCallback {
        TextureView getPreviewTextureView();

        ImageView getPreviewImageView();

        File getSnapFile();

        Consts.SnapType getSnapType();
    }
}
