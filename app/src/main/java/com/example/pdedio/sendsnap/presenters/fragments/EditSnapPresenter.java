package com.example.pdedio.sendsnap.presenters.fragments;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.databinding.FragmentEditSnapBinding;
import com.example.pdedio.sendsnap.logic.helpers.Consts;
import com.example.pdedio.sendsnap.logic.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.ui.activities.BaseFragmentActivity;
import com.example.pdedio.sendsnap.ui.dialogs.NumberPickerDialog;
import com.example.pdedio.sendsnap.ui.views.BaseImageButton;
import com.example.pdedio.sendsnap.ui.views.BaseImageView;
import com.example.pdedio.sendsnap.ui.views.BaseTextView;
import com.example.pdedio.sendsnap.ui.views.DrawingView;
import com.example.pdedio.sendsnap.ui.views.FiltersView;
import com.example.pdedio.sendsnap.ui.views.MovableEditText;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.ColorRes;

import java.io.File;

/**
 * Created by pawel on 19.09.2016.
 */
@EBean
public class EditSnapPresenter extends BaseFragmentPresenter {


    private PresenterCallback presenterCallback;

    private MediaPlayer mediaPlayer;

    @Bean
    protected SharedPreferenceManager sharedPreferenceManager;

    private boolean isDrawing;

    @ColorRes(R.color.edit_snap_default_draw)
    protected int defaultDrawColor;

    private NumberPickerDialog numberPickerDialog;

    private FragmentEditSnapBinding editSnapBinding;

    private static final int MAX_DISTANCE_FOR_CLICK = 15;



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
        this.editSnapBinding = this.presenterCallback.getBinding();

        this.editSnapBinding.setPrefs(this.sharedPreferenceManager);

        this.configureFilters();

        this.presenterCallback.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrawing) {
                    presenterCallback.getDrawingView().clearArea();
                    stopDrawing();
                } else {
                    popFragment(presenterCallback.getBaseFragmentActivity());
                }
            }
        });

        this.presenterCallback.getTimerButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker();
            }
        });

        this.presenterCallback.getFiltersView().getViewPager().setOnTouchListener(new View.OnTouchListener() {
            float touchY;
            float touchX;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchY = motionEvent.getRawY();
                        touchX = motionEvent.getRawX();
                        break;

                    case MotionEvent.ACTION_UP:
                        float currentY = motionEvent.getRawY();
                        float currentX = motionEvent.getRawX();

                        if(Math.abs(touchY - currentY) <= MAX_DISTANCE_FOR_CLICK && Math.abs(touchX - currentX) <= MAX_DISTANCE_FOR_CLICK) {
                            MovableEditText editText = presenterCallback.getTextEt();
                            if(editText.isTyping()) {
                                editText.stopTyping();
                            } else if(editText.getText().length() == 0) {
                                editText.startTyping(touchY);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        this.presenterCallback.getAddTextButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovableEditText editText = presenterCallback.getTextEt();
                if(editText.getVisibility() == View.VISIBLE) {
                    editText.setText("");
                    editText.stopTyping();
                } else {
                    editText.startTypingFromCenter();
                }
            }
        });

        DrawingView drawingView = this.presenterCallback.getDrawingView();
        drawingView.setColor(this.defaultDrawColor);

        this.presenterCallback.getDrawButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawingView view = presenterCallback.getDrawingView();
                if(view.isDrawingEnabled()) {
                    stopDrawing();
                } else {
                    startDrawing();
                }
            }
        });

        this.presenterCallback.getUndoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterCallback.getDrawingView().undoLastChange();
            }
        });
        this.presenterCallback.getColorSelectorButton().setBackgroundColor(this.defaultDrawColor);
        this.presenterCallback.getColorSelectorButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPicker();
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

    private void configureFilters() {

    }

    private void startDrawing() {
        this.isDrawing = true;

        this.presenterCallback.getAddTextButton().setVisibility(View.GONE);
        this.presenterCallback.getUndoButton().setVisibility(View.VISIBLE);
        this.presenterCallback.getDrawingView().startDrawing();
        this.presenterCallback.getColorSelectorButton().setVisibility(View.VISIBLE);
    }

    private void stopDrawing() {
        this.isDrawing = false;

        this.presenterCallback.getAddTextButton().setVisibility(View.VISIBLE);
        this.presenterCallback.getUndoButton().setVisibility(View.GONE);
        this.presenterCallback.getDrawingView().stopDrawing();
        this.presenterCallback.getColorSelectorButton().setVisibility(View.INVISIBLE);
    }

    private void showColorPicker() {
        SpectrumDialog.Builder builder = new SpectrumDialog.Builder(this.presenterCallback.getBaseFragmentActivity());
        builder.setColors(R.array.edit_snap_draw_colors)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setSelectedColor(this.presenterCallback.getDrawingView().getCurrentColor())
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if(positiveResult) {
                            presenterCallback.getColorSelectorButton().setBackgroundColor(color);
                            presenterCallback.getDrawingView().setColor(color);
                        } else {
                            Log.e("onColorSelected", "false");
                        }
                    }
                });

        builder.build().show(this.presenterCallback.getBaseFragmentActivity().getSupportFragmentManager(), "Dialog");
    }

    private void showNumberPicker() {
        if(this.numberPickerDialog == null) {
            this.numberPickerDialog = new NumberPickerDialog(presenterCallback.getBaseFragmentActivity());
            this.numberPickerDialog.setTitle(R.string.edit_snap_number_picker_title);
            this.numberPickerDialog.setMinValue(Consts.MIN_SNAP_DURATION);
            this.numberPickerDialog.setMaxValue(Consts.MAX_SNAP_DURATION);
            this.numberPickerDialog.setWrapSelectorWheel(false);
            this.numberPickerDialog.setResultListener(new NumberPickerDialog.ResultListener() {
                @Override
                public void onValueSet(int value) {
                    updateSnapDuration(value);
                }
            });
        }

        this.numberPickerDialog.setSelectedValue(this.sharedPreferenceManager.getSnapDuration());
        this.numberPickerDialog.show();
    }

    private void updateSnapDuration(int value) {
        this.sharedPreferenceManager.setSnapDuration(value);
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

        DrawingView getDrawingView();

        BaseImageButton getUndoButton();

        View getColorSelectorButton();

        FragmentEditSnapBinding getBinding();

        FiltersView getFiltersView();
    }
}
