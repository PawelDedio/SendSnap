package com.example.pdedio.sendsnap.presenters.fragments;

import com.example.pdedio.sendsnap.presenters.BasePresenter;

import org.androidannotations.annotations.EBean;

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

    }

    @Override
    public void destroy() {

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

    }
}
