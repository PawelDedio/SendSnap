package com.example.pdedio.sendsnap.edit_snap;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.example.pdedio.sendsnap.BaseFragmentPresenter;
import com.example.pdedio.sendsnap.R;
import com.example.pdedio.sendsnap.databinding.FragmentEditSnapBinding;
import com.example.pdedio.sendsnap.helpers.Consts;
import com.example.pdedio.sendsnap.helpers.SharedPreferenceManager;
import com.example.pdedio.sendsnap.BaseFragmentActivity;
import com.example.pdedio.sendsnap.select_recipient.SelectSnapRecipientFragment;
import com.example.pdedio.sendsnap.common.views.BaseImageButton;
import com.example.pdedio.sendsnap.common.views.BaseImageView;
import com.example.pdedio.sendsnap.common.views.BaseTextView;
import com.example.pdedio.sendsnap.select_recipient.SelectSnapRecipientFragment_;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.ColorRes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

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
        this.presenterCallback.hideStatusBar();
        this.configureViews();
    }

    @Override
    public void destroy() {
        this.presenterCallback = null;
    }

    @Override
    public void onResume() {
        if(this.presenterCallback != null && this.presenterCallback.getSnapType() == Consts.SnapType.VIDEO) {
            this.showVideo();
        }
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
                    //popFragment(mainView.getBaseFragmentActivity());
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

        this.presenterCallback.getSaveImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSnap();
            }
        });

        this.presenterCallback.getSendButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSnap();
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

    private void saveSnap() {
        String directory = this.presenterCallback.getBaseFragmentActivity().getString(R.string.snap_directory_name);
        Long timeStamp = System.currentTimeMillis() / 1000;
        String name = this.presenterCallback.getBaseFragmentActivity().getString(R.string.snap_saved_file_name, timeStamp);

        File snap = this.generateSnapFile(directory, name);

        Toast.makeText(this.presenterCallback.getBaseFragmentActivity(), R.string.edit_snap_snap_saved_message, Toast.LENGTH_SHORT).show();
        MediaScannerConnection.scanFile(this.presenterCallback.getBaseFragmentActivity(), new String[] { snap.getAbsolutePath()}, null, null);
    }

    private void sendSnap() {
        File snap = this.generateSnapFile();

        this.openFragment(snap);
    }

    private File generateSnapFile() {
        return this.generateSnapFile(null, null);
    }

    private File generateSnapFile(String directory, String fileName) {

        File snap;

        if(directory == null || fileName == null) {
            snap = this.presenterCallback.getSnapFile();
        } else {
            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + directory);

            if(!path.exists()) {
                path.mkdir();
            }

            String format = this.presenterCallback.getSnapType() == Consts.SnapType.PHOTO ? ".jpg" : ".mp4";
            snap = new File(path, fileName + format);
        }

        if(this.presenterCallback.getSnapType() == Consts.SnapType.PHOTO) {
            Bitmap bitmap = this.makeViewsScreenshot();
            this.saveBitmapToFile(bitmap, snap);
        } else if(snap.length() == 0) {
            File takenSNap  = this.presenterCallback.getSnapFile();
            this.copyFile(takenSNap, snap);
            takenSNap.delete();
        }

        return snap;
    }

    private Bitmap makeViewsScreenshot() {
        int width = this.presenterCallback.getPreviewImageView().getWidth();
        int height = this.presenterCallback.getPreviewImageView().getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        this.presenterCallback.getPreviewImageView().draw(canvas);
        this.presenterCallback.getFiltersView().draw(canvas);
        this.presenterCallback.getDrawingView().draw(canvas);
        MovableEditText etText = this.presenterCallback.getTextEt();
        etText.setDrawingCacheEnabled(true);
        Bitmap text = etText.getDrawingCache();
        canvas.drawBitmap(text, 0, etText.getTranslationY(), null);

        return bitmap;
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(File source, File destination) {
        try {
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(destination);
            FileChannel inChannel = in.getChannel();
            FileChannel outChannel = out.getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFragment(File file) {
        SelectSnapRecipientFragment fragment = SelectSnapRecipientFragment_.builder().snapFile(file).build();
        //this.openFragment(this.mainView.getBaseFragmentActivity(), fragment);
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

        DrawingView getDrawingView();

        BaseImageButton getUndoButton();

        View getColorSelectorButton();

        FragmentEditSnapBinding getBinding();

        FiltersView getFiltersView();

        void hideStatusBar();
    }
}
